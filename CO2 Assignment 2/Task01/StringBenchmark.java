import java.util.*;

public class StringBenchmark {
    private static volatile int blackhole;

    interface Builder { String build(int fields); }

    static String ticketWithString(int fields) {
        String ticket = "";
        for (int i = 0; i < fields; i++) ticket += "FIELD" + i + "|";
        return ticket;
    }

    static String ticketWithStringBuilder(int fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields; i++) sb.append("FIELD").append(i).append('|');
        return sb.toString();
    }

    static String ticketWithStringBuffer(int fields) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < fields; i++) sb.append("FIELD").append(i).append('|');
        return sb.toString();
    }

    static long benchmark(Builder builder, int fields, int tickets, int repetitions) {
        long total = 0;
        for (int r = 0; r < repetitions; r++) {
            long start = System.nanoTime();
            int checksum = 0;
            for (int t = 0; t < tickets; t++) checksum += builder.build(fields).length();
            total += System.nanoTime() - start;
            blackhole = checksum;
        }
        return total / repetitions;
    }

    static void runWorkload(String label, int fields, int tickets) {
        int reps = 3;
        System.out.println("\n" + label + " (" + tickets + " tickets x " + fields + " fields)");
        System.out.printf("String:        %.3f ms%n",
                benchmark(StringBenchmark::ticketWithString, fields, tickets, reps) / 1_000_000.0);
        System.out.printf("StringBuilder: %.3f ms%n",
                benchmark(StringBenchmark::ticketWithStringBuilder, fields, tickets, reps) / 1_000_000.0);
        System.out.printf("StringBuffer:  %.3f ms%n",
                benchmark(StringBenchmark::ticketWithStringBuffer, fields, tickets, reps) / 1_000_000.0);
    }

    public static void main(String[] args) {
        runWorkload("Workload 1 - short tickets", 5, 100);
        runWorkload("Workload 2 - heavy load", 50, 10000);
        runWorkload("Workload 3 - many fields", 200, 1000);

        System.out.println("\nEstimated behaviour:");
        System.out.println("Repeated String concatenation creates intermediate immutable strings.");
        System.out.println("StringBuilder uses a mutable buffer and is normally fastest for local construction.");
        System.out.println("StringBuffer synchronizes its methods and may cost more in single-threaded use.");
        System.out.println("Recommendation: StringBuilder per ticket; a thread-safe design such as synchronized");
        System.out.println("logging or a dedicated logging queue for a shared log. Do not share one ticket builder.");
    }
}
