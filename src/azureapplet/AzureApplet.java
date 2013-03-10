package azureapplet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AzureApplet extends JApplet {

    private JPanel panel;
    private PadDraw draw;
    JColorChooser chooser;

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
        //panel.setPreferredSize(new Dimension(32, 68));

        //OTSIKKO
        JLabel Otsikko = new JLabel(
                "DRAW PAD");
        // valikko.add(Otsikko);
        Otsikko.setHorizontalAlignment(JLabel.LEFT);
        //Otsikko.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        //valikko.add(Otsikko);

        //VÄRIT
        chooser = new JColorChooser();
        ChangeListener changeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                draw.changeColor(chooser.getColor());
            }
        };
        chooser.getSelectionModel().addChangeListener(changeListener);
        JPanel previewPanel = new JPanel();
        previewPanel.setVisible(false);
        chooser.setPreviewPanel(previewPanel);
        
        
        panel.add(chooser);
//        valikko.setBounds(100, 1, 300, 25);
//        valikko.setBackground(Color.GRAY);
//        getContentPane().add(valikko);
//        makeColorButton(Color.BLUE);
//        makeColorButton(Color.MAGENTA);
//        makeColorButton(Color.RED);
//        makeColorButton(Color.GREEN);
//        makeColorButton(Color.BLACK);
//        makeColorButton(Color.WHITE);
//        makeColorButton(Color.YELLOW);

        //PIIRTO ALUE
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.SOUTH);
        draw = new PadDraw();
        //draw.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));

        getContentPane().add(draw);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                draw.clear();
            }
        });

        panel.add(clearButton);
        JMenuBar menu = new JMenuBar();
        JMenu a = new JMenu();
        a.setText("FILE");
        JMenuItem save = new JMenuItem();
        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveImage();
            }
        });
        menu.add(a);
        a.add(save);
        JMenuItem load = new JMenuItem();
        load.setText("Load");
        load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadmenu(evt);
            }
        });
        a.add(load);
        setJMenuBar(menu);
        menu.setVisible(true);

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
    
    private void loadmenu(java.awt.event.ActionEvent evt) {
        File file = loadImageActionPerformed();
        if (file == null) {
            return;
        } else {
            try {
                BufferedImage tempImage = ImageIO.read(file);
                draw.setNewImage(tempImage);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
    
    private File loadImageActionPerformed() {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File file = chooser.getSelectedFile();

        return file;
    }
    
    public boolean saveImage() {
        JFileChooser filechooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG Images", "jpg");
        filechooser.setFileFilter(filter);
        int result = filechooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File saveFile = filechooser.getSelectedFile();
            RenderedImage rImage = convertToRenderedImage(draw.image);
            try {
                ImageIO.write(rImage, "jpg", saveFile);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    public RenderedImage convertToRenderedImage(Image image) {

        BufferedImage bImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D bImageGraphics = bImage.createGraphics();
        bImageGraphics.drawImage(image, null, null);
        RenderedImage rImage = (RenderedImage) bImage;
        return rImage;
    }
}
