package heitezy.reestr;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ItemEvent;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

class MainWindow {
    //todo fix path isnt saved to txt after manual input
    private JTextField inputPath;
    private JPanel panel;
    //todo fix path isnt saved to txt after manual input
    private JTextField outputPath;
    private JButton browseOutputhPath;
    private JButton browseInputPath;
    private JButton personNameButton;
    private JButton signButton;
    private JButton organizationButton;
    private JCheckBox deleteSourceFilesAfterCheckBox;
    private JButton convertButton;
    private JButton exitButton;

    static String personText;
    static String organizationText;


    MainWindow(boolean silence) throws IOException {
        JFrame frame = new JFrame("Реєстр");
        frame.setSize(650, 220);
        if (silence) {
            frame.setVisible(false);
        } else {
            frame.setVisible(true);
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);

        //todo Settings in single file
        //todo default paths
        //fixme catch path /resources not found exception
        //fixme dont save path after manual input
        File fileIn = new File("resources/inputpath.txt");
        File fileOut = new File("resources/outputpath.txt");
        File filePerson = new File("resources/person.txt");
        File fileOrg = new File("resources/org.txt");
        File fileCheckbox = new File("resources/checkbox");

        if (fileCheckbox.exists()) {
            deleteSourceFilesAfterCheckBox.setSelected(true);
        }

        if (fileIn.exists()) {
            inputPath.setText(new String(Files.readAllBytes(Paths.get("resources/inputpath.txt"))));
        }

        if (fileOut.exists()) {
            outputPath.setText(new String(Files.readAllBytes(Paths.get("resources/outputpath.txt"))));
        }

        if (filePerson.exists()) {
            personText = new String(Files.readAllBytes(Paths.get("resources/person.txt")));
        }

        if (fileOrg.exists()) {
            organizationText = new String(Files.readAllBytes(Paths.get("resources/org.txt")));
        }

        exitButton.addActionListener(actionEvent -> System.exit(0));

        browseInputPath.addActionListener(actionEvent -> browseButton(inputPath, "resources/inputpath.txt"));

        browseOutputhPath.addActionListener(actionEvent -> browseButton(outputPath, "resources/outputpath.txt"));

        personNameButton.addActionListener(actionEvent -> {
            try {
                Person.main();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        organizationButton.addActionListener(actionEvent -> {
            try {
                Organization.main();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        signButton.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Файл малюнку", "jpg", "gif", "png", "jpeg");
            fileChooser.setFileFilter(filter);
            if (fileChooser.showOpenDialog(signButton) == JFileChooser.APPROVE_OPTION) {
                try {
                    InputStream inStream = new FileInputStream(new File(fileChooser.getSelectedFile().getAbsolutePath()));
                    OutputStream outStream = new FileOutputStream(new File(System.getProperty("user.dir"), "resources/sign.jpg"));

                    byte[] buffer = new byte[1024];
                    int lenght;
                    while ((lenght = inStream.read(buffer)) > 0) {
                        outStream.write(buffer, 0, lenght);
                    }

                    inStream.close();
                    outStream.close();
                    System.out.println("Файл скопійовано.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Копіювання перервано.");
            }
        });

        convertButton.addActionListener(actionEvent -> {
            //todo Progressbar
            File scanFile = new File("resources/sign.jpg");

            if (!silence) {
                if (!scanFile.exists()) {
                    Object[] options = {"Обрати",
                            "Відміна"};
                    int result = JOptionPane.showOptionDialog(panel,
                            "Потрібно вказати скан штампа. Хочете обрати?",
                            "Скан штампа",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);
                    //todo more logic to this dialog
                    if (result == JOptionPane.YES_OPTION) {
                        signButton.doClick();
                    }
                }
            }
            try {
                Convertor.convert(inputPath.getText(), outputPath.getText());
                if (fileCheckbox.exists()) {
                    Files.walkFileTree(Paths.get(inputPath.getText()), new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                            //todo filter maybe?
                            Files.delete(file);
                            return FileVisitResult.CONTINUE;
                        }
                    });
                }
                if (!silence) {
                    JOptionPane.showMessageDialog(panel, "Конвертація завершена");
                    //todo more dialogs for different cases
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        deleteSourceFilesAfterCheckBox.addItemListener(itemEvent -> {
            //fixme setting isnt changed after first round of using
            if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                try {
                    PrintStream out = new PrintStream(new FileOutputStream("resources/checkbox"));
                    out.print("checkbox");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
                //noinspection ResultOfMethodCallIgnored
                fileCheckbox.delete();
            }
        });

        if (silence) {
            convertButton.doClick();
            System.exit(0);
        }
    }

    private void browseButton(JTextField textField, String filename) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(panel);
        if (result == JFileChooser.APPROVE_OPTION) {
            textField.setText(fileChooser.getSelectedFile().toString());
        }
        try {
            PrintStream out = new PrintStream(new FileOutputStream(filename));
            out.print(textField.getText());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
