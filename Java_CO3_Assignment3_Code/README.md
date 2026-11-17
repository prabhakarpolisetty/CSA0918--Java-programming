# Programming in Java – Assignment 3 (CO3)

## Requirements
- JDK 11 or later (Task07 uses `var` in try-with-resources; JDK 11+ is recommended).
- VS Code with Java Extension Pack.

## Folder structure
- `Task01/ATMWithdrawal.java` – ATM validation and exception handling.
- `Task02/StorageDemo.java` – custom checked exceptions, propagation and exception chaining.
- `Task03/ReportGenerator.java` – manual resource closing and try-with-resources.
- `Task04/ScannerStations.java` – Thread subclass, Runnable, start, sleep, join and isAlive.
- `Task05/SeatBooking.java` – synchronized check-and-book operation.
- `Task05/SeatBookingBenchmark.java` – method/block synchronization timing comparison.
- `Task06/TokenCounter.java` – bounded producer-consumer buffer using wait/notifyAll.
- `Task06/DeadlockAndPrevention.java` – safe wallet transfer using consistent lock ordering.
- `Task07/ResultProcessor.java` – ExecutorService, Callable, Future and concurrent result queues.

## Compile all Java files
Run from the root folder:
```powershell
New-Item -ItemType Directory -Force out
javac -d out (Get-ChildItem -Recurse -Filter *.java | ForEach-Object FullName)
```

## Run programs
```powershell
java -cp out ATMWithdrawal
java -cp out StorageDemo
java -cp out ReportGenerator
java -cp out ScannerStations
java -cp out SeatBooking
java -cp out SeatBookingBenchmark
java -cp out TokenCounter
java -cp out DeadlockAndPrevention
java -cp out ResultProcessor
```

## Notes
- `ATMWithdrawal` asks for an account number and amount. Try valid amount, letters, negative amount, zero and an amount above balance.
- `StorageDemo` includes the assignment's completed TODO 1 and expected exception chaining.
- `ReportGenerator` reads files in `Task03/input` and writes reports in `Task03/output`. A missing file is deliberately included to demonstrate failure handling.
- `SeatBooking` should print `Seats sold: 10`.
- `TokenCounter` should print `Sum of tokens collected: 210 (expected 210)`.
- `ResultProcessor` runs with thread pools of 1, 2, 4 and 8. It handles corrupt score files without stopping the full run.

## Output evidence
After running each program in VS Code, save terminal screenshots in the matching task folder, for example `Task01/output/`. The timing values are machine-dependent: record your own runs and do not present estimated values as measured evidence.

## GitHub
Create the required repository `CSA09-<RegisterNumber>-Assignment3`. Commit each task progressively and push the completed folder.
