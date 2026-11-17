# Java Assignment 2 – CO2

## Requirements
- JDK 8 or later
- VS Code or any Java IDE

## Compile and run
Open a terminal in the assignment folder. Compile and run each task separately:

```bash
javac Task01/StringBenchmark.java
java -cp Task01 StringBenchmark

javac Task02/ListBenchmark.java
java -cp Task02 ListBenchmark

javac Task03/Attendance.java
java -cp Task03 Attendance

javac Task04/PharmacyInventory.java
java -cp Task04 PharmacyInventory

javac Task05/LeaderboardDemo.java
java -cp Task05 LeaderboardDemo

javac Task06/Triage.java
java -cp Task06 Triage

javac Task07/UpNextRedesign.java
java -cp Task07 UpNextRedesign
```

On Windows PowerShell, the same commands work when run from this folder.

## Task summary
1. StringBenchmark – compares String, StringBuilder and StringBuffer over three workloads.
2. ListBenchmark – compares ArrayList and LinkedList operations and demonstrates ArrayDeque for an Up Next queue.
3. Attendance – completes hashCode() consistently with equals().
4. PharmacyInventory – compares HashMap, TreeMap and LinkedHashMap for lookup and iteration.
5. LeaderboardDemo – completes a generic leaderboard and multi-field Comparator.
6. Triage – completes a stable severity-based PriorityQueue.
7. UpNextRedesign – diagnoses front-shifting overhead in ArrayList and validates an ArrayDeque redesign.

## Important experimental note
Execution times depend on CPU, JVM warm-up, memory and background processes. Run each benchmark several times and record your own machine's results in the report. Do not copy sample timings as measured evidence. For stronger benchmarking, use multiple runs and a framework such as JMH.

## Submission checklist
- Add your name and register number to this README.
- Run all seven programs on your computer.
- Capture terminal output screenshots.
- Record repeated measurements and calculate averages.
- Commit each task separately to your GitHub repository.
