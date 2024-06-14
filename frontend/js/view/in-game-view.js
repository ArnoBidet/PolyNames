import clearView from "./utils/clear-view.js";
import View from "./view.js";
import { role } from "../utils/sessionstorage.js";
import { PlayerRole } from "../utils/playerRole.js";
import GameService from "../service/game-service.js";
import InGameService from "../service/in-game-service.js";

export default class InGameView extends View {
  get #cards() {
    return document.querySelectorAll(".card");
  }

  get #announcement() {
    return document.querySelector("div#announcement");
  }

  get #sendHint() {
    return document.querySelector("button#send-hint");
  }

  get #wmInputsContainer() {
    return document.querySelector("div#wm-inputs");
  }

  get #gmInputsContainer() {
    return document.querySelector("div#gm-inputs");
  }

  get #hintElement() {
    return document.querySelector("input#wm-hint");
  }

  get #associatedGuessElement() {
    return document.querySelector("input#wm-associated-guess");
  }

  get #hintContainer() {
    return document.querySelector("div#clues div.side-pannel-container");
  }

  get #inputErrorsContainer() {
    return document.querySelector("div#input-errors");
  }

  constructor() {
    super();
  }

  render() {
    clearView();
    fetch("/frontend/templates/in-game-view.html").then(async (response) => {
      this.root.innerHTML += await response.text();
      const card_list = await GameService.getCards();
      this.#renderGrid(card_list);
    });
  }

  async #renderGrid(card_list) {
    let card_template = await fetch("/frontend/templates/card.html").then(
      (response) => response.text()
    );

    document.querySelector("h1.master-title").innerHTML =
      "Maître des " +
      (role() === PlayerRole.WORD_MASTER ? "mots" : "intuitions");

    let cardGrid = document.querySelector(".card-grid");

    card_list.forEach((card, index) => {
      let current_card = card_template;
      current_card = current_card.replace(
        "data-row",
        `data-row="${card.grid_row}"`
      );
      current_card = current_card.replace(
        "data-column",
        `data-column="${card.grid_col}"`
      );
      current_card = current_card.replaceAll("{word}", card.word);
      cardGrid.innerHTML += current_card;
      current_card = cardGrid.querySelector(
        `.card[data-row='${card.grid_row}'][data-column='${card.grid_col}'`
      );
      current_card.style.gridRow = card.grid_row + 1;
      current_card.style.gridColumn = card.grid_col + 1;
    });
    this.#cards.forEach((card) => {
      card.style.rotate = `${Math.random() * 6 - 3}deg`;
    });
    this.#animate(card_list);

    if (role() === PlayerRole.WORD_MASTER) {
      this.#renderWordMaster(card_list);
    } else {
      this.#renderGuessMaster();
    }
  }

  #animate(card_list) {
    card_list
      .sort((a, b) => {
        return a.grid_row - b.grid_row - (a.grid_col - b.grid_col);
      })
      .forEach((card, index) => {
        let current_card = document.querySelector(
          `.card[data-row='${card.grid_row}'][data-column='${card.grid_col}'`
        );
        setTimeout(() => {
          current_card.classList.add("animated");
        }, 50 * index);
      });
  }

  #renderWordMaster(card_list) {
    this.#announcement.innerHTML = "C'est à vous de donner un indice";
    this.#gmInputsContainer.style.display = "none";
    card_list.forEach((card) => {
      document
        .querySelector(
          `.card[data-row='${card.grid_row}'][data-column='${card.grid_col}'`
        )
        .classList.add(card.card_type.toLowerCase(), "word-master-card");
    });
    this.#hintElement.addEventListener("input", this.#evaluateInput.bind(this));
    this.#associatedGuessElement.addEventListener("input", this.#evaluateInput.bind(this));
    this.#evaluateInput();
    this.#sendHint.addEventListener("click", this.#makeHint.bind(this));
    InGameService.subscribeGuessMasterUpdates(this.#onGuessResponse.bind(this));
  }

  #renderGuessMaster() {
    this.#announcement.innerHTML = "En attente de l'indice du maître des mots";
    this.#wmInputsContainer.style.display = "none";
    InGameService.subscribeWordMasterUpdates(this.#onHintResponse.bind(this));
  }

  #makeHint() {
    GameService.makeHint(this.#hintElement.value, this.#associatedGuessElement.value).then(this.#onHintResponse.bind(this));
  }
  #makeGuess(row, col) {
    GameService.makeGuess(row, col).then(this.#onGuessResponse.bind(this));

  }

  #newHint(data) {
    let newHint = document.createElement("div");
    newHint.classList.add("line");
    let hint = document.createElement("span");
    hint.innerHTML = data.hint;
    let associated_guess = document.createElement("span");
    associated_guess.innerHTML = data.associated_guess;
    let found_cards = document.createElement("span");
    found_cards.innerHTML = 0;
    newHint.append(hint);
    newHint.append(associated_guess);
    newHint.append(found_cards);
    this.#hintContainer.prepend(newHint);
  }

  #newGuess(data) {
    let card = document.querySelector(`.card[data-row='${data.grid_row}'][data-column='${data.grid_col}'`);
    card.classList.add(data.card_type.toLowerCase(), "revealed");

    card.classList.remove("clickable");
    let clone = card.cloneNode(true);
    card.parentNode.replaceChild(clone, card);
  }

  #evaluateInput() {
    this.#inputErrorsContainer.innerHTML = "";
    this.#hintElement.value = this.#hintElement.value.trim();
    this.#hintElement.value = this.#hintElement.value.replace(/[^a-zA-Z-]+/g, '')
    if (this.#hintElement.value === "" || this.#associatedGuessElement.value === "") {
      this.#sendHint.disabled = true;
    } else {
      this.#sendHint.disabled = false;
    }
    if ((this.#associatedGuessElement.value <= 0 || this.#associatedGuessElement.value > 8) && this.#associatedGuessElement.value !== "") {
      this.#sendHint.disabled = true;
      this.#inputErrorsContainer.innerHTML += "<span>Le nombre associé doit être compris entre 1 et 8</span>";
    }
    if (this.#hintElement.value.length > 30) {
      this.#sendHint.disabled = true;
      this.#inputErrorsContainer.innerHTML += "<span>Le mot doit contenir moins de 30 caractères</span>";
    }
  }

  #toggleCardClickability() {
    this.#cards.forEach((card) => {
      if (card.classList.contains("revealed"))
        return
      if (!card.classList.contains("clickable")) {
        card.classList.add("clickable");
        card.addEventListener("click", () => this.#makeGuess(card.dataset.row, card.dataset.column));
      } else {
        card.classList.remove("clickable");
        card.removeEventListener("click", () => this.#makeGuess(card.dataset.row, card.dataset.column));
      }
    });
  }

  #onGuessResponse(data) {
    this.#newGuess(data);
    this.#updateRemainingGuessForRound();
    if (data.card_type === "KILLER") {
      this.#onDeath();
    } else if (data.card_type === "GUESS") {
      this.#onFound();
    }
  }

  #onHintResponse(data) {
    this.#newHint(data);
    document.querySelector("#last-hint-value").innerHTML = data.hint;
    document.querySelector("#last-hint-linked").innerHTML = data.associated_guess;
    this.#updateRemainingGuessForRound();
    if (role() === PlayerRole.WORD_MASTER) {
      this.#hintElement.value = "";
      this.#associatedGuessElement.value = "";
      this.#sendHint.disabled = true;
      this.#hintElement.disabled = true
      this.#associatedGuessElement.disabled = true;
      this.#announcement.innerHTML = `À l'autre joueur`;
    } else {
      this.#announcement.innerHTML = `À vous`;

      this.#toggleCardClickability(true);
    }
  }

  #onFound() {
    this.score = document.querySelector("#remaining");
    this.score.innerHTML = parseInt(this.score.innerHTML) - 1;
    if (this.score.innerHTML == 0) {
      this.score = document.querySelector("span#score");
      this.root.innerHTML = "Vous avez gagné !<br> Votre score est de " + this.score.innerHTML + "<br> Vous pouvez recommencer en cliquant <a href='/frontend/'><button class=\"primary\">ici</button></a>";
      this.#toggleCardClickability();
    }
  }
  #onDeath() {
    this.score = document.querySelector("span#score");
    this.root.innerHTML = "Vous avez perdu !<br> Votre score est de " + this.score.innerHTML + "<br> Vous pouvez recommencer en cliquant <a href='/frontend/'><button class=\"primary\">ici</button></a>";
    this.#toggleCardClickability();
  }

  #updateRemainingGuessForRound() {
    let lastHintRemaining = document.querySelector("#last-hint-remaining");
    if (lastHintRemaining.innerHTML === "-"){
      let nextValue = parseInt(document.querySelector("#last-hint-linked").innerHTML) + 1
      nextValue = nextValue > 8 ? 8 : nextValue;
      lastHintRemaining.innerHTML = nextValue;
    }else
      lastHintRemaining.innerHTML = parseInt(lastHintRemaining.innerHTML) - 1;
  }
}
