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

public class GetFileMetadataRequestTest extends RequestTestBase {

    @Test
    public void uriTest() {
        Assert.assertEquals("/files/123/metadata/properties", GetFileMetadataRequest.getUri("123", IBoxFileMetadataManager.METADATA_TYPE_PROPERTIES));
    }

    @Test
    public void testRequestWellFormed() throws BoxRestException, AuthFatalFailureException {
        String fileId = "testfileid";
        GetFileMetadataRequest request = new GetFileMetadataRequest(CONFIG, JSON_PARSER, fileId, IBoxFileMetadataManager.METADATA_TYPE_PROPERTIES, null);
        this.testRequestIsWellFormed(request, (new BoxConfigBuilder()).build().getApiUrlAuthority(),
            (new BoxConfigBuilder()).build().getApiUrlPath().concat(GetFileMetadataRequest.getUri(fileId, IBoxFileMetadataManager.METADATA_TYPE_PROPERTIES)),
            HttpStatus.SC_OK, RestMethod.GET);
    }
}
