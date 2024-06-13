export class CartType {
	static #_KILLER = "KILLER";
	static #_NEUTRAL = "NEUTRAL";
	static #_WORD = "GUESS";

	static get KILLER() { return this.#_KILLER; }
	static get NEUTRAL() { return this.#_NEUTRAL; }
	static get GUESS() { return this.#_WORD; }
}