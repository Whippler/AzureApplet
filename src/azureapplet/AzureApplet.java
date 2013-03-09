package azureapplet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;

public class AzureApplet extends JApplet {
    private JPanel panel;
    private PadDraw draw;
    @Override
    public void init() {
        try {
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    createGUI();
                }
            });
        } catch (InterruptedException | InvocationTargetException e) {
            System.err.println("createGUI didn't successfully complete");
        }
    }

    private void createGUI() {

        panel = new JPanel();
        panel.setPreferredSize(new Dimension(32, 68));
        
        //OTSIKKO
        JLabel Otsikko = new JLabel(
                "DRAW PAD");
       // valikko.add(Otsikko);
        Otsikko.setHorizontalAlignment(JLabel.LEFT);
        //Otsikko.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        //valikko.add(Otsikko);
        
        //VÄRIT
        
//        valikko.setBounds(100, 1, 300, 25);
//        valikko.setBackground(Color.GRAY);
//        getContentPane().add(valikko);
        makeColorButton(Color.BLUE);
        makeColorButton(Color.MAGENTA);
        makeColorButton(Color.RED);
        makeColorButton(Color.GREEN);
        makeColorButton(Color.BLACK);
        makeColorButton(Color.WHITE);
        makeColorButton(Color.YELLOW);

        //PIIRTO ALUE
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.SOUTH);
        draw = new PadDraw();
        //draw.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));

        getContentPane().add(draw);

        //OTSIKKO
    }
    public void makeColorButton(final Color color) {
        JButton tempButton = new JButton();
        tempButton.setBackground(color);
        tempButton.setPreferredSize(new Dimension(16, 16));
        panel.add(tempButton);
        tempButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                draw.changeColor(color);
            }
        });
    }

}
