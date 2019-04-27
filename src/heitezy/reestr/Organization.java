package heitezy.reestr;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static heitezy.reestr.mainUI.organizationtext;

class Organization extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField organization;

    private Organization() throws IOException {
        setContentPane(contentPane);
        setModal(true);
        setLocationRelativeTo(contentPane);
        setLocation(450,10);
        getRootPane().setDefaultButton(buttonOK);
        File file = new File("resources/org.txt");
        if (file.exists()) {
            organization.setText(new String(Files.readAllBytes(Paths.get("resources/org.txt"))));
        }

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        try (PrintStream out = new PrintStream(new FileOutputStream("resources/org.txt"))) {
            out.print(organization.getText());
            organizationtext = organization.getText();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    static void main() throws IOException {
        Organization dialog = new Organization();
        dialog.pack();
        dialog.setVisible(true);
    }

}
