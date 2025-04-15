import javax.swing.*;
import java.awt.*;
//import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataStreams extends JFrame {

    private JTextArea originalArea;
    private JTextArea filteredArea;
    private JTextField searchField;
    private JLabel fileLabel;
    private File currentFile;

    public DataStreams() {
        super("Java Data Streams - Lab 09");
        setupGUI();
    }

    private void setupGUI() {
        originalArea = new JTextArea(25, 40);
        filteredArea = new JTextArea(25, 40);
        searchField = new JTextField(20);
        fileLabel = new JLabel("No file loaded");

        JButton loadButton = new JButton("Load File");
        JButton searchButton = new JButton("Search");
        JButton quitButton = new JButton("Quit");

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(loadButton);
        topPanel.add(searchButton);
        topPanel.add(quitButton);

        JScrollPane originalScroll = new JScrollPane(originalArea);
        JScrollPane filteredScroll = new JScrollPane(filteredArea);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        centerPanel.add(originalScroll);
        centerPanel.add(filteredScroll);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(fileLabel, BorderLayout.SOUTH);

        originalArea.setEditable(false);
        filteredArea.setEditable(false);

        loadButton.addActionListener(e -> loadFile());
        searchButton.addActionListener(e -> searchFile());
        quitButton.addActionListener(e -> System.exit(0));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            currentFile = chooser.getSelectedFile();
            fileLabel.setText("Loaded: " + currentFile.getName());
            try (Stream<String> lines = Files.lines(currentFile.toPath())) {
                List<String> content = lines.collect(Collectors.toList());
                originalArea.setText(String.join("\n", content));
                filteredArea.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading file.");
            }
        }
    }

    private void searchFile() {
        if (currentFile == null) {
            JOptionPane.showMessageDialog(this, "Please load a file first.");
            return;
        }

        String search = searchField.getText().toLowerCase();
        if (search.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.");
            return;
        }

        try (Stream<String> lines = Files.lines(currentFile.toPath())) {
            List<String> result = lines
                    .filter(line -> line.toLowerCase().contains(search))
                    .collect(Collectors.toList());
            filteredArea.setText(String.join("\n", result));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error filtering file.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DataStreams::new);
    }
}


