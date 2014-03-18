package com.box.restclientv2.requestsbase;

import java.util.ArrayList;

import org.apache.commons.codec.CharEncoding;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.box.boxjavalibv2.jsonparsing.IBoxJSONParser;
import com.box.boxmetadatalibv2.requests.MetadataOperation;
import com.box.boxmetadatalibv2.requests.MetadataOperation.Operation;
import com.box.restclientv2.exceptions.BoxRestException;

/**
 * Metadata request object that will be turned in to a <a href="http://tools.ietf.org/html/rfc6902">JSON patch</a> in http request.
 */
public class MetadataJSONPatchRequestObject extends BoxDefaultRequestObject {

    public static final String JSON_PATCH_CONTENT_TYPE = "application/json-patch+json";

    private MetadataJSONPatchRequestObject() {
        super();
    }

    private final ArrayList<MetadataOperation> operations = new ArrayList<MetadataOperation>();

    /**
     * Create a request object for batch operation. You can add more operations to the request object. e.g.,
     * MetadataJSONPatchRequestObject.batchOperation().testOperation("key","value").appendAddOperation("newKey","newValue");
     */
    public static MetadataJSONPatchRequestObject batchOperation() {
        return new MetadataJSONPatchRequestObject();
    }

    /**
     * Create a request object to add metadata. You can also append more operations.
     * e.g.MetadataJSONPatchRequestObject.addOperation("key1","value1").appendAddOperation("key2","value2");
     */
    public static MetadataJSONPatchRequestObject addOperation(String key, String value) {
        MetadataJSONPatchRequestObject obj = new MetadataJSONPatchRequestObject();
        obj.appendAddOperation(key, value);
        return obj;
    }

    /**
     * Create a request object to test metadata. You can also append more operations.
     * e.g.MetadataJSONPatchRequestObject.testOperation("key","value").appendAddOperation("key2","value2");
     */
    public static MetadataJSONPatchRequestObject testOperation(String key, String value) {
        MetadataJSONPatchRequestObject obj = new MetadataJSONPatchRequestObject();
        obj.appendTestOperation(key, value);
        return obj;
    }

    /**
     * Create a request object to test metadata. You can also append more operations.
     * e.g.MetadataJSONPatchRequestObject.removeOperation("key").appendAddOperation("key2","value2");
     */
    public static MetadataJSONPatchRequestObject removeOperation(String key) {
        MetadataJSONPatchRequestObject obj = new MetadataJSONPatchRequestObject();
        obj.appendRemoveOperation(key);
        return obj;
    }

    ArrayList<MetadataOperation> getOperations() {
        return operations;
    }

    /**
     * Append an add operation to existing metadata operation(s).
     * e.g.:MetadataJSONPatchRequestObject.testOperation("key","value").appendAddOperation("key2","value2");
     */
    public MetadataJSONPatchRequestObject appendAddOperation(String key, String value) {
        return insertOperation(new MetadataOperation(Operation.ADD, key, value));
    }

    /**
     * append a remove operation to existing metadata operation(s) e.g.
     * MetadataJSONPatchRequestObject.testOperation("key","value").appendRemoveOperation("key");
     */
    public MetadataJSONPatchRequestObject appendRemoveOperation(String key) {
        return insertOperation(new MetadataOperation(Operation.REMOVE, key, null));
    }

    /**
     * append a test operation to existing metadata operation(s) e.g.
     * MetadataJSONPatchRequestObject.addOperation("keya","valuea").appendTestOperation("keyb","valueb");
     */
    public MetadataJSONPatchRequestObject appendTestOperation(String key, String value) {
        return insertOperation(new MetadataOperation(Operation.TEST, key, value));
    }

    private MetadataJSONPatchRequestObject insertOperation(MetadataOperation op) {
        operations.add(op);
        return this;
    }

    @Override
    HttpEntity getEntity(IBoxJSONParser parser) throws BoxRestException {
        try {
            StringEntity entity = new StringEntity(parser.convertBoxObjectToJSONString(operations.toArray()), CharEncoding.UTF_8);
            entity.setContentType(JSON_PATCH_CONTENT_TYPE);
            return entity;
        }
        catch (Exception e) {
            throw new BoxRestException(e);
        }
    }
}
