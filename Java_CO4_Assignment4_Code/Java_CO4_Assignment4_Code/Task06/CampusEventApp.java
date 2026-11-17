import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CampusEventApp extends JFrame {
    private final JTextField studentId = new JTextField(12);
    private final JTextField studentName = new JTextField(14);
    private final JComboBox<String> event = new JComboBox<>(new String[]{
            "Coding Contest", "Cultural Night", "Robotics Workshop"});
    private final JComboBox<String> filter = new JComboBox<>(new String[]{
            "All", "Coding Contest", "Cultural Night", "Robotics Workshop"});
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Student", "Event", "Registered At"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JLabel status = new JLabel("Ready.");
    private final RegistrationService service = new RegistrationService();

    public CampusEventApp() {
        super("Campus Event Registration");
        setLayout(new BorderLayout(8, 8));
        setJMenuBar(createMenu());

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.setBorder(BorderFactory.createTitledBorder("Register student"));
        form.add(new JLabel("Student ID:")); form.add(studentId);
        form.add(new JLabel("Name:")); form.add(studentName);
        form.add(new JLabel("Event:")); form.add(event);
        JButton register = new JButton("Register");
        form.add(register);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter event:")); filterPanel.add(filter);
        JButton applyFilter = new JButton("Apply Filter");
        JButton export = new JButton("Export CSV");
        filterPanel.add(applyFilter); filterPanel.add(export);

        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.NORTH);
        north.add(filterPanel, BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        register.addActionListener(e -> registerStudent());
        applyFilter.addActionListener(e -> loadPage(0));
        filter.addActionListener(e -> { /* Apply Filter button controls reload */ });
        export.addActionListener(e -> exportCsv());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 520);
        setLocationRelativeTo(null);
        loadPage(0);
    }

    private JMenuBar createMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem refresh = new JMenuItem("Refresh");
        JMenuItem exit = new JMenuItem("Exit");
        refresh.addActionListener(e -> loadPage(0));
        exit.addActionListener(e -> dispose());
        file.add(refresh); file.addSeparator(); file.add(exit);
        bar.add(file);
        return bar;
    }

    private void registerStudent() {
        String id = studentId.getText().trim();
        String name = studentName.getText().trim();
        String eventName = (String) event.getSelectedItem();
        if (id.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student ID and name are required.");
            return;
        }
        setBusy(true, "Registering...");
        new SwingWorker<String, Void>() {
            @Override protected String doInBackground() throws Exception {
                service.register(id, name, eventName);
                return "Registration successful.";
            }
            @Override protected void done() {
                try {
                    status.setText(get());
                    studentId.setText(""); studentName.setText("");
                    loadPage(0);
                } catch (Exception ex) {
                    Throwable cause = ex.getCause() == null ? ex : ex.getCause();
                    JOptionPane.showMessageDialog(CampusEventApp.this, cause.getMessage(),
                            "Registration failed", JOptionPane.ERROR_MESSAGE);
                    status.setText("Registration failed.");
                } finally { setBusy(false, "Ready."); }
            }
        }.execute();
    }

    private void loadPage(int offset) {
        String selected = (String) filter.getSelectedItem();
        setBusy(true, "Loading registrations...");
        new SwingWorker<List<Registration>, Void>() {
            @Override protected List<Registration> doInBackground() throws Exception {
                return service.findPage(selected, 100, offset);
            }
            @Override protected void done() {
                try {
                    model.setRowCount(0);
                    for (Registration r : get())
                        model.addRow(new Object[]{r.studentId, r.studentName, r.eventName, r.registeredAt});
                    status.setText("Loaded " + model.getRowCount() + " registration(s).");
                } catch (Exception ex) {
                    status.setText("Database unavailable: " + ex.getMessage());
                } finally { setBusy(false, status.getText()); }
            }
        }.execute();
    }

    private void exportCsv() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            out.println("Student ID,Student Name,Event,Registered At");
            for (int i = 0; i < model.getRowCount(); i++) {
                out.printf("\"%s\",\"%s\",\"%s\",\"%s\"%n",
                        model.getValueAt(i,0), model.getValueAt(i,1),
                        model.getValueAt(i,2), model.getValueAt(i,3));
            }
            status.setText("Exported current page to " + file.getName());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Export error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setBusy(boolean busy, String message) {
        status.setText(message);
        setCursor(busy ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CampusEventApp().setVisible(true));
    }
}

class Registration {
    final String studentId, studentName, eventName, registeredAt;
    Registration(String id, String name, String event, String at) {
        studentId=id; studentName=name; eventName=event; registeredAt=at;
    }
}

class RegistrationService {
    private final RegistrationDAO dao = new RegistrationDAO();

    void register(String id, String name, String event) throws SQLException {
        if (id == null || id.isBlank() || name == null || name.isBlank())
            throw new IllegalArgumentException("Student ID and name are required.");
        if (event == null || event.isBlank()) throw new IllegalArgumentException("Select an event.");
        dao.insert(id.trim(), name.trim(), event);
    }

    List<Registration> findPage(String event, int limit, int offset) throws SQLException {
        return dao.findPage(event, limit, offset);
    }
}

class RegistrationDAO {
    private Connection connect() throws SQLException {
        String url = System.getenv().getOrDefault("CO4_DB_URL",
                "jdbc:mysql://localhost:3306/campus_events?useSSL=false&serverTimezone=UTC");
        String user = System.getenv().getOrDefault("CO4_DB_USER", "root");
        String password = System.getenv().getOrDefault("CO4_DB_PASSWORD", "");
        return DriverManager.getConnection(url, user, password);
    }

    void insert(String id, String name, String event) throws SQLException {
        String sql = "INSERT INTO registrations(student_id, student_name, event_name) VALUES (?, ?, ?)";
        try (Connection c = connect();
             PreparedStatement check = c.prepareStatement(
                     "SELECT 1 FROM registrations WHERE student_id = ? AND event_name = ?");
             PreparedStatement insert = c.prepareStatement(sql)) {
            check.setString(1, id); check.setString(2, event);
            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) throw new SQLException("This student is already registered for this event.");
            }
            insert.setString(1, id); insert.setString(2, name); insert.setString(3, event);
            insert.executeUpdate();
        }
    }

    List<Registration> findPage(String event, int limit, int offset) throws SQLException {
        List<Registration> rows = new ArrayList<>();
        boolean all = event == null || event.equals("All");
        String sql = all
                ? "SELECT student_id, student_name, event_name, registered_at FROM registrations ORDER BY id DESC LIMIT ? OFFSET ?"
                : "SELECT student_id, student_name, event_name, registered_at FROM registrations WHERE event_name=? ORDER BY id DESC LIMIT ? OFFSET ?";
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            int index = 1;
            if (!all) ps.setString(index++, event);
            ps.setInt(index++, limit); ps.setInt(index, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) rows.add(new Registration(
                        rs.getString("student_id"), rs.getString("student_name"),
                        rs.getString("event_name"), rs.getTimestamp("registered_at").toString()));
            }
        }
        return rows;
    }
}
