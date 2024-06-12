package model;

public class PlayerRole {
    public static final String WORD_MASTER= "WORD_MASTER";
    public static final String GUESS_MASTER= "GUESS_MASTER";
    public static final String INDETERMINE= "INDETERMINE";

    public static boolean isValid(String role) {
        return role.equals(WORD_MASTER) || role.equals(GUESS_MASTER);
    }
}
