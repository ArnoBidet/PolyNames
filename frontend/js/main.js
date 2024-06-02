import { SSEClient } from "../libs/sse-client.js";
import FormatErrorView from "./view/format-error-view.js";
import LandingView from "./view/landing-view.js";

let formatErrorView = new FormatErrorView();

function run() {
  let landingView = new LandingView();
  landingView.renderLanding();
  resize();
}

function resize() {
  if (window.innerWidth < 1080 || window.innerHeight < 800)
    formatErrorView.renderFormatError();
  else
    formatErrorView.removeError();

}

document.addEventListener("DOMContentLoaded", run);
window.addEventListener("resize", resize);
