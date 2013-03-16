package azureapplet;

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

//    public static final String storageConnectionString = 
//    	    "DefaultEndpointsProtocol=http;" + 
//    	    "AccountName=portalvhdskvxq9mzmh61c5;" + 
//    	    "AccountKey=WiVtbH14RumlFBybM+AhWM7Nyc9hGBIsKpEDQUyEKld0I3+65VcooLdMx6+/6EdQjNuU681uWaMq/G4pQ7zAoQ==";
//    String storageConnectionString = 
//    	    RoleEnvironment.getConfigurationSettings().get("StorageConnectionString");
//    
//    CloudBlobContainer container = blobClient.getContainerReference("mycontainer");
//
//    // Create the container if it does not exist
//    container.createIfNotExist();
// 
//    BlobContainerPermissions containerPermissions = new BlobContainerPermissions();
//
//    //Include public access in the permissions object
//	containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
//
//	//Set the permissions on the container
//	container.uploadPermissions(containerPermissions);
//	
//	// Retrieve storage account from connection-string
//	CloudStorageAccount storageAccount =
//	    CloudStorageAccount.parse(storageConnectionString);
//
//	// Create the blob client
//	CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
//
//	// Retrieve reference to a previously created container
//	CloudBlobContainer container = blobClient.getContainerReference("mycontainer");
//
//	// Create or overwrite the "myimage.jpg" blob with contents from a local file
//	CloudBlockBlob blob = container.getBlockBlobReference("myimage.jpg");
//	File source = new File("c:\\myimages\\myimage.jpg");
//	blob.upload(new FileInputStream(source), source.length());
//	
//	// Retrieve storage account from connection-string
//	CloudStorageAccount storageAccount =
//	    CloudStorageAccount.parse(storageConnectionString);
//
//	// Create the blob client
//	CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
//
//	// Retrieve reference to a previously created container
//	CloudBlobContainer container = blobClient.getContainerReference("mycontainer");
//
//	// Loop over blobs within the container and output the URI to each of them
//	for (ListBlobItem blobItem : container.listBlobs()) {
//	    System.out.println(blobItem.getUri());
//	}
    //@Override
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
        a.setText("FILE");
        menu.add(a);
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
//                ArrayList<ListBlobItem> list = connection.getList();
//                String[] blobNames = new String[list.size()];
//
//                for (int i = 0; i < list.size(); i++) {
//                    blobNames[i] = list.get(i).getUri().toString();
//                }
//                String s = displayListOfItems(blobNames);
                String fileName = JOptionPane.showInputDialog(null, "Give a name to the file to load from the blob:",
                        "Blob load", 1);
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
            }

            );
        blob.add (load);

            setJMenuBar(menu);

            menu.setVisible (
        
        true);

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
