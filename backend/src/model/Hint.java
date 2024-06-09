package model;

public record Hint(String game_id, int game_round, String hint, int associated_cards, int found_cards, boolean is_done) {
    
}
