import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConverterStyles {
    static double convertValue(String text, String currency) {
        double value = Double.parseDouble(text.trim());
        if (!Double.isFinite(value) || value < 0) throw new NumberFormatException();
        return value * (currency.equals("USD") ? 0.012 : 0.011);
    }

    static class BaseFrame extends JFrame {
        final JTextField amount = new JTextField(10);
        final JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});
        final JLabel result = new JLabel(" ");
        BaseFrame(String title) {
            super(title);
            setLayout(new FlowLayout());
            JButton button = new JButton("Convert");
            add(new JLabel("INR")); add(amount); add(target); add(button); add(result);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override public void windowClosing(WindowEvent e) { dispose(); }
            });
            pack();
        }
        void showResult() {
            try {
                String out = String.format("%.2f %s", convertValue(amount.getText(), (String) target.getSelectedItem()),
                        target.getSelectedItem());
                result.setText(out);
                JOptionPane.showMessageDialog(this, out);
            } catch (NumberFormatException e) {
                result.setText("Invalid amount");
                JOptionPane.showMessageDialog(this, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // (a) Frame itself handles ActionListener.
    static class FrameListenerVersion extends BaseFrame implements ActionListener {
        FrameListenerVersion() {
            super("Style A - Frame implements ActionListener");
            JButton b = (JButton) getContentPane().getComponent(3);
            b.addActionListener(this);
            amount.addActionListener(this);
        }
        @Override public void actionPerformed(ActionEvent e) { showResult(); }
    }

    // (b) Named inner class.
    static class NamedInnerVersion extends BaseFrame {
        NamedInnerVersion() {
            super("Style B - Named inner class");
            ActionListener handler = new ConvertHandler();
            ((JButton) getContentPane().getComponent(3)).addActionListener(handler);
            amount.addActionListener(handler);
        }
        private class ConvertHandler implements ActionListener {
            @Override public void actionPerformed(ActionEvent e) { showResult(); }
        }
    }

    // (c) Anonymous inner class.
    static class AnonymousVersion extends BaseFrame {
        AnonymousVersion() {
            super("Style C - Anonymous inner class");
            ActionListener handler = new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) { showResult(); }
            };
            ((JButton) getContentPane().getComponent(3)).addActionListener(handler);
            amount.addActionListener(handler);
        }
    }

    // (d) Lambda expression.
    static class LambdaVersion extends BaseFrame {
        LambdaVersion() {
            super("Style D - Lambda");
            ActionListener handler = e -> showResult();
            ((JButton) getContentPane().getComponent(3)).addActionListener(handler);
            amount.addActionListener(handler);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame chooser = new JFrame("Choose Event Handling Style");
            chooser.setLayout(new GridLayout(4, 1, 5, 5));
            chooser.add(button("Frame listener", () -> new FrameListenerVersion().setVisible(true)));
            chooser.add(button("Named inner class", () -> new NamedInnerVersion().setVisible(true)));
            chooser.add(button("Anonymous inner class", () -> new AnonymousVersion().setVisible(true)));
            chooser.add(button("Lambda", () -> new LambdaVersion().setVisible(true)));
            chooser.pack(); chooser.setLocationRelativeTo(null); chooser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            chooser.setVisible(true);
        });
    }

    private static JButton button(String label, Runnable action) {
        JButton b = new JButton(label);
        b.addActionListener(e -> action.run());
        return b;
    }
}
