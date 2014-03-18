package com.box.restclientv2.requestsbase;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

import com.box.boxjavalibv2.jsonparsing.BoxJSONParser;
import com.box.boxjavalibv2.jsonparsing.BoxResourceHub;
import com.box.boxmetadatalibv2.requests.MetadataOperation;
import com.box.boxmetadatalibv2.requests.MetadataOperation.Operation;
import com.box.restclientv2.exceptions.BoxRestException;
import com.box.restclientv2.requestsbase.MetadataJSONPatchRequestObject;

public class MetadataJSONPatchRequestObjectTest {

    @Test
    public void testContentType() throws BoxRestException {
        MetadataJSONPatchRequestObject obj = MetadataJSONPatchRequestObject.addOperation("a", "b");
        Assert.assertEquals(MetadataJSONPatchRequestObject.JSON_PATCH_CONTENT_TYPE, obj.getEntity(new BoxJSONParser(new BoxResourceHub())).getContentType()
            .getValue());
    }

    @Test
    public void testAddOperation() {
        String key = "testKey";
        String value = "testValue";
        MetadataJSONPatchRequestObject obj = MetadataJSONPatchRequestObject.addOperation(key, value);
        ArrayList<MetadataOperation> operations = obj.getOperations();
        Assert.assertEquals(1, operations.size());
        MetadataOperation op = operations.get(0);
        assertOperation(op, key, value, Operation.ADD);
    }

    @Test
    public void testTestOperation() {
        String key = "testKey";
        String value = "testValue";
        MetadataJSONPatchRequestObject obj = MetadataJSONPatchRequestObject.testOperation(key, value);
        ArrayList<MetadataOperation> operations = obj.getOperations();
        Assert.assertEquals(1, operations.size());
        MetadataOperation op = operations.get(0);
        assertOperation(op, key, value, Operation.TEST);
    }

    @Test
    public void testRemoveOperation() {
        String key = "testKey";
        MetadataJSONPatchRequestObject obj = MetadataJSONPatchRequestObject.removeOperation(key);
        ArrayList<MetadataOperation> operations = obj.getOperations();
        Assert.assertEquals(1, operations.size());
        MetadataOperation op = operations.get(0);
        assertOperation(op, key, null, Operation.REMOVE);
    }

    @Test
    public void testBatchOperation() {
        MetadataJSONPatchRequestObject obj = MetadataJSONPatchRequestObject.batchOperation();
        String key1 = "testKey1";
        String value1 = "testValue1";
        String key2 = "testKey1";
        String value2 = "testValue1";
        obj.appendTestOperation(key1, value1);
        obj.appendAddOperation(key2, value2);
        ArrayList<MetadataOperation> operations = obj.getOperations();
        Assert.assertEquals(2, operations.size());

        MetadataOperation op1 = operations.get(0);
        assertOperation(op1, key1, value1, Operation.TEST);
        MetadataOperation op2 = operations.get(1);
        assertOperation(op2, key2, value2, Operation.ADD);
    }

    private void assertOperation(MetadataOperation op, String expectedKey, String expectedValue, Operation expectedOperation) {
        Assert.assertEquals(expectedOperation, op.getOperation());
        Assert.assertEquals(expectedKey, op.getKey());
        if (expectedValue != null) {
            Assert.assertEquals(expectedValue, op.getValue());
        }
    }
}
