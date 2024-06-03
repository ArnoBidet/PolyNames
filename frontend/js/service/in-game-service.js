import SSEClient from "../../libs/sse-client.js";

export default class InGameService {
  constructor() {}
  static sse = new SSEClient();
  static async newGame() {
    const response = await fetch("http://localhost:8081/api/new-game", {
      method: "POST",
    });
    this.sseClient = new SSEClient("my.domain.com:port_number");
    this.sseClient.connect();
    sse.subscribe("http://localhost:8081/api/game-state", this.updateGameState);
    return await response.json();
  }

  updateGameState(event) {
    console.log(event);
  }
}
