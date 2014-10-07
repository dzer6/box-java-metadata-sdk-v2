package com.box.boxjavalibv2;

import com.box.boxjavalibv2.dao.BoxCollection;
import com.box.boxjavalibv2.dao.BoxOAuthToken;
import com.box.boxjavalibv2.exceptions.AuthFatalFailureException;
import com.box.boxjavalibv2.exceptions.BoxJSONException;
import com.box.boxjavalibv2.exceptions.BoxServerException;
import com.box.boxmetadatalibv2.BoxFileMetadataPlugin;
import com.box.boxmetadatalibv2.IBoxFileMetadataManager;
import com.box.boxmetadatalibv2.dao.BoxFileMetadata;
import com.box.restclientv2.exceptions.BoxRestException;
import com.box.restclientv2.requestsbase.BoxDefaultRequestObject;
import com.box.restclientv2.requestsbase.BoxFileUploadRequestObject;
import org.junit.Test;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import static junit.framework.Assert.assertNotNull;

/**
 * @author Andrew Panfilov
 */
public class MethodNotAllowedTest {

    public static final String METADATA_KEY_ONE = "metadata-key-one";
    public static final String METADATA_VALUE_ONE = "metadata-value-one";

    public static final String FOLDER_ID_ROOT = "0";
    public static final String FILE_1 = "1.txt";

    public static final String CLIENT_ID = "your client id"; // TODO change it!
    public static final String CLIENT_SECRET = "your client secret"; // TODO change it!

    public static final int PORT = 8080; // TODO change it! your app port that should be configured in box admin console
    public static final int ALREADY_EXISTS = 409;

    @Test
    public void test() throws Exception {
        final BoxClient client = new BoxClient(CLIENT_ID, CLIENT_SECRET, null, null, null, new BoxConnectionManagerBuilder().build());

        String code = "";
        String url = "https://www.box.com/api/oauth2/authorize?response_type=code&client_id=" + CLIENT_ID;
        try {
            Desktop.getDesktop().browse(java.net.URI.create(url));
            code = getCode();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        final BoxOAuthToken bt = client.getOAuthManager().createOAuth(code, CLIENT_ID, CLIENT_SECRET, null);
        client.authenticate(bt);

        String fileId;

        try {
            fileId = uploadFile(client, FOLDER_ID_ROOT, FILE_1);
        } catch (BoxServerException e) {
            if (e.getStatusCode() == ALREADY_EXISTS) {
                deleteExistedFile(client, FILE_1);
                Thread.sleep(3000); // need to wait while box updating info about deleted file
                fileId = uploadFile(client, FOLDER_ID_ROOT, FILE_1);
            } else {
                throw e;
            }
        }

        final BoxFileMetadataPlugin plugin = new BoxFileMetadataPlugin();
        final IBoxFileMetadataManager metadataManager = plugin.plugin(client);

        final HashMap<String, String> map = new HashMap();
        map.put(METADATA_KEY_ONE, METADATA_VALUE_ONE);

        final BoxFileMetadata metadata = metadataManager.createOrUpdateMetadata(fileId, IBoxFileMetadataManager.METADATA_TYPE_PROPERTIES, map);

        assertNotNull(metadata);
    }

    protected void deleteExistedFile(final BoxClient client, final String fileName) throws BoxRestException, BoxServerException, AuthFatalFailureException {
        final BoxDefaultRequestObject searchRequest = new BoxDefaultRequestObject();

        searchRequest.put("type", "file");
        searchRequest.put("content_types", "name");

        final BoxCollection boxCollection = client.getSearchManager().search(fileName, searchRequest);

        if (boxCollection.getTotalCount() != 0) {
            final String existedFileId = boxCollection.getEntries().get(0).getId();
            client.getFilesManager().deleteFile(existedFileId, new BoxDefaultRequestObject());
        }
    }

    protected String uploadFile(final BoxClient client, String folderId, String fileName) throws BoxRestException, BoxJSONException, FileNotFoundException, BoxServerException, AuthFatalFailureException, InterruptedException {
        final BoxFileUploadRequestObject boxFileUploadRequestObject = BoxFileUploadRequestObject.uploadFileRequestObject(folderId, fileName, content(fileName));
        return client.getFilesManager().uploadFile(boxFileUploadRequestObject).getId();
    }

    protected String getCode() throws IOException {

        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            socket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String code = "";
                try {
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    out.write("HTTP/1.1 200 OK\r\n");
                    out.write("Content-Type: text/html\r\n");
                    out.write("\r\n");

                    code = in.readLine();
                    System.out.println(code);
                    String match = "code";
                    int loc = code.indexOf(match);

                    if (loc > 0) {
                        int httpstr = code.indexOf("HTTP") - 1;
                        code = code.substring(code.indexOf(match), httpstr);
                        String parts[] = code.split("=");
                        code = parts[1];
                        out.write("Now return to command line to see the output of the HelloWorld sample app.");
                    }
                    else {
                        // It doesn't have a code
                        out.write("Code not found in the URL!");
                    }

                    out.close();

                    return code;
                }
                catch (IOException e) {
                    // error ("System: " + "Connection to server lost!");
                    System.exit(1);
                    break;
                }
            }
            return "";
        }
        finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
            if (socket != null) {
                socket.close();
            }
        }
    }

    protected FileInputStream content(final String fileName) throws FileNotFoundException {
        return new FileInputStream("tst-resources/3460/" + fileName);
    }
}
