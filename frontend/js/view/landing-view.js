export default class LandingView {
    constructor() {
        this._root = document.getElementById('root');
    }

    renderLanding() {
        fetch("/frontend/pages/landing.html").then(response => response.text()).then(text => {
            this._root.innerHTML = text;
        });
    }

}