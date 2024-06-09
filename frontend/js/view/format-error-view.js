export default class FormatErrorView {
    static isDisplayed = false;
    constructor() {
    }
    static render() {
        if (FormatErrorView.isDisplayed) {
            return;
        }
        FormatErrorView.isDisplayed = true;
        fetch("/frontend/templates/format-error.html").then(response => response.text()).then(text => {
            document.body.innerHTML += text;
        });
    }


    static remove() {
        if (!FormatErrorView.isDisplayed) {
            return;
        }
        FormatErrorView.isDisplayed = false;
        document.querySelector('.dialog-overlay').remove();
    }
}