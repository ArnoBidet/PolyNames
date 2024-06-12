import clearView from "./utils/clear-view.js";
import View from "./view.js";

export default class InGameView extends View {
    constructor() {
        super();
    }

    render() {
        clearView();
        fetch("/frontend/templates/in-game-view.html").then(response => response.text()).then(text => {
            this.root.innerHTML += text;
            let cardGrid = document.createElement("div");
            cardGrid.classList.add("card-grid");

            list_cards.forEach((card, index) => {
                const cardElement = document.createElement("div");
                cardElement.classList.add("card");
                cardElement.style.gridRow = card.grid_row + 1;
                cardElement.style.gridColumn = card.grid_col + 1;
                cardElement.innerHTML = `
                <p>${card.word}</p>
                <h3>${card.word}</h3>
            `;
                cardGrid.appendChild(cardElement);
                setTimeout(() => {
                    cardElement.classList.add("animated");
                }, 50 * index);
            });
            const cards = document.querySelectorAll(".card");
            cards.forEach((card) => {
                card.style.rotate = `${Math.random() * 6 - 3}deg`;
            });
            root.appendChild(cardGrid);
        });
    }
}

let list_cards = [
    {
      word: "Bateau",
      grid_row: 0,
      grid_col: 0,
      is_discovered: false,
    },
    {
      word: "Étoile",
      grid_row: 1,
      grid_col: 0,
      is_discovered: false,
    },
    {
      word: "Reine",
      grid_row: 2,
      grid_col: 0,
      is_discovered: false,
    },
    {
      word: "Souris",
      grid_row: 3,
      grid_col: 0,
      is_discovered: false,
    },
    {
      word: "Château",
      grid_row: 4,
      grid_col: 0,
      is_discovered: false,
    },
    {
      word: "Plume",
      grid_row: 0,
      grid_col: 1,
      is_discovered: false,
    },
    {
      word: "Aigle",
      grid_row: 1,
      grid_col: 1,
      is_discovered: false,
    },
    {
      word: "Banane",
      grid_row: 2,
      grid_col: 1,
      is_discovered: false,
    },
    {
      word: "Magie",
      grid_row: 3,
      grid_col: 1,
      is_discovered: false,
    },
    {
      word: "Océan",
      grid_row: 4,
      grid_col: 1,
      is_discovered: false,
    },
    {
      word: "Trompette",
      grid_row: 0,
      grid_col: 2,
      is_discovered: false,
    },
    {
      word: "Fusée",
      grid_row: 1,
      grid_col: 2,
      is_discovered: false,
    },
    {
      word: "Diamant",
      grid_row: 2,
      grid_col: 2,
      is_discovered: false,
    },
    {
      word: "Forêt",
      grid_row: 3,
      grid_col: 2,
      is_discovered: false,
    },
    {
      word: "Drôle",
      grid_row: 4,
      grid_col: 2,
      is_discovered: false,
    },
    {
      word: "Fantôme",
      grid_row: 0,
      grid_col: 3,
      is_discovered: false,
    },
    {
      word: "Galaxie",
      grid_row: 1,
      grid_col: 3,
      is_discovered: false,
    },
    {
      word: "Horloge",
      grid_row: 2,
      grid_col: 3,
      is_discovered: false,
    },
    {
      word: "Montagne",
      grid_row: 3,
      grid_col: 3,
      is_discovered: false,
    },
    {
      word: "Lune",
      grid_row: 4,
      grid_col: 3,
      is_discovered: false,
    },
    {
      word: "Piano",
      grid_row: 0,
      grid_col: 4,
      is_discovered: false,
    },
    {
      word: "Robot",
      grid_row: 1,
      grid_col: 4,
      is_discovered: false,
    },
    {
      word: "Sirène",
      grid_row: 2,
      grid_col: 4,
      is_discovered: false,
    },
    {
      word: "Tigre",
      grid_row: 3,
      grid_col: 4,
      is_discovered: false,
    },
    {
      word: "Voleur",
      grid_row: 4,
      grid_col: 4,
      is_discovered: false,
    },
  ];