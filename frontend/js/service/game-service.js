import { SSEClient } from "../../libs/sse-client.js";
import { BASE_URL, SSE_URL } from "../constants.js";

export default class GameService {
    static async createGame() {
        const response = await fetch(`${BASE_URL}api/create-game`, {
            method: 'POST'
        });
        const newGame = JSON.parse(await response.json());
        return newGame;
    }

    static async waitForPlayer(game_code, callback) {
        let sseClient = new SSEClient(SSE_URL);
        await sseClient.connect()
        await sseClient.subscribe("player_waiting_" + game_code, callback);
    }

    static async joinGame(game_code) {
        const response = await fetch(`${BASE_URL}api/join-game/${game_code}`, {
            method: 'POST'
        });
        const newGame = JSON.parse(await response.json());
        return newGame;
    }

    static async choseRole(game_code, role) {
        throw new Error("Not implemented");
    }

}