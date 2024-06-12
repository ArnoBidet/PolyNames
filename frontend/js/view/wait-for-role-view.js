import clearView from './utils/clear-view.js';
import View from './view.js';

export class WaitForRoleView extends View {
    
    constructor() {
        super();
    }
    render() {
        clearView();
        fetch("/frontend/templates/wait-for-role.html").then(response => response.text()).then(text => {
            this.root.innerHTML = text;
        });
    }


}