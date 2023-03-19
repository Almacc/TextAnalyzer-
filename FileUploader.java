import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

public class FileUploader extends JFrame implements ActionListener {

    private final JButton uploadButton = new JButton("Upload File");
    private final JTextArea fileTextArea = new JTextArea(20, 40);
    private final JTextArea wordCountTextArea = new JTextArea(20, 40);


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

    public static void main(String[] args) {
        FileUploader fileUploader = new FileUploader();
        fileUploader.setVisible(true);
    }

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

    private Map<String, Integer> countWords(String text) {
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

    private String sortMapByValue(Map<String, Integer> wordCountMap) {
        List<Map.Entry<String, Integer>> wordCountList = new LinkedList<>(wordCountMap.entrySet());
        wordCountList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Integer> entry : wordCountList) {
            stringBuilder.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }
        return stringBuilder.toString();
    }
}
