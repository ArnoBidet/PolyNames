export default class InGameService {
    static async getGame() {
        const response = await fetch('/api/game');
        return await response.json();
    }
}