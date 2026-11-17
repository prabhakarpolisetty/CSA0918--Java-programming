import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class LibrarySearchScreen extends JFrame {
    private final JTextField query = new JTextField(22);
    private final JComboBox<String> category = new JComboBox<>(new String[]{"All", "Java", "AI", "Networks"});
    private final DefaultListModel<String> resultsModel = new DefaultListModel<>();
    private final JList<String> results = new JList<>(resultsModel);
    private final JLabel status = new JLabel("Enter a title or keyword.");
    private final List<String> allBooks = Arrays.asList(
            "Java: The Complete Reference", "Effective Java", "Artificial Intelligence Basics",
            "Computer Networks", "Learning Swing", "Data Structures in Java");

    public LibrarySearchScreen() {
        super("Campus Library Search");
        setLayout(new BorderLayout(8, 8));

        JLabel title = new JLabel("Campus Library Search", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        add(title, BorderLayout.NORTH);

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchRow.add(new JLabel("Search:"));
        searchRow.add(query);
        searchRow.add(category);
        JButton search = new JButton("Search");
        searchRow.add(search);
        JPanel top = new JPanel(new BorderLayout());
        top.add(title, BorderLayout.NORTH);
        top.add(searchRow, BorderLayout.SOUTH);
        remove(title);
        add(top, BorderLayout.NORTH);

        results.setVisibleRowCount(10);
        add(new JScrollPane(results), BorderLayout.CENTER);

        JPanel bottomButtons = new JPanel(new FlowLayout());
        JButton prev = new JButton("Prev");
        JButton next = new JButton("Next");
        JButton reserve = new JButton("Reserve");
        bottomButtons.add(prev); bottomButtons.add(next); bottomButtons.add(reserve);
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(bottomButtons, BorderLayout.CENTER);
        bottom.add(status, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);

        search.addActionListener(e -> searchBooks());
        query.addActionListener(e -> searchBooks());
        prev.addActionListener(e -> status.setText("Previous page selected."));
        next.addActionListener(e -> status.setText("Next page selected."));
        reserve.addActionListener(e -> {
            String selected = results.getSelectedValue();
            status.setText(selected == null ? "Select a book first." : "Reservation requested: " + selected);
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(650, 430);
        setLocationRelativeTo(null);
        searchBooks();
    }

    private void searchBooks() {
        resultsModel.clear();
        String q = query.getText().trim().toLowerCase();
        String cat = (String) category.getSelectedItem();
        for (String book : allBooks) {
            boolean matchesText = q.isEmpty() || book.toLowerCase().contains(q);
            boolean matchesCategory = "All".equals(cat)
                    || book.toLowerCase().contains(cat.toLowerCase());
            if (matchesText && matchesCategory) resultsModel.addElement(book);
        }
        status.setText(resultsModel.size() + " result(s) found.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibrarySearchScreen().setVisible(true));
    }
}
