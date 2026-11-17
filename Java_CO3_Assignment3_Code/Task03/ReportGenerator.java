import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ReportGenerator {
    static void manualReport(List<Path> inputs, Path output) {
        BufferedWriter writer = null;
        try {
            writer = Files.newBufferedWriter(output);
            writer.write("DAILY SALES REPORT\n");
            for (Path input : inputs) {
                BufferedReader reader = null;
                try {
                    reader = Files.newBufferedReader(input);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.newLine();
                    }
                } catch (IOException e) {
                    System.err.println("Manual version failed at " + input + ": " + e.getMessage());
                    break;
                } finally {
                    if (reader != null) {
                        try { reader.close(); }
                        catch (IOException e) { System.err.println("Reader close failed: " + e.getMessage()); }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Could not create report: " + e.getMessage());
        } finally {
            if (writer != null) {
                try { writer.close(); }
                catch (IOException e) { System.err.println("Writer close failed: " + e.getMessage()); }
            }
        }
    }

    static void tryWithResourcesReport(List<Path> inputs, Path output) {
        try (BufferedWriter writer = Files.newBufferedWriter(output)) {
            writer.write("DAILY SALES REPORT\n");
            for (Path input : inputs) {
                try (BufferedReader reader = Files.newBufferedReader(input)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.newLine();
                    }
                } catch (IOException e) {
                    System.err.println("Try-with-resources failed at " + input + ": " + e.getMessage());
                    // Continue with remaining store files.
                }
            }
        } catch (IOException e) {
            System.err.println("Could not create report: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        Path inputDir = Paths.get("Task03", "input");
        Path outputDir = Paths.get("Task03", "output");
        Files.createDirectories(outputDir);

        List<Path> inputs = Arrays.asList(
                inputDir.resolve("store1.txt"),
                inputDir.resolve("missing-store.txt"), // deliberate missing-file test
                inputDir.resolve("store2.txt")
        );

        Path manual = outputDir.resolve("manual-report.txt");
        Path automatic = outputDir.resolve("try-with-resources-report.txt");

        System.out.println("Running manual close() version");
        manualReport(inputs, manual);
        System.out.println("Manual report path: " + manual);

        System.out.println("\nRunning try-with-resources version");
        tryWithResourcesReport(inputs, automatic);
        System.out.println("Try-with-resources report path: " + automatic);
    }
}
