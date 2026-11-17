public class SeatBookingBenchmark {
    static class MethodLock {
        private final boolean[] seats;
        private int sold;
        MethodLock(int n) { seats = new boolean[n]; }
        synchronized boolean book(int seat) {
            if (seats[seat]) return false;
            seats[seat] = true; sold++; return true;
        }
    }

    static class BlockLock {
        private final boolean[] seats;
        private int sold;
        BlockLock(int n) { seats = new boolean[n]; }
        boolean book(int seat) {
            synchronized (this) {
                if (seats[seat]) return false;
                seats[seat] = true; sold++; return true;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final int seats = 10000;
        MethodLock method = new MethodLock(seats);
        BlockLock block = new BlockLock(seats);
        long start = System.nanoTime();
        Thread[] a = new Thread[seats];
        for (int i=0;i<seats;i++) { final int s=i; a[i]=new Thread(() -> method.book(s)); a[i].start(); }
        for (Thread t:a) t.join();
        long methodTime = System.nanoTime()-start;

        start = System.nanoTime();
        Thread[] b = new Thread[seats];
        for (int i=0;i<seats;i++) { final int s=i; b[i]=new Thread(() -> block.book(s)); b[i].start(); }
        for (Thread t:b) t.join();
        long blockTime = System.nanoTime()-start;

        System.out.printf("Synchronized method: %.3f ms%n", methodTime/1e6);
        System.out.printf("Synchronized block: %.3f ms%n", blockTime/1e6);
        System.out.println("Both designs prevent duplicate booking; timings include thread creation.");
    }
}
