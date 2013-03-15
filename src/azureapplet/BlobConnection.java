/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azureapplet;

import com.microsoft.windowsazure.services.core.storage.*;
import com.microsoft.windowsazure.services.blob.client.*;

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

    public boolean connect() {
        return true;
    }
}
