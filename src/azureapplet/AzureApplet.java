package azureapplet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AzureApplet extends JApplet {

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

        JPanel valikko = new JPanel();
        
        //OTSIKKO
        JLabel Otsikko = new JLabel(
                "DRAW PAD");
        valikko.add(Otsikko);
        Otsikko.setHorizontalAlignment(JLabel.LEFT);
        Otsikko.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        //valikko.add(Otsikko);
        
        //VÄRIT
        
        valikko.setBounds(100, 1, 300, 25);
        valikko.setBackground(Color.GRAY);
        getContentPane().add(valikko);


        //PIIRTO ALUE
        PadDraw draw = new PadDraw();
        draw.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));

        getContentPane().add(draw);

        //OTSIKKO
    }
}
