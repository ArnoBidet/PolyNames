import clearView from './utils/clear-view.js';
import View from './view.js';

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
                console.log('word master');
            });
            this.b_guess_master.addEventListener('click', () => {
                console.log('guess master');
            });
        });
    }


}