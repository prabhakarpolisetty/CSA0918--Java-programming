class StationThread extends Thread {
    private final String station;
    private final int scripts;
    StationThread(String station, int scripts) {
        this.station = station; this.scripts = scripts;
    }
    @Override public void run() {
        try {
            for (int i = 1; i <= scripts; i++) {
                Thread.sleep(100);
            }
            System.out.println(station + " scanned " + scripts + " scripts");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(station + " interrupted");
        }
    }
}

class StationTask implements Runnable {
    private final String station;
    private final int scripts;
    StationTask(String station, int scripts) {
        this.station = station; this.scripts = scripts;
    }
    @Override public void run() {
        try {
            for (int i = 1; i <= scripts; i++) Thread.sleep(80);
            System.out.println(station + " scanned " + scripts + " scripts");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class ScannerStations {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Using Thread subclass:");
        Thread[] stations = {
            new StationThread("Station A", 3),
            new StationThread("Station B", 4),
            new StationThread("Station C", 2),
            new StationThread("Station D", 5)
        };
        for (Thread t : stations) t.start();
        System.out.println("Station A alive after start: " + stations[0].isAlive());
        for (Thread t : stations) t.join();
        System.out.println("All Thread stations completed.");

        System.out.println("\nUsing Runnable:");
        Thread[] workers = {
            new Thread(new StationTask("Station A", 3)),
            new Thread(new StationTask("Station B", 4)),
            new Thread(new StationTask("Station C", 2)),
            new Thread(new StationTask("Station D", 5))
        };
        for (Thread t : workers) t.start();
        for (Thread t : workers) t.join();
        System.out.println("All Runnable stations completed.");
    }
}
