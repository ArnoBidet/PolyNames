export default class InGameService {
    static async getGame() {
        const response = await fetch(
            '/api/game',
            {
                method: 'GET',
                credentials: 'include'
            }
        );
        return await response.json();
    }
}