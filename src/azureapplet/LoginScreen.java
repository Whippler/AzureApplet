/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azureapplet;

/**
 *
 * @author tanelvir
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginScreen extends JFrame implements ActionListener {

    JButton SUBMIT;
    JPanel panel;
    JLabel label1, label2;
    final JTextField text1, text2;

    LoginScreen() {
        label1 = new JLabel();
        label1.setText("Username:");
        text1 = new JTextField(15);

        label2 = new JLabel();
        label2.setText("Password:");
        text2 = new JPasswordField(15);

        JButton sign = new JButton();
        sign.setText("Sign up");


        SUBMIT = new JButton("SUBMIT");

        panel = new JPanel(new GridLayout(3, 1));
        panel.add(label1);
        panel.add(text1);
        panel.add(label2);
        panel.add(text2);
        panel.add(SUBMIT);
        panel.add(sign);
        sign.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Execute when button is pressed
                SignUp register = new SignUp();
                register.setSize(900, 200);
                register.setVisible(true);
                panel.removeAll();
                dispose();
            }
        });
        add(panel, BorderLayout.CENTER);
        SUBMIT.addActionListener(this);
        setTitle("LOGIN FORM");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void keyPressed(KeyEvent evt) {
        System.out.println("LOL");
        int key = evt.getKeyCode();

        if (key == 10) {
            SUBMIT.doClick();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        String value1 = text1.getText();
        String value2 = text2.getText();
        if (value1.equals("roseindia") && value2.equals("roseindia")) {
            panel.removeAll();
            this.dispose();
        } else {
            System.out.println("enter the valid username and password");
            JOptionPane.showMessageDialog(this, "Incorrect login or password",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String arg[]) {
        try {
            LoginScreen frame = new LoginScreen();
            frame.setSize(1000, 200);
            frame.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
