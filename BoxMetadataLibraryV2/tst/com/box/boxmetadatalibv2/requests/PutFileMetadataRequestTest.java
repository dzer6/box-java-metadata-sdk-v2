package com.box.boxmetadatalibv2.requests;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import com.box.boxjavalibv2.BoxConfigBuilder;
import com.box.boxjavalibv2.exceptions.AuthFatalFailureException;
import com.box.boxjavalibv2.requests.RequestTestBase;
import com.box.boxmetadatalibv2.IBoxFileMetadataManager;
import com.box.restclientv2.RestMethod;
import com.box.restclientv2.exceptions.BoxRestException;

public class PutFileMetadataRequestTest extends RequestTestBase {

    @Test
    public void uriTest() {
        Assert.assertEquals("/files/123/metadata/properties", PutFileMetadataRequest.getUri("123", IBoxFileMetadataManager.METADATA_TYPE_PROPERTIES));
    }

    @Test
    public void testRequestWellFormed() throws BoxRestException, AuthFatalFailureException {
        String fileId = "testfileid";
        PutFileMetadataRequest request = new PutFileMetadataRequest(CONFIG, JSON_PARSER, fileId, IBoxFileMetadataManager.METADATA_TYPE_PROPERTIES, null);
        this.testRequestIsWellFormed(request, (new BoxConfigBuilder()).build().getApiUrlAuthority(),
            (new BoxConfigBuilder()).build().getApiUrlPath().concat(PutFileMetadataRequest.getUri(fileId, IBoxFileMetadataManager.METADATA_TYPE_PROPERTIES)),
            HttpStatus.SC_OK, RestMethod.PUT);
    }
}