package heitezy.reestr;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static heitezy.reestr.mainUI.personname;

class Person extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldPerson;

    private Person() throws IOException {
        setContentPane(contentPane);
        setModal(true);
        setLocationRelativeTo(contentPane);
        setLocation(10,10);
        getRootPane().setDefaultButton(buttonOK);
        File file = new File("person.txt");
        if (file.exists()) {
            textFieldPerson.setText(new String(Files.readAllBytes(Paths.get("person.txt"))));
        }

        buttonOK.addActionListener(e -> {
            try {
                onOK();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });

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

    private void onOK() throws FileNotFoundException {
        try (PrintStream out = new PrintStream(new FileOutputStream("person.txt"))) {
            out.print(textFieldPerson.getText());
            personname = textFieldPerson.getText();
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    static void main() throws IOException {
        Person dialog = new Person();
        dialog.pack();
        dialog.setVisible(true);
    }

}
