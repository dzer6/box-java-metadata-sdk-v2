package com.box.boxmetadatalibv2.example;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.box.boxjavalibv2.BoxClient;
import com.box.boxjavalibv2.BoxConfigBuilder;
import com.box.boxjavalibv2.dao.BoxOAuthToken;
import com.box.boxjavalibv2.exceptions.AuthFatalFailureException;
import com.box.boxjavalibv2.exceptions.BoxJSONException;
import com.box.boxjavalibv2.exceptions.BoxServerException;
import com.box.restclientv2.exceptions.BoxRestException;

/**
 * Most of this class is handling login. For metadata specific code, please check the HelloMetadata class in the same package.
 */
public class HelloWorld {

    public static final int PORT = 4000;
    public static final String key = "YOUR API KEY HERE";
    public static final String secret = "YOUR OAUTH2 SECRET HERE";

    public static void main(String[] args) throws AuthFatalFailureException, BoxServerException, BoxRestException, InterruptedException, BoxJSONException {

        if (key.equals("YOUR API KEY HERE")) {
            System.out.println("Before this sample app will work, you will need to change the");
            System.out.println("'key' and 'secret' values in the source code.");
            return;
        }

        String code = "";
        String url = "https://www.box.com/api/oauth2/authorize?response_type=code&client_id=" + key;
        try {
            Desktop.getDesktop().browse(java.net.URI.create(url));
            code = getCode();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        BoxClient client = getAuthenticatedClient(code);

        HelloMetadata metadata = new HelloMetadata(client);
        metadata.start();
    }

    private static BoxClient getAuthenticatedClient(String code) throws BoxRestException, BoxServerException, AuthFatalFailureException {
        BoxClient client = new BoxClient(key, secret, null, null, (new BoxConfigBuilder()).build());
        BoxOAuthToken bt = client.getOAuthManager().createOAuth(code, key, secret, "http://localhost:" + PORT);
        client.authenticate(bt);
        return client;
    }

    private static String getCode() throws IOException {

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

}
