import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * A Swing application that allows users to upload a text file and displays the word occurrences in the file.
 *
 * @author Alessandro Maccarrone
 * @version 1.0
 * @since   2023-04-08
 *
 */
public class FileUploader extends JFrame implements ActionListener {

    /**
     * The button used to upload a file.
     */
    private final JButton uploadButton = new JButton("Upload File");

    /**
     * The text area used to display the content of the uploaded file.
     */
    private final JTextArea fileTextArea = new JTextArea(20, 40);

    /**
     * The text area used to display the word count of the uploaded file.
     */
    private final JTextArea wordCountTextArea = new JTextArea(20, 40);

    /**
     * Creates a new instance of the FileUploader class, which is a GUI application
     * for counting the occurrences of words in a file. The constructor sets up the
     * JFrame window and its components, including two JPanels for displaying the
     * file content and the word count, respectively. The left panel contains a
     * JTextArea for displaying the file content and a JButton for uploading a file,
     * while the right panel contains a JTextArea for displaying the word count. The
     * constructor also sets various properties for the components, such as their
     * size, layout, border, background color, font, and margin. Finally, it adds
     * the two panels to the JFrame window and registers an ActionListener for the
     * upload button.
     */
    public FileUploader() {
        setTitle("Word Occurrences");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1,2, 10, 10));
        setResizable(false);


        JPanel leftPanel = new JPanel(new BorderLayout(10,10));
        JPanel rightPanel = new JPanel(new BorderLayout(20,10));

        leftPanel.setBorder(BorderFactory.createTitledBorder("File Content"));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Word Count"));

        uploadButton.setPreferredSize(new Dimension(150, 40));

        leftPanel.add(new JScrollPane(fileTextArea), BorderLayout.CENTER);
        rightPanel.add(new JScrollPane(wordCountTextArea), BorderLayout.CENTER);

        JPanel uploadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        uploadPanel.add(uploadButton);
        leftPanel.add(uploadPanel, BorderLayout.NORTH);


        add(leftPanel);
        add(rightPanel);


        leftPanel.setBackground(new Color(67, 156, 236));
        rightPanel.setBackground(new Color(67, 156, 236));
        uploadButton.addActionListener(this);
        fileTextArea.setMargin(new Insets(10, 10, 10, 10));
        fileTextArea.setFont(new Font("monospaced", Font.BOLD, 12));
        fileTextArea.setBackground(new Color(121, 163, 208));
        wordCountTextArea.setForeground(new Color(19, 49, 19));
        wordCountTextArea.setMargin(new Insets(10, 10, 10, 10));
        wordCountTextArea.setFont(new Font("monospaced", Font.BOLD, 14));
        wordCountTextArea.setBackground(new Color(121, 163, 208));

    }

    /**
     * The main entry point of the application.
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        FileUploader fileUploader = new FileUploader();
        fileUploader.setVisible(true);
    }

    /**
     * Called when the user clicks the upload button. Opens a file chooser dialog and reads the selected file.
     * @param e The event that triggered the action.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                fileTextArea.setText(stringBuilder.toString());
                reader.close();
                Map<String, Integer> wordCountMap = countWords(stringBuilder.toString());
                String wordCountString = sortMapByValue(wordCountMap);
                wordCountTextArea.setText(wordCountString);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Counts the occurrences of each word in the given text.
     * @param text The text to count words in.
     * @return A map of words to their occurrence counts.
     */
    Map<String, Integer> countWords(String text) {
        Map<String, Integer> wordCountMap = new HashMap<>();
        String[] words = text.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
        for (String word : words) {
            if (wordCountMap.containsKey(word)) {
                wordCountMap.put(word, wordCountMap.get(word) + 1);
            } else {
                wordCountMap.put(word, 1);
            }
        }
        return wordCountMap;
    }

    /**
     * Sorts a given Map of word counts by their values in descending order and
     * returns the sorted result as a String. The input Map should have String keys
     * and Integer values representing the frequency of each word. This method
     * first converts the Map into a List of Map.Entry objects using a LinkedList,
     * which allows the entries to be sorted by their values using the
     * comparingByValue method of the Map.Entry class and a reverse order Comparator.
     * Then it iterates over the sorted list and constructs a StringBuilder that
     * appends each word and its frequency to a new line. Finally, it returns the
     * sorted String as the result.
     * @param wordCountMap a Map of word counts to be sorted by value
     * @return a String representation of the sorted word count Map
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