import clearView from './utils/clear-view.js';
import View from './view.js';
import GameService from '../service/game-service.js';
import InGameView from './in-game-view.js';
import { set_role } from '../utils/sessionstorage.js';

export class WaitForRoleView extends View {

    constructor() {
        super();
    }
    render() {
        clearView();
        fetch("/frontend/templates/wait-for-role.html").then(response => response.text()).then(text => {
            this.root.innerHTML = text;
            GameService.waitForRole((data) => {
                data = JSON.parse(data);
                if (data.role) {
                    set_role(data.role);
                    new InGameView().render();
                }
            });
        });
    }


}