import { SSEClient } from "../../libs/sse-client.js";
import { BASE_URL, SSE_URL } from "../constants.js";
import { game_id, user_id } from "../utils/sessionstorage.js";

export default class InGameService {
  static async getGame() {
    const response = await fetch("/api/game", {
      method: "GET",
      credentials: "include",
    });
    return await response.json();
  }

  static async subscribeToGameUpdates(callback) {
    let sseClient = new SSEClient(SSE_URL);
    await sseClient.connect();
    await sseClient.subscribe("game_flow_" + user_id() + "_" + game_id(), callback);
  }
}
