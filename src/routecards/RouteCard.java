package routecards;

public class RouteCard {
    private Location start;
    private Location end;
    private int value;
    private boolean completed = false;

    public RouteCard(Location start, Location end, int value) {
        this.start = start;
        this.end = end;
        this.value = value;
    }

    public void setCompleted() {
        completed = true;
    }

    public boolean isCompleted() {
        return completed;
    }
}
