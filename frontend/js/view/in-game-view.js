import clearView from "./utils/clear-view.js";
import View from "./view.js";
import { role } from "../utils/sessionstorage.js";
import { PlayerRole } from "../utils/playerRole.js";
import GameService from "../service/game-service.js";

export default class InGameView extends View {
  get #cards() {
    return document.querySelectorAll(".card");
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
    console.log(card_list);
    let card_template = await fetch("/frontend/templates/card.html").then(
      (response) => response.text()
    );

    let cardGrid = document.querySelector(".card-grid");

    card_list.forEach(card => {
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
        ".card[data-row='" +
          card.grid_row +
          "'][data-column='" +
          card.grid_col +
          "'"
      );
      current_card.style.gridRow = card.grid_row + 1;
      current_card.style.gridColumn = card.grid_col + 1;
    });
    this.#cards.forEach((card) => {
      card.style.rotate = `${Math.random() * 6 - 3}deg`;
    });
    root.appendChild(cardGrid);
    this.#cards.forEach((card, index) => {
      setTimeout(() => {
        card.classList.add("animated");
      }, 50 * index);
    });
  }

  #renderWordMaster() {
    let wordMaster = document.querySelector(".word-master");
    // wordMaster.style.display = "block";
  }

  #renderGuessMaster() {
    let guessMaster = document.querySelector(".guess-master");
    // guessMaster.style.display = "block";
  }
}
