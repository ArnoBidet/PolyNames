export class CartType {
	static #_ASSASSIN = "ASSASSIN";
	static #_NEUTRAL = "NEUTRAL";
	static #_WORD = "WORD";

	static get ASSASSIN() { return this.#_ASSASSIN; }
	static get NEUTRAL() { return this.#_NEUTRAL; }
	static get WORD() { return this.#_WORD; }
}