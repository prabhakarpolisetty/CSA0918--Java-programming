import java.util.*;

public class PharmacyInventory {
    static class Medicine {
        final String code;
        final String name;
        int stock;
        Medicine(String code, String name, int stock) {
            this.code = code; this.name = name; this.stock = stock;
        }
        @Override public String toString() { return code + " - " + name + " : " + stock; }
    }

    static Map<String, Medicine> makeMap(String type, int count) {
        Map<String, Medicine> map;
        if (type.equals("HashMap")) map = new HashMap<>();
        else if (type.equals("TreeMap")) map = new TreeMap<>();
        else map = new LinkedHashMap<>();
        for (int i = 0; i < count; i++) {
            String code = String.format("MED%06d", i);
            map.put(code, new Medicine(code, "Medicine-" + i, 100 + i));
        }
        return map;
    }

    static void test(String type, int count) {
        Map<String, Medicine> map = makeMap(type, count);
        Random random = new Random(42);
        int lookups = 100000;
        long start = System.nanoTime();
        int found = 0;
        for (int i = 0; i < lookups; i++) {
            String key = String.format("MED%06d", random.nextInt(count));
            if (map.get(key) != null) found++;
        }
        long lookupTime = System.nanoTime() - start;

        start = System.nanoTime();
        String first = map.keySet().iterator().next();
        String last = "";
        for (String key : map.keySet()) last = key;
        long iterationTime = System.nanoTime() - start;

        System.out.printf("%s | medicines=%d | lookups=%d | lookup=%.3f ms | iteration=%.3f ms | found=%d | first=%s | last=%s%n",
                type, count, lookups, lookupTime/1e6, iterationTime/1e6, found, first, last);
    }

    public static void main(String[] args) {
        for (int n : new int[]{1000, 10000, 50000}) {
            test("HashMap", n);
            test("TreeMap", n);
            test("LinkedHashMap", n);
            System.out.println();
        }

        Map<String, Medicine> codeOrder = new TreeMap<>();
        Map<String, Medicine> insertionOrder = new LinkedHashMap<>();
        for (int i : new int[]{3, 1, 2}) {
            String code = "MED" + i;
            Medicine m = new Medicine(code, "Medicine-" + i, 10*i);
            codeOrder.put(code, m);
            insertionOrder.put(code, m);
        }
        System.out.println("Code order report: " + codeOrder.values());
        System.out.println("Insertion order report: " + insertionOrder.values());
    }
}
