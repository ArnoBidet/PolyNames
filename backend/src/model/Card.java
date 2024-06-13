package model;

public record Card(String game_id, int grid_row, int grid_col, String word, String card_type, boolean is_discovered) {
    
}
