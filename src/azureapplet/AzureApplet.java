package azureapplet;

import com.microsoft.windowsazure.services.blob.client.CloudBlob;
import com.microsoft.windowsazure.services.blob.client.ListBlobItem;
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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
//import com.microsoft.windowsazure.services.core.storage.*;
//import com.microsoft.windowsazure.services.blob.client.*;
public class AzureApplet extends JApplet {

    private JPanel panel;
    private PadDraw draw;
    JColorChooser chooser;

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
        final JButton hideChooserButton = new JButton("Hide chooser");
        hideChooserButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (chooser.isVisible()) {
                    chooser.setVisible(false);
                    hideChooserButton.setText("Show Chooser");
                } else {
                    chooser.setVisible(true);
                    hideChooserButton.setText("Hide Chooser");
                }
            }
        });

        panel.add(clearButton);
        panel.add(hideChooserButton);
        JMenuBar menu = new JMenuBar();
        JMenu a = new JMenu();
        a.setText("File");
        menu.add(a);
        JMenuItem locsave = new JMenuItem();
        locsave.setText("Save");
        locsave.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveImage();
            }
        });
        JMenuItem locload = new JMenuItem();
        locload.setText("Load");
        locload.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadmenu(evt);
            }
        });
        a.add(locsave);
        a.add(locload);
        JMenu blob = new JMenu();
        blob.setText("Blob");
        menu.add(blob);
        JMenuItem save = new JMenuItem();
        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBlobImage();
            }

            private void saveBlobImage() {
                String fileName = JOptionPane.showInputDialog(null, "Give a name to the file to upload to the blob:",
                        "Blob save", 1);
                if (fileName != null) {
                    RenderedImage image = convertToRenderedImage(draw.image);
                    BlobConnection connection = new BlobConnection();
                    connection.connect();
                    if (connection.upload(image, fileName)) {
                        JOptionPane.showMessageDialog(rootPane, "Image succesfully saved!");
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Image save failed!");
                    }
                } else {
                    return;
                }

            }
        });
        blob.add(save);
        JMenuItem load = new JMenuItem();
        load.setText("Load");
        load.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadblobImage();
            }

            private void loadblobImage() {

                BlobConnection connection = new BlobConnection();
                connection.connect();

//                }
//                String s = displayListOfItems(blobNames);
                ArrayList<ListBlobItem> array = connection.getList();
                String viesti = "Give a name to the file to load from the blob:\n";
                ArrayList<ListBlobItem> list = connection.getList();

                for (int i = 0; i < list.size(); i++) {
                    viesti = viesti + list.get(i).getUri().toString() + "\n";
                }
                JOptionPane pane = new JOptionPane();
                JScrollPane scroll = new JScrollPane(pane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                String fileName = pane.showInputDialog(null, viesti, 1);
                if (fileName != null) {
                    File newFile = connection.downLoad(fileName);
                    if (newFile != null) {
                        try {
                            BufferedImage newImage = ImageIO.read(newFile);
                            newFile.delete();
                            draw.setNewImage(newImage);
                        } catch (IOException ex) {
                            Logger.getLogger(AzureApplet.class.getName()).log(Level.SEVERE, null, ex);
                            JOptionPane.showMessageDialog(rootPane, "Image load failed! Reason: " + ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Image load failed! Reason: Incorrect File Name");
                    }
                } else {
                }

            }
//            private String displayListOfItems(String[] blobNames) {
//                JPopupMenu pmenu;
//                pmenu = new JPopupMenu();
//                for (String string : blobNames) {
//                    JMenuItem item = new JMenuItem(string);
//                    pmenu.add(item);
//                }
//                pmenu.setVisible(true);
//                pmenu.setLocation(50, 50);
//                return "lol";
//            }
        });
        blob.add(load);
        JMenuItem delete = new JMenuItem();

        delete.setText(
                "(ADMIN) Delete");
        delete.addActionListener(
                new java.awt.event.ActionListener() {

                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        deleteBlob();
                    }

                    private void deleteBlob() {
                        String password = JOptionPane.showInputDialog(null, "Give the admin password.",
                                "Blob load", 1);
                        if (password.equals("Mursukuningas")) {
                            BlobConnection connection = new BlobConnection();
                            connection.connect();
                            
                            ArrayList<ListBlobItem> array = connection.getList();
                            String viesti = "Give a name to the file to delete:\n";
                            ArrayList<ListBlobItem> list = connection.getList();
                            for (int i = 0; i < list.size(); i++) {
                                viesti = viesti + list.get(i).getUri().toString() + "\n";
                            }
                            JOptionPane pane = new JOptionPane();
                            JScrollPane scroll = new JScrollPane(pane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                            String fileName = pane.showInputDialog(null, viesti, 1);

                            int result = connection.delete(fileName);
                            if (result == 0) {
                                JOptionPane.showMessageDialog(rootPane, "Deletion successful!");
                            } else if (result == -1) {
                                JOptionPane.showMessageDialog(rootPane, "Image load failed! Reason: File requested was not found.");
                            } else if (result == -2) {
                                JOptionPane.showMessageDialog(rootPane, "Image load failed! Reason: Storage error");
                            } else {
                                JOptionPane.showMessageDialog(rootPane, "Image load failed! Reason: Java Error");
                            }
                        } else {
                            JOptionPane.showMessageDialog(rootPane, "You shouldn't be tinkering around here");
                        }
                    }
                });
        blob.add(delete);
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
