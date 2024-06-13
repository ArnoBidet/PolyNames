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
    root.appendChild(cardGrid);
    this.#animate(card_list);
    if (role() == PlayerRole.WORD_MASTER) {
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
        .classList.add(card.card_type.toLowerCase());
    });
    fetch("/frontend/templates/word-master-overlay.html").then(
      async (response) => {
        this.overlay.innerHTML = await response.text();
      }
    );
  }

  #renderGuessMaster() {
    fetch("/frontend/templates/guess-master-overlay.html").then(
      async (response) => {
        this.overlay.innerHTML = await response.text();
      }
    );
  }
}
