import View from './view.js';
import LandingView from './landing-view.js';
import clearView from './utils/clear-view.js';
import GameService from '../service/game-service.js';
import { ChoseRoleView } from './chose-role-view.js';
export default class JoinCodeView extends View {
    get b_copy_code() { return document.getElementById("copy-join-code") }
    get s_join_code() { return document.getElementById("join-code-value") }
    get b_cancel() { return document.getElementById("join-cancel") }
    constructor() {
        super();
    }
    render(joinCode) {
        clearView();
        fetch("/frontend/templates/join-code.html").then(response => response.text()).then(text => {
            this.root.innerHTML += text;
            this.s_join_code.innerText = joinCode;
            this.b_copy_code.addEventListener("click", () => {
                navigator.clipboard.writeText(this.s_join_code.innerText);
            });
            this.b_cancel.addEventListener("click", () => {
                new LandingView().render();
            });

            GameService.waitForPlayer(joinCode, (event) => {
                new ChoseRoleView().render();
            });
        });
    }

}