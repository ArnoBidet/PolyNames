import GameService from '../service/game-service.js';
import clearView from './utils/clear-view.js';
import View from './view.js';
import InGameView from './in-game-view.js';
import { PlayerRole } from '../utils/playerRole.js';

export class ChoseRoleView extends View{
    get b_word_master() { return document.getElementById('word-master') }
    get b_guess_master() { return document.getElementById('guess-master') }
    constructor() {
        super()
    }
    render() {
        clearView();
        fetch("/frontend/templates/chose-role.html").then(response => response.text()).then(text => {
            this.root.innerHTML += text;
            this.b_word_master.addEventListener('click', () => {
                GameService.choseRole(PlayerRole.GUESS_MASTER).then((data) => {
                    if (data.role) {
                        new InGameView().render(data.role);
                    }
                });
            });
            this.b_guess_master.addEventListener('click', () => {
                GameService.choseRole(PlayerRole.WORD_MASTER).then((data) => {
                    if (data.role) {
                        new InGameView().render(data.role);
                    }
                });
            });
        });
    }


}