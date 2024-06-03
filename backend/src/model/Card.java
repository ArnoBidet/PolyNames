package model;

public record Card(String game_code, int grid_row, int grid_col, int word_id, String card_type, boolean is_done) {
    
}
