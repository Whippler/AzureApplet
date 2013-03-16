/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azureapplet;

import com.microsoft.windowsazure.services.core.storage.*;
import com.microsoft.windowsazure.services.blob.client.*;
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

/**
 *
 * @author Lauri Suomalainen
 */
public class BlobConnection {

    public static final String storageConnectionString1 =
            "DefaultEndpointsProtocol=http;"
            + "AccountName=portalvhdskvxq9mzmh61c5;"
            + "AccountKey=WiVtbH14RumlFBybM+AhWM7Nyc9hGBIsKpEDQUyEKld0I3+65VcooLdMx6+/6EdQjNuU681uWaMq/G4pQ7zAoQ==";
    public static final String storageConnectionString2 =
            "DefaultEndpointsProtocol=http;"
            + "AccountName=portalvhdskvxq9mzmh61c5;"
            + "AccountKey=BJnks9Qoa5fhNP71FLilgypWwwGhs8d/c7/ioTRlCBmNyXi4N4AxKo1qDHC50oQ95YYwsvyuyVe92x0xiqCTGQ==";
    public CloudStorageAccount account;
    public CloudBlobContainer container;
    public CloudBlobClient blobClient;

    public boolean connect() {
        
        boolean secondTry = false;
        try {
            account = CloudStorageAccount.parse(storageConnectionString1);
        } catch (URISyntaxException | InvalidKeyException ex) {
            Logger.getLogger(BlobConnection.class.getName()).log(Level.SEVERE, null, ex);
            secondTry = true;
        }
        if (secondTry) {
            try {
                account = CloudStorageAccount.parse(storageConnectionString2);
            } catch (    URISyntaxException | InvalidKeyException ex) {
                Logger.getLogger(BlobConnection.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        blobClient = account.createCloudBlobClient();
        try {
            container = blobClient.getContainerReference("pictures");
            container.createIfNotExist();
            BlobContainerPermissions containerPermissions = new BlobContainerPermissions();
            containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
            container.uploadPermissions(containerPermissions);

        } catch (URISyntaxException | StorageException ex) {
            Logger.getLogger(BlobConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public boolean upload(RenderedImage image, String imageName) {
        try {
            FileInputStream stream;
            CloudBlockBlob blob = container.getBlockBlobReference(imageName);
            File newFile = new File(imageName);
            newFile.deleteOnExit();
            ImageIO.write(image, "jpg", newFile);
            blob.upload(stream = new FileInputStream(newFile), newFile.length());
            stream.close();
            newFile.delete();
            return true;
        } catch (IOException | URISyntaxException | StorageException ex) {
            Logger.getLogger(BlobConnection.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }   
        public ArrayList getList() {
            ArrayList<ListBlobItem> array = new ArrayList<>();
            for (ListBlobItem blob : container.listBlobs()) {
                array.add((blob));
            }
            return array;
        }
        
        public File downLoad(String fileName) {
            CloudBlob blob = null;
            File newFile = new File(fileName);
            FileOutputStream stream;
            ArrayList<ListBlobItem> array = getList();
        try {
            for (ListBlobItem blob2 : array) {
                if (blob2.getUri().toString().contains(fileName)) {
                    blob = (CloudBlob) blob2;
                    if (fileName.equals(blob.getName())) {
                    break;
                    } else {
                        blob = null;
                    }
                }
            }
            if (blob == null) {
                return null;
            }
            blob.download(stream = new FileOutputStream(fileName));
            stream.close();
            return newFile;
        } catch (URISyntaxException ex) {
            Logger.getLogger(BlobConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException | StorageException ex) {
            Logger.getLogger(BlobConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
            
        }
        
    
}
