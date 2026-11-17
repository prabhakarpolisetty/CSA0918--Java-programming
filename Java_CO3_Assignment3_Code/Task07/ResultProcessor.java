import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ResultProcessor {
    static class Result {
        final String file;
        final int score;
        Result(String file, int score) { this.file=file; this.score=score; }
    }

    static class ProcessingOutcome {
        final String file;
        final Result result;
        final String error;
        ProcessingOutcome(String file, Result result, String error) {
            this.file=file; this.result=result; this.error=error;
        }
    }

    static class ScoreTask implements Callable<ProcessingOutcome> {
        private final Path path;
        ScoreTask(Path path) { this.path=path; }

        @Override public ProcessingOutcome call() {
            try {
                List<String> lines = Files.readAllLines(path);
                if (lines.isEmpty()) throw new IOException("Empty score file");
                int score = Integer.parseInt(lines.get(0).trim());
                if (score < 0 || score > 100) throw new IllegalArgumentException("Score must be 0 to 100");
                return new ProcessingOutcome(path.getFileName().toString(), new Result(path.toString(), score), null);
            } catch (Exception e) {
                return new ProcessingOutcome(path.getFileName().toString(), null, e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }

    static void process(Path inputDir, int poolSize) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        ConcurrentLinkedQueue<Result> successes = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<String> failures = new ConcurrentLinkedQueue<>();
        List<Future<ProcessingOutcome>> futures = new ArrayList<>();

        try (var paths = Files.list(inputDir)) {
            paths.filter(p -> p.toString().endsWith(".txt"))
                 .forEach(p -> futures.add(executor.submit(new ScoreTask(p))));
        } catch (IOException e) {
            System.out.println("Cannot list input directory: " + e.getMessage());
        }

        long start = System.nanoTime();
        for (Future<ProcessingOutcome> future : futures) {
            try {
                ProcessingOutcome outcome = future.get();
                if (outcome.result != null) successes.add(outcome.result);
                else failures.add(outcome.file + " -> " + outcome.error);
            } catch (ExecutionException e) {
                failures.add("Unexpected task failure: " + e.getCause());
            }
        }
        long elapsed = System.nanoTime() - start;
        executor.shutdown();
        if (!executor.awaitTermination(1, TimeUnit.MINUTES)) executor.shutdownNow();

        System.out.println("Pool size: " + poolSize);
        System.out.println("Successful files: " + successes.size());
        System.out.println("Failed files: " + failures.size());
        System.out.printf("Processing time: %.3f ms%n", elapsed / 1e6);
        for (String failure : failures) System.out.println("Failure: " + failure);
        System.out.println("Summary total: " + (successes.size() + failures.size()));
    }

    public static void main(String[] args) throws InterruptedException {
        Path inputDir = Paths.get("Task07", "input");
        for (int pool : new int[]{1, 2, 4, 8}) {
            process(inputDir, pool);
            System.out.println();
        }
    }
}
