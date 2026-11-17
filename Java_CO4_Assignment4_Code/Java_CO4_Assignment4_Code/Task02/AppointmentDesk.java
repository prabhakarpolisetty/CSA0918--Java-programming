import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AppointmentDesk extends JFrame {
    private final JTextField patientId = new JTextField(12);
    private final JComboBox<String> department = new JComboBox<>(
            new String[]{"Cardiology", "Dermatology", "Neurology"});
    private final JComboBox<String> doctor = new JComboBox<>();
    private final JComboBox<String> slot = new JComboBox<>(
            new String[]{"09:00", "10:00", "11:00", "14:00", "15:00"});
    private final JLabel status = new JLabel("Ready for appointment booking.");
    private boolean bookingInProgress = false;
    private final Map<String, java.util.List<String>> doctorsByDepartment = new HashMap<>();

    public AppointmentDesk() {
        super("Hospital Outpatient Appointment Desk");
        doctorsByDepartment.put("Cardiology", Arrays.asList("Dr. Rao", "Dr. Mehta"));
        doctorsByDepartment.put("Dermatology", Arrays.asList("Dr. Iyer", "Dr. Shah"));
        doctorsByDepartment.put("Neurology", Arrays.asList("Dr. Kumar", "Dr. Das"));

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        form.add(new JLabel("Patient ID:")); form.add(patientId);
        form.add(new JLabel("Department:")); form.add(department);
        form.add(new JLabel("Doctor:")); form.add(doctor);
        form.add(new JLabel("Time slot:")); form.add(slot);
        JButton book = new JButton("Book Appointment");
        form.add(new JLabel("")); form.add(book);
        add(form, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        department.addActionListener(e -> refreshDoctors());
        book.addActionListener(e -> bookAppointment());
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                if (bookingInProgress) {
                    int answer = JOptionPane.showConfirmDialog(AppointmentDesk.this,
                            "A booking is in progress. Close anyway?", "Confirm close",
                            JOptionPane.YES_NO_OPTION);
                    if (answer == JOptionPane.YES_OPTION) dispose();
                } else dispose();
            }
        });
        refreshDoctors();
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private void refreshDoctors() {
        String selectedDepartment = (String) department.getSelectedItem();
        doctor.removeAllItems();
        for (String name : doctorsByDepartment.getOrDefault(selectedDepartment, Collections.emptyList()))
            doctor.addItem(name);
    }

    private void bookAppointment() {
        String id = patientId.getText().trim();
        if (id.isEmpty()) {
            status.setText("Error: Patient ID is required.");
            JOptionPane.showMessageDialog(this, "Enter patient ID.");
            return;
        }
        if (doctor.getSelectedItem() == null || slot.getSelectedItem() == null) {
            status.setText("Error: Select a doctor and slot.");
            return;
        }
        bookingInProgress = true;
        int answer = JOptionPane.showConfirmDialog(this,
                "Confirm appointment for " + id + " with " + doctor.getSelectedItem()
                        + " at " + slot.getSelectedItem() + "?",
                "Confirm booking", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            status.setText("Appointment booked for patient " + id + ".");
        } else {
            status.setText("Booking cancelled.");
        }
        bookingInProgress = false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppointmentDesk().setVisible(true));
    }
}
