import { SSEClient } from "../libs/sse-client.js";
import InGameView from "./view/in-game-view.js";

function run() {
  let inGameView =  new InGameView();
  inGameView.displayProducts();
  const sseClient = new SSEClient("localhost:8080");
  sseClient.connect();

  sseClient.subscribe("bids", (data) => {
    console.log(data);
    productsView.updateBid(data);
  });
}

document.addEventListener("DOMContentLoaded", run);
