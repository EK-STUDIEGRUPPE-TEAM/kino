package dk.kino.kino.model;

public enum EmployeeType {

    SALES("Sales"),
    MOVIE_OPERATOR("Movie Operator"),
    TICKET_INSPECTOR("Ticket Inspector");

    private final String displayName;

    EmployeeType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
