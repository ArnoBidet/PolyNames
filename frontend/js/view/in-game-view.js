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
    return document.querySelector("#announcement");
  }

  get #sendHint() {
    return document.querySelector("#send-hint");
  }

  get #hintValueElement(){
    return document.querySelector("#wm-hint");
  }
  get #associatedGuessElement() {
    return document.querySelector("#wm-associated-guess");
  }

  get #hintContainer(){
    return document.querySelector("#clues .side-pannel-container");
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
      if (role() == PlayerRole.WORD_MASTER) {
      } else {
        this.#renderGuessMaster();
      }
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
    this.#sendHint.addEventListener("click", this.#makeGuess.bind(this));
  }

  #renderGuessMaster() {
    document.querySelector("#wm-inputs").style.display = "none";
    document.querySelector("#send-hint").style.display = "none";
    this.#announcement.innerHTML = "Choix en cours";
    InGameService.subscribeToGameUpdates((data) => {
      data = JSON.parse(data);
      this.#newGuess(data);
    });
  }

  async #makeGuess() {
    let data = await GameService.makeHint(this.#hintValueElement.value, this.#associatedGuessElement.value);
    data = JSON.parse(data);
    this.#newGuess(data);
  }

  #newGuess(data) {
    let newHint = document.createElement("div");
    newHint.classList.add("line");
    let hint = document.createElement("span");
    hint.innerHTML = data.hint;
    let associated_guess = document.createElement("span");
    associated_guess.innerHTML = data.associated_guess;
    newHint.append(hint);
    newHint.append(associated_guess);
    this.#hintContainer.prepend(newHint);
  }

  #evaluateInput() {
    this.#inputErrorsContainer.innerHTML = "";
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
}
