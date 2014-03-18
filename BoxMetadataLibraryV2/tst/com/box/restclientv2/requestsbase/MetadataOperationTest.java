package com.box.restclientv2.requestsbase;

import junit.framework.Assert;

import org.junit.Test;

import com.box.boxjavalibv2.exceptions.BoxJSONException;
import com.box.boxjavalibv2.jsonparsing.BoxJSONParser;
import com.box.boxjavalibv2.jsonparsing.BoxResourceHub;
import com.box.boxmetadatalibv2.requests.MetadataOperation;
import com.box.boxmetadatalibv2.requests.MetadataOperation.Operation;
import com.box.boxmetadatalibv2.utils.BoxMetadataUtils;

public class MetadataOperationTest {

    @Test
    public void testConstructing() {
        Operation op = Operation.TEST;
        String key = "hello";
        String value = "hellovalue";

        MetadataOperation metaOperation = new MetadataOperation(op, key, value);
        Assert.assertEquals(op, metaOperation.getOperation());
        Assert.assertEquals(key, metaOperation.getKey());
        Assert.assertEquals(value, metaOperation.getValue());
    }

    @Test
    public void testJSONPointerEscaping() throws BoxJSONException {
        Operation op = Operation.TEST;
        String trySlash = "/tryslash";
        String tryTilde = "~trytilde";
        String key = "something" + trySlash + tryTilde;
        String value = "hellovalue";

        MetadataOperation metaOperation = new MetadataOperation(op, key, value);
        String opString = metaOperation.toJSONString(new BoxJSONParser(new BoxResourceHub()));
        Assert.assertTrue(opString.contains(BoxMetadataUtils.SLASH + BoxMetadataUtils.escapeJSONPointerKey(key)));
    }
}
