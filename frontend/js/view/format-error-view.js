import { MIN_HEIGHT, MIN_WIDTH } from "../constants.js";

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
            document.querySelector('#min-width').innerHTML = MIN_WIDTH;
            document.querySelector('#min-height').innerHTML = MIN_HEIGHT;
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