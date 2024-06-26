import { SSEClient } from "../../libs/sse-client.js";
import { BASE_URL, SSE_URL } from "../constants.js";
import {
  game_id,
  set_game_id,
  set_user_id,
  user_id,
  set_role,
} from "../utils/sessionstorage.js";

export default class GameService {
  static async createGame() {
    const response = await fetch(`${BASE_URL}api/create-game`, {
      method: "POST",
    });
    const newGame = JSON.parse(await response.json());
    set_game_id(newGame.game_id);
    set_user_id(newGame.user_id);
    return newGame;
  }

  static async waitForPlayer(callback) {
    let sseClient = new SSEClient(SSE_URL);
    await sseClient.connect();
    await sseClient.subscribe("player_waiting_" + game_id(), callback);
  }

  static async joinGame(game_id) {
    const response = await fetch(`${BASE_URL}api/join-game/${game_id}`, {
      method: "POST",
    });
    const newGame = JSON.parse(await response.json());
    set_game_id(newGame.game_id);
    set_user_id(newGame.user_id);
    return newGame;
  }

  static async choseRole(role) {
    const response = await fetch(`${BASE_URL}api/chose-role/${game_id()}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ role: role, user_id: user_id() }),
    });
    const result = JSON.parse(await response.json());
    set_role(result.role);
    return result;
  }
  static async waitForRole(callback) {
    let sseClient = new SSEClient(SSE_URL);
    await sseClient.connect();
    await sseClient.subscribe("wait_for_role_" + game_id(), callback);
  }

  static async getCards() {
    const response = await fetch(
      `${BASE_URL}api/${game_id()}/cards/${user_id()}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    return await response.json();
  }

  static async makeHint(hint, associated_guess) {
    const response = await fetch(
      `${BASE_URL}api/${game_id()}/hint`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          hint: hint,
          associated_guess: associated_guess,
          user_id: user_id()
        }),
      }
    );
    return await response.json();
  }

  static async makeGuess(row, col) {
    const response = await fetch(
      `${BASE_URL}api/${game_id()}/guess`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          grid_row : row,
          grid_col : col,
          user_id: user_id()
        }),
      }
    );
    return await response.json();
  }
}
