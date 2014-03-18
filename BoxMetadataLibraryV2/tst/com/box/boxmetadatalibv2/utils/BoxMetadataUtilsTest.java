package com.box.boxmetadatalibv2.utils;

import junit.framework.Assert;

import org.junit.Test;

public class BoxMetadataUtilsTest {

    @Test
    public void testJSONPointerEscaping() {
        Assert.assertEquals("a~1b~0c", BoxMetadataUtils.escapeJSONPointerKey("a/b~c"));
    }
}
