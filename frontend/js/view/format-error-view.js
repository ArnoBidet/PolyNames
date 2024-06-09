export default class FormatErrorView {
    isDisplayed = false;
    constructor() {
    }
    render() {
        if (this.isDisplayed) {
            return;
        }
        this.isDisplayed = true;
        fetch("/frontend/templates/format-error.html").then(response => response.text()).then(text => {
            document.body.innerHTML += text;
        });
    }


    remove() {
        if (!this.isDisplayed) {
            return;
        }
        this.isDisplayed = false;
        document.querySelector('.dialog-overlay').remove();
    }
}