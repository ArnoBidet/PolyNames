package model;

public class PlayerRole {
    public static final String MOTS= "MOTS";
    public static final String INTUITIONS= "INTUITIONS";
    public static final String INDETERMINE= "INDETERMINE";

    public static boolean isValid(String role) {
        return role.equals(MOTS) || role.equals(INTUITIONS);
    }
}
