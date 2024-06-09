import { MIN_HEIGHT, MIN_WIDTH } from "./constants.js";
import FormatErrorView from "./view/format-error-view.js";
import LandingView from "./view/landing-view.js";

function run() {
  new LandingView().render();
  resize();
}

function resize() {
  if (window.innerWidth < MIN_WIDTH || window.innerHeight < MIN_HEIGHT)
    FormatErrorView.render();
  else
    FormatErrorView.remove();
}

document.addEventListener("DOMContentLoaded", run);
window.addEventListener("resize", resize);
