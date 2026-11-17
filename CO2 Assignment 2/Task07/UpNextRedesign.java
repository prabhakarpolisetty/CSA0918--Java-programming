import java.util.*;

public class UpNextRedesign {
    static volatile int sink;

    static long measureArrayList(int n, int operations) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) list.add(i);
        long start = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            list.add(0, -i);       // shifts existing elements
            list.remove(0);
        }
        sink = list.size();
        return System.nanoTime() - start;
    }

    static long measureArrayDeque(int n, int operations) {
        Deque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < n; i++) deque.addLast(i);
        long start = System.nanoTime();
        for (int i = 0; i < operations; i++) {
            deque.addFirst(-i);   // constant-amortized end operations
            deque.removeFirst();
        }
        sink = deque.size();
        return System.nanoTime() - start;
    }

    public static void main(String[] args) {
        int operations = 20000;
        System.out.println("Up Next queue redesign: ArrayList vs ArrayDeque");
        for (int size : new int[]{1000, 10000, 50000}) {
            long oldTime = measureArrayList(size, operations);
            long newTime = measureArrayDeque(size, operations);
            System.out.printf("size=%d operations=%d | ArrayList=%.3f ms | ArrayDeque=%.3f ms%n",
                    size, operations, oldTime/1e6, newTime/1e6);
        }

        System.out.println("\nDiagnosis: ArrayList insertion/removal at index 0 shifts the remaining elements.");
        System.out.println("Redesign: ArrayDeque stores the Up Next queue and supports efficient operations at both ends.");
        System.out.println("Trade-off: ArrayDeque does not provide efficient indexed access; keep the main playlist");
        System.out.println("in ArrayList when random access is important.");
    }
}
