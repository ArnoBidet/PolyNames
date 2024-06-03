package model;

public record Hint(String game_code, int game_round, String hint, int associated_cards, int found_cards, boolean has_failed) {
    
}
