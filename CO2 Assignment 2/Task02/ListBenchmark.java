import java.util.*;

public class ListBenchmark {
    static volatile long sink;

    static long time(Runnable action) {
        long start = System.nanoTime();
        action.run();
        return System.nanoTime() - start;
    }

    static void test(int size) {
        List<Integer> array = new ArrayList<>();
        List<Integer> linked = new LinkedList<>();
        for (int i = 0; i < size; i++) { array.add(i); linked.add(i); }

        final int operations = Math.min(2000, size);
        long aEnd = time(() -> { for (int i = 0; i < operations; i++) array.add(i); });
        long lEnd = time(() -> { for (int i = 0; i < operations; i++) linked.add(i); });

        List<Integer> aFront = new ArrayList<>(array);
        List<Integer> lFront = new LinkedList<>(linked);
        long aInsertFront = time(() -> { for (int i = 0; i < operations; i++) aFront.add(0, i); });
        long lInsertFront = time(() -> { for (int i = 0; i < operations; i++) lFront.add(0, i); });

        List<Integer> aMiddle = new ArrayList<>(array);
        List<Integer> lMiddle = new LinkedList<>(linked);
        long aMiddleTime = time(() -> { for (int i = 0; i < operations; i++) aMiddle.add(aMiddle.size()/2, i); });
        long lMiddleTime = time(() -> { for (int i = 0; i < operations; i++) lMiddle.add(lMiddle.size()/2, i); });

        long aGet = time(() -> { long sum=0; for (int i=0;i<operations;i++) sum += array.get(i % size); sink=sum; });
        long lGet = time(() -> { long sum=0; for (int i=0;i<operations;i++) sum += linked.get(i % size); sink=sum; });

        long aIter = time(() -> { long sum=0; for (int x: array) sum+=x; sink=sum; });
        long lIter = time(() -> { long sum=0; for (int x: linked) sum+=x; sink=sum; });

        System.out.println("\nList size: " + size + ", repeated operations: " + operations);
        System.out.printf("Add end (ArrayList / LinkedList): %.3f / %.3f ms%n", aEnd/1e6, lEnd/1e6);
        System.out.printf("Insert front: %.3f / %.3f ms%n", aInsertFront/1e6, lInsertFront/1e6);
        System.out.printf("Insert middle: %.3f / %.3f ms%n", aMiddleTime/1e6, lMiddleTime/1e6);
        System.out.printf("Get indexed element: %.3f / %.3f ms%n", aGet/1e6, lGet/1e6);
        System.out.printf("Full iteration: %.3f / %.3f ms%n", aIter/1e6, lIter/1e6);
    }

    public static void main(String[] args) {
        test(1000);
        test(10000);
        test(50000);

        Deque<String> upNext = new ArrayDeque<>();
        upNext.addFirst("Urgent song");
        upNext.addLast("Next song");
        System.out.println("\nArrayDeque Up Next: " + upNext);
        System.out.println("ArrayList suits random access and append-heavy playlists; ArrayDeque suits frequent operations at both ends.");
    }
}
