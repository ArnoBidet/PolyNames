export class PlayerRole {
    static #_WORD_MASTER = "WORD_MASTER";
    static #_UNDEFINED = "UNDEFINED";
    static #_GUESS_MASTER = "GUESS_MASTER";

    static get WORD_MASTER() { return this.#_WORD_MASTER; }
    static get UNDEFINED() { return this.#_UNDEFINED; }
    static get GUESS_MASTER() { return this.#_GUESS_MASTER; }
}