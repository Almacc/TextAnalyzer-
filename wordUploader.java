import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import java.util.List;

/**

 A GUI program that counts the occurrences of each word in a MySQL database. It retrieves the word data from the database,
 calculates the word counts, and displays the results in a text area.

 @author Alessandro Maccarrone

 @version 1.0
 */
public class wordUploader extends JFrame implements ActionListener {

    // Text area for displaying word count
    private final JTextArea wordCountTextArea = new JTextArea(20, 40);

    /**

     Constructs a new WordUploader window with the description text on the left panel, and the word count text area and
     count button on the right panel.
     */
    public wordUploader() {
        // Set up the window
        setTitle("Word Occurrences");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1,2, 10, 10));
        setResizable(false);

        // Create the left and right panels
        JPanel leftPanel = new JPanel(new BorderLayout(10,10));
        JPanel rightPanel = new JPanel(new BorderLayout(20,10));

        // Add the description text to the left panel
        leftPanel.setBorder(BorderFactory.createTitledBorder("Description"));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Word Count"));

        JTextArea descriptionTextArea = new JTextArea("\nThis program counts the occurrences of each word in a" +
                " MySQL database. It retrieves the word data from the database, calculates the word counts, and " +
                "displays the results in the word count Area.\n\n\nClick the 'Count Words' button to start" +
                " the counting process.\n\n\n\n\n Thanks for using Word Occurrences.\n\n\n\n\n\n\n Alessandro Maccarrone");
        descriptionTextArea.setFont(new Font("SansSerif", Font.PLAIN, 18));
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionTextArea.setEditable(false);

        // Add the "Count Words" button and the word count text area to the right panel
        JButton countButton = new JButton("Count Words");
        countButton.setPreferredSize(new Dimension(150, 40));

        leftPanel.add(descriptionTextArea, BorderLayout.CENTER);
        rightPanel.add(new JScrollPane(wordCountTextArea), BorderLayout.CENTER);
        rightPanel.add(countButton, BorderLayout.SOUTH);

        // Add the panels to the window
        add(leftPanel);
        add(rightPanel);

        // Set the colors of the panels and text areas
        leftPanel.setBackground(new Color(67, 156, 236));
        rightPanel.setBackground(new Color(152, 195, 238));
        countButton.addActionListener(this);
        wordCountTextArea.setForeground(new Color(11, 30, 11));
        wordCountTextArea.setMargin(new Insets(10, 10, 10, 10));
        wordCountTextArea.setFont(new Font("monospaced", Font.BOLD, 14));
        wordCountTextArea.setBackground(new Color(41, 133, 227));
        descriptionTextArea.setBackground(new Color(152,195,238));
    }

    /**

     Main method to create an instance of the WordUploader class and display the window.
     @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        // Create an instance of the wordUploader class and display the window
        wordUploader fileUploader = new wordUploader();
        fileUploader.setVisible(true);
    }

    /**
     * An event listener that reads words from a database and counts their occurrences,
     * then sorts the word count map by value and displays the result in a text area.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Create a map to store the word counts
        Map<String, Integer> wordCountMap = new HashMap<>();

        // Specify the database connection parameters
        String url = "jdbc:mysql://localhost:3306/wordOccurrences";
        String user = "root";
        String password = "123456";

        // The SQL query to select words from the database
        String sql = "SELECT word FROM word";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            // Iterate over the results of the SQL query
            while (resultSet.next()) {
                // Get the word from the current row of the result set
                String word = resultSet.getString("word");
                // Split the word into individual words, remove non-alphabetic characters, and convert to lowercase
                String[] words = word.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
                for (String w : words) {
                    if (wordCountMap.containsKey(w)) {
                        wordCountMap.put(w, wordCountMap.get(w) + 1);
                    } else {
                        wordCountMap.put(w, 1);
                    }
                }
            }
            // Display the sorted word count map in a text area
            wordCountTextArea.setText(sortMapByValue(wordCountMap));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Sorts the word count map by value and returns a string representation.
     *
     * @param wordCountMap the map of word counts to be sorted
     * @return a string representation of the sorted word count map
     */
    String sortMapByValue(Map<String, Integer> wordCountMap) {
        List<Map.Entry<String, Integer>> wordCountList = new LinkedList<>(wordCountMap.entrySet());
        wordCountList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Integer> entry : wordCountList) {
            stringBuilder.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }
        return stringBuilder.toString();
    }
}

