import View from './view.js';
import JoinCodeView from './join-code.js';
import clearView from './utils/clear-view.js';
import GameService from '../service/game-service.js';
import { WaitForRoleView } from './wait-for-role-view.js';

export default class LandingView extends View {
    get b_create_game() { return document.getElementById('create-game') }
    get i_join_game() { return document.getElementById('join-game') }
    get b_join_game() { return document.getElementById('join-game-button') }
    constructor() {
        super();
    }

    onJoinGame() {
        GameService.joinGame(this.i_join_game.value).then(game => {
            new WaitForRoleView().render(game.game_code);
        });
    }
    onCreateGame() {
        GameService.createGame().then(game => {
            new JoinCodeView().render(game.game_code);
        });
    }

    render() {
        clearView();
        fetch("/frontend/templates/landing.html").then(response => response.text()).then(text => {
            root.innerHTML = text;
            this.i_join_game.addEventListener('input', () => {
                this.b_join_game.disabled = this.i_join_game.value.length === 0;
                if (this.i_join_game.value.length < 8) {
                    this.b_join_game.disabled = true;
                    this.b_join_game.removeEventListener('click', onJoinGame);
                } else {
                    if (this.i_join_game.value.length > 8)
                        this.i_join_game.value = this.i_join_game.value.substring(0, 8);
                    this.b_join_game.disabled = false;
                    this.b_join_game.addEventListener('click', this.onJoinGame.bind(this));
                }
            });

            this.b_create_game.addEventListener('click', this.onCreateGame);
        });
    }

}