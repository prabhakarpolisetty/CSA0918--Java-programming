import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.UUID;

class FeeService {
    String payFee(String regNo, String rawAmount) {
        if (regNo == null || regNo.trim().isEmpty())
            throw new IllegalArgumentException("Registration number is required.");
        final double amount;
        try { amount = Double.parseDouble(rawAmount.trim()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Enter a valid numeric amount."); }
        if (!Double.isFinite(amount) || amount <= 0)
            throw new IllegalArgumentException("Amount must be greater than zero.");
        // Demo payment. Replace this method with prepared-statement JDBC and a payment gateway.
        return "Payment successful. Receipt: " + UUID.randomUUID().toString().substring(0, 8)
                + " | Reg No: " + regNo + " | Amount: Rs. " + String.format("%.2f", amount);
    }
}

public class FeeWindow extends JFrame {
    private final JTextField regNo = new JTextField(14);
    private final JTextField amount = new JTextField(14);
    private final JLabel status = new JLabel("Enter details and select Pay.");
    private final FeeService service = new FeeService();
    private String lastReceipt = "";

    public FeeWindow() {
        super("Student Fee Payment");
        setLayout(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.add(new JLabel("Registration number:")); form.add(regNo);
        form.add(new JLabel("Amount (Rs.):")); form.add(amount);
        JPanel actions = new JPanel(new FlowLayout());
        JButton pay = new JButton("Pay");
        JButton clear = new JButton("Clear");
        JButton receipt = new JButton("Receipt");
        actions.add(pay); actions.add(clear); actions.add(receipt);
        add(form, BorderLayout.NORTH);
        add(actions, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        pay.addActionListener(e -> {
            try {
                lastReceipt = service.payFee(regNo.getText(), amount.getText());
                status.setText("Payment completed.");
                JOptionPane.showMessageDialog(this, lastReceipt, "Payment receipt", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException ex) {
                status.setText(ex.getMessage());
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid payment", JOptionPane.ERROR_MESSAGE);
            }
        });
        clear.addActionListener(e -> { regNo.setText(""); amount.setText(""); status.setText("Form cleared."); });
        receipt.addActionListener(e -> JOptionPane.showMessageDialog(this,
                lastReceipt.isEmpty() ? "No successful payment yet." : lastReceipt, "Receipt",
                JOptionPane.INFORMATION_MESSAGE));

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FeeWindow().setVisible(true));
    }
}
