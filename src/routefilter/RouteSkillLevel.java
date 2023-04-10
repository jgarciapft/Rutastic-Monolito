package routefilter;

/**
 * Enumeration of different route skill levels
 */
public enum RouteSkillLevel {
    UNDEFINED, EASY, MEDIUM, HARD;

    /**
     * Get the enumeration constant equivalent of a route skill level name
     *
     * @param skillLevelString Name to be parsed
     * @return The equivalent enumeration constant
     */
    public static RouteSkillLevel parseSkillLevelFromString(String skillLevelString) {
        if (skillLevelString == null || skillLevelString.trim().isEmpty())
            return UNDEFINED;

        skillLevelString = skillLevelString.trim();

        switch (skillLevelString) {
            case "facil":
                return EASY;
            case "media":
                return MEDIUM;
            case "dificil":
                return HARD;
            default:
                return UNDEFINED;
        }
    }
}
