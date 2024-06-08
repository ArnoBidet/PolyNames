export default class LandingView {
    #b_create_game
    #i_joing_game
    #b_join_game
    constructor() {
        this._root = document.getElementById('root');
    }

    #onJoinGame() {
        
    }
    #onCreateGame() {
        
    }

    renderLanding() {
        fetch("/frontend/templates/landing.html").then(response => response.text()).then(text => {
            this._root.innerHTML = text;
            this.#b_create_game = document.getElementById('create-game');
            this.#i_joing_game = document.getElementById('join-game');
            this.#b_join_game = document.getElementById('join-game-button');

            this.#i_joing_game.addEventListener('input', () => {
                this.#b_join_game.disabled = this.#i_joing_game.value.length === 0;
                if (this.#i_joing_game.value.length < 8) {
                    this.#b_join_game.disabled = true;
                    this.#b_join_game.removeEventListener('click', this.#onJoinGame);
                } else {
                    if (this.#i_joing_game.value.length > 8)
                        this.#i_joing_game.value = this.#i_joing_game.value.substring(0, 8);
                    this.#b_join_game.disabled = false;
                    this.#b_join_game.addEventListener('click', this.#onJoinGame);
                }
            });

            this.#b_create_game.addEventListener('click', this.#onCreateGame);
        });
    }

}