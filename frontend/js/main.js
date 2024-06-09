import FormatErrorView from "./view/format-error-view.js";
import LandingView from "./view/landing-view.js";

function run() {
  new LandingView().render();
  resize();
}

function resize() {
  if (window.innerWidth < 1000 || window.innerHeight < 700)
    new FormatErrorView().render();
  else
    new FormatErrorView().remove();

}

document.addEventListener("DOMContentLoaded", run);
window.addEventListener("resize", resize);
