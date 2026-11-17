# Programming in Java – Assignment 4 (CO4)

**Topic:** Event-Driven GUI Design  
**Implementation:** Java AWT and Swing  
**Recommended JDK:** 17 or later

## Folder structure
- `Task01/FeeWindow.java` – Fee payment UI separated from `FeeService`; distinct button listeners, validation, receipt and close handling.
- `Task02/AppointmentDesk.java` – Department-dependent doctor list, appointment validation, confirmation and close prompt.
- `Task03/LibrarySearchScreen.java` – Nested BorderLayout and FlowLayout panels; resizable results area.
- `Task04/DrawPad.java` – Completed mouse listener and persistent repainting from stored points.
- `Task05/Converter.java` – Completed lambda converter TODOs; button and Enter share one listener.
- `Task05/ConverterStyles.java` – Frame listener, named inner class, anonymous inner class and lambda examples.
- `Task06/CampusEventApp.java` – Swing application with layered UI/service/DAO, JDBC, JTable, filter, CSV export and SwingWorker.
- `Task06/schema.sql` – MySQL database and table creation script.

## Compile all Java files
Run in PowerShell from this folder:
```powershell
New-Item -ItemType Directory -Force out
javac -d out (Get-ChildItem -Recurse -Filter *.java | ForEach-Object FullName)
```
All source files compile with the JDK without requiring the MySQL connector at compile time.

## Run each task
```powershell
java -cp out FeeWindow
java -cp out AppointmentDesk
java -cp out LibrarySearchScreen
java -cp out DrawPad
java -cp out Converter
java -cp out ConverterStyles
java -cp "out;mysql-connector-j-9.5.0.jar" CampusEventApp
```
The last command assumes the MySQL Connector/J JAR is downloaded and placed in this folder with that exact name. Use the actual JAR filename if different.

## Task 6 MySQL setup
1. Start MySQL.
2. Run `Task06/schema.sql` in MySQL Workbench.
3. Download MySQL Connector/J and place the JAR in the project root.
4. Set connection environment variables in PowerShell if your MySQL credentials differ:
```powershell
$env:CO4_DB_URL="jdbc:mysql://localhost:3306/campus_events?useSSL=false&serverTimezone=UTC"
$env:CO4_DB_USER="root"
$env:CO4_DB_PASSWORD="YOUR_MYSQL_PASSWORD"
```
5. Run the Task 6 command above.

Do not commit database passwords to GitHub. The application uses prepared statements and a database UNIQUE constraint to protect against duplicate student/event registrations.

## Evidence to capture
- Task 1: invalid input, successful payment and receipt dialog.
- Task 2: doctor list changes by department, valid/invalid booking and close confirmation.
- Task 3: screenshots at three window sizes; verify results region grows while top/bottom rows retain preferred sizes.
- Task 4: click count and dots before and after minimizing/restoring.
- Task 5: valid and invalid conversion for lambda version; test all four styles.
- Task 6: successful, invalid and duplicate registration; event filtering and CSV export. Capture database output and GUI screenshots.

Screenshots must be captured after running on your computer. Do not present generated or assumed output as actual execution evidence.

## GitHub
Keep this folder named `Java_CO4_Assignment4_Code`. Copy it into your existing local repository if you want CO1–CO4 in one repository, then stage, commit and push the folder.
