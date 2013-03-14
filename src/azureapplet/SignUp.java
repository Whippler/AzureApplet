/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azureapplet;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 *
 * @author tanelvir
 */
public class SignUp extends JFrame implements ActionListener {

    JButton Register, sign;
    JPanel panel;
    JLabel label1, label2, label3;
    final JTextField text1, text2, text3;
    boolean rightPass;

    SignUp() {

        label1 = new JLabel();
        label1.setText("Username:");
        text1 = new JTextField(15);

        label2 = new JLabel();
        label2.setText("Password:");
        text2 = new JPasswordField(15);

        label3 = new JLabel();
        label3.setText("Retype Password:");
        text3 = new JPasswordField(15);

        sign = new JButton();
        sign.setText("Cancel");
        sign.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Execute when button is pressed
                LoginScreen frame = new LoginScreen();
                frame.main(new String[0]);
                panel.removeAll();
                dispose();
            }
        });

        rightPass = false;


        Register = new JButton("Register");

        panel = new JPanel(new GridLayout(4, 1));
        panel.add(label1);
        panel.add(text1);
        panel.add(label2);
        panel.add(text2);
        panel.add(label3);
        panel.add(text3);
        panel.add(Register);
        panel.add(sign);
        add(panel, BorderLayout.CENTER);
        Register.addActionListener(this);
        setTitle("Give new username and password, please");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent ae) {
        String value1 = text1.getText();
        String value2 = text2.getText();
        String value3 = text3.getText();
        if (value2.equals(value3)) {
            rightPass = true;
        } else {
            rightPass = false;
        }
//        if (value1.equals("roseindia") && value2.equals("roseindia")) {
//            panel.removeAll();
//            GUI gui = new GUI();
//            this.dispose();
//            gui.run();
//        } else {
//            System.out.println("enter the valid username and password");
//            JOptionPane.showMessageDialog(this, "Incorrect login or password",
//                    "Error", JOptionPane.ERROR_MESSAGE);
//        }
    }
}
