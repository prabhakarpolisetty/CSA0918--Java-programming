import java.util.*;

class Team {
    final String name;
    final int points;
    final int goalDiff;

    Team(String name, int points, int goalDiff) {
        this.name = name; this.points = points; this.goalDiff = goalDiff;
    }

    @Override public String toString() {
        return name + "(" + points + "," + goalDiff + ")";
    }
}

class Leaderboard<T> {
    private final List<T> items = new ArrayList<>();
    private final Comparator<? super T> order;

    Leaderboard(Comparator<? super T> order) {
        this.order = order;
    }

    void add(T item) { items.add(item); }

    // TODO 1 completed: sort a copy, preserving insertion order in the source list.
    List<T> ranking() {
        List<T> result = new ArrayList<>(items);
        result.sort(order);
        return result;
    }

    void addAll(List<? extends T> newItems) {
        items.addAll(newItems);
    }
}

public class LeaderboardDemo {
    public static void main(String[] args) {
        // TODO 2 completed: points descending, goal difference descending, name ascending.
        Comparator<Team> byRank =
                Comparator.comparingInt((Team t) -> t.points).reversed()
                        .thenComparing(Comparator.comparingInt((Team t) -> t.goalDiff).reversed())
                        .thenComparing(t -> t.name);

        Leaderboard<Team> board = new Leaderboard<>(byRank);
        board.add(new Team("Panthers", 9, 4));
        board.add(new Team("Eagles", 9, 6));
        board.add(new Team("Cobras", 6, 1));
        board.add(new Team("Bisons", 9, 4));
        System.out.println(board.ranking());

        List<Team> extraTeams = Arrays.asList(
                new Team("Falcons", 6, 3), new Team("Wolves", 9, 4));
        board.addAll(extraTeams);
        System.out.println("Ranking with extra teams: " + board.ranking());
    }
}
