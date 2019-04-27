package heitezy.reestr;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ItemEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

class mainUI {
    private JTextField textField1;
    private JPanel panel1;
    private JTextField textField2;
    private JButton browseButton2;
    private JButton browseButton1;
    private JButton personNameButton;
    private JButton signButton;
    private JButton organizationButton;
    private JCheckBox deleteSourceFilesAfterCheckBox;
    private JButton convertButton;
    private JButton exitButton;

    static String personname;
    static String organizationtext;


    mainUI() throws IOException {
        JFrame f = new JFrame("Реестр");
        f.setSize(650, 220);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setContentPane(panel1);

        File filein = new File("resources/inputpath.txt");
        File fileout = new File("resources/outputpath.txt");
        File fileperson = new File("resources/person.txt");
        File fileorg = new File("resources/org.txt");


        if (filein.exists()) {
            String inputpath = new String(Files.readAllBytes(Paths.get("resources/inputpath.txt")));
            textField1.setText(inputpath);
        }

        if (fileout.exists()) {
            String outputpath = new String(Files.readAllBytes(Paths.get("resources/outputpath.txt")));
            textField2.setText(outputpath);
        }

        if (fileperson.exists()) {
            personname = new String(Files.readAllBytes(Paths.get("resources/person.txt")));
        }

        if (fileorg.exists()) {
            organizationtext = new String(Files.readAllBytes(Paths.get("resources/org.txt")));
        }

        exitButton.addActionListener(actionEvent -> System.exit(0));

        browseButton1.addActionListener(actionEvent -> {
            JTextField textField = textField1;
            String filename = "resources/inputpath.txt";
            browseButton(textField, filename);
        });

        browseButton2.addActionListener(actionEvent -> {
            JTextField textField = textField2;
            String filename = "resources/outputpath.txt";
            browseButton(textField, filename);
        });

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
            JFileChooser FileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "gif", "png", "jpeg");
            FileChooser.setFileFilter(filter);
            int returnValue = FileChooser.showOpenDialog(signButton);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = FileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();
                InputStream inStream;
                OutputStream outStream;
                try {
                    File source = new File(filePath);
                    File dest = new File(System.getProperty("user.dir"), "resources/sign.jpg");
                    inStream = new FileInputStream(source);
                    outStream = new FileOutputStream(dest);

                    byte[] buffer = new byte[1024];

                    int lenght;
                    while ((lenght = inStream.read(buffer)) > 0) {
                        outStream.write(buffer, 0, lenght);
                    }

                    inStream.close();
                    outStream.close();
                    System.out.println("File copied.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Canceled.");
            }
        });

        convertButton.addActionListener(actionEvent -> {
            //todo Progressbar and finish window
            String inpath = textField1.getText();
            String outpath = textField2.getText();
            try {
                Convertor.convert(inpath, outpath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        deleteSourceFilesAfterCheckBox.addItemListener(itemEvent -> {
            if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                // todo
                System.out.println("Checkbox");
            }
        });
    }

    private void browseButton(JTextField textField, String filename) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int rVal = fileChooser.showOpenDialog(panel1);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            textField.setText(fileChooser.getSelectedFile().toString());
        }
        String stringpath = textField.getText();
        try (PrintStream out = new PrintStream(new FileOutputStream(filename))) {
            out.print(stringpath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
