import InGameService from "../service/in-game-service.js";
export default class LandingView {
    constructor() {
        this._root = document.getElementById('root');
    }

    async renderLanding() {
        fetch("/frontend/pages/landing.html").then(response => response.text()).then(text => {
            this._root.innerHTML = text;
            let createGameButton = document.getElementById("create-game");
            createGameButton.addEventListener("click", InGameService.newGame);
        });
    }

    async newGame() {
        const response = await InGameService.newGame();
    }
}