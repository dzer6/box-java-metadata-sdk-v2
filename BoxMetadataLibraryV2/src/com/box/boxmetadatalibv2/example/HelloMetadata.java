package com.box.boxmetadatalibv2.example;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.box.boxjavalibv2.BoxClient;
import com.box.boxjavalibv2.dao.BoxFile;
import com.box.boxjavalibv2.exceptions.AuthFatalFailureException;
import com.box.boxjavalibv2.exceptions.BoxJSONException;
import com.box.boxjavalibv2.exceptions.BoxServerException;
import com.box.boxmetadatalibv2.BoxFileMetadataPlugin;
import com.box.boxmetadatalibv2.IBoxFileMetadataManager;
import com.box.boxmetadatalibv2.dao.BoxFileMetadata;
import com.box.restclientv2.exceptions.BoxRestException;
import com.box.restclientv2.requestsbase.BoxFileUploadRequestObject;
import com.box.restclientv2.requestsbase.MetadataJSONPatchRequestObject;

public class HelloMetadata {

    private final BoxClient client;

    public HelloMetadata(BoxClient client) {
        this.client = client;
    }

    public void start() throws BoxRestException, BoxServerException, AuthFatalFailureException, InterruptedException, BoxJSONException {
        String fileContent = "abc";
        InputStream is = new ByteArrayInputStream(fileContent.getBytes());
        BoxFile bFile = null;

        try {
            bFile = client.getFilesManager().uploadFile(BoxFileUploadRequestObject.uploadFileRequestObject("0", "a" + System.currentTimeMillis(), is));
            String fileId = bFile.getId();
            metadataSample(fileId);
        }
        finally {
            if (bFile != null) {
                client.getFilesManager().deleteFile(bFile.getId(), null);
            }
        }
    }

    private void metadataSample(String fileId) throws BoxRestException, AuthFatalFailureException, BoxServerException {
        // Plug in metadata client.
        BoxFileMetadataPlugin plugin = new BoxFileMetadataPlugin();
        IBoxFileMetadataManager manager = plugin.plugin(client);
        // Or instead, can use:
        // BoxFileMetadataManager manager = (BoxFileMetadataManager) client.getPluginManager(BoxFileMetadataPlugin.FILE_METADATA_MANAGER_KEY);

        String key1 = "key0";
        String value1 = "value0";
        // create metadata
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(key1, value1);
        BoxFileMetadata meta = manager.createMetadata(fileId, IBoxFileMetadataManager.METADATA_TYPE_PROPERTIES, map);
        System.out.println(String.format("metadata added,{key, value}:{%s,%s}", key1, value1));
        meta = manager.getMetadata(fileId, IBoxFileMetadataManager.METADATA_TYPE_PROPERTIES, null);
        // Get metadata
        Map<String, String> metamap = meta.getAllMetadata();
        System.out.println("retrieved metadata:");
        printMap(metamap);

        key1 = "key1";
        value1 = "value1";
        meta = manager.updateMetadata(fileId, IBoxFileMetadataManager.METADATA_TYPE_PROPERTIES, key1, value1);
        System.out.println(String.format("metadata added,{key, value}:{%s,%s}", key1, value1));
        meta = manager.getMetadata(fileId, IBoxFileMetadataManager.METADATA_TYPE_PROPERTIES, null);
        // Get metadata
        metamap = meta.getAllMetadata();
        System.out.println("retrieved metadata:");
        printMap(metamap);

        // Test metadata (pass)
        System.out.println("test should pass");
        meta = manager.testMetadata(fileId, IBoxFileMetadataManager.METADATA_TYPE_PROPERTIES, key1, value1);
        System.out.println("test passed");

        // Test metadata (fail)
        try {
            System.out.println("test should fail");
            meta = manager.testMetadata(fileId, IBoxFileMetadataManager.METADATA_TYPE_PROPERTIES, key1, "blablabla");
        }
        catch (BoxServerException e) {
            System.out.println("test failed");
        }

        // Batch operation (test pass then update)
        MetadataJSONPatchRequestObject obj = MetadataJSONPatchRequestObject.testOperation(key1, value1);
        obj.appendAddOperation("something", "somevalue");
        manager.executeBatchMetadataRequest(fileId, IBoxFileMetadataManager.METADATA_TYPE_PROPERTIES, obj);
        meta = manager.getMetadata(fileId, IBoxFileMetadataManager.METADATA_TYPE_PROPERTIES, null);
        metamap = meta.getAllMetadata();
        System.out.println("retrieved metadata:");
        printMap(metamap);

        // Batch operation (test fail then update won't happen)
        MetadataJSONPatchRequestObject obj2 = MetadataJSONPatchRequestObject.testOperation(key1, "blablabla");
        obj.appendAddOperation("somethingelse", "somevalueelse");
        try {
            manager.executeBatchMetadataRequest(fileId, IBoxFileMetadataManager.METADATA_TYPE_PROPERTIES, obj2);
        }
        catch (BoxServerException e) {
            System.out.println("test failed");
        }
        meta = manager.getMetadata(fileId, IBoxFileMetadataManager.METADATA_TYPE_PROPERTIES, null);
        metamap = meta.getAllMetadata();
        System.out.println("retrieved metadata:");
        printMap(metamap);
    }

    private void printMap(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "," + entry.getValue());
        }
    }
}
