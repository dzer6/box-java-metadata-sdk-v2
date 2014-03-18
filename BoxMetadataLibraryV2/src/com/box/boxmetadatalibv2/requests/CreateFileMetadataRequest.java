package com.box.boxmetadatalibv2.requests;

import org.apache.http.HttpStatus;

import com.box.boxjavalibv2.IBoxConfig;
import com.box.boxjavalibv2.jsonparsing.IBoxJSONParser;
import com.box.restclientv2.RestMethod;
import com.box.restclientv2.exceptions.BoxRestException;
import com.box.restclientv2.requestsbase.CreateMetadataRequestObject;
import com.box.restclientv2.requestsbase.DefaultBoxRequest;

public class CreateFileMetadataRequest extends DefaultBoxRequest {

    private static final String URI = "/files/%s/metadata/%s";

    public CreateFileMetadataRequest(IBoxConfig config, IBoxJSONParser parser, String fileId, String metadataType, CreateMetadataRequestObject requestObject)
        throws BoxRestException {
        super(config, parser, getUri(fileId, metadataType), RestMethod.POST, requestObject);
        setExpectedResponseCode(HttpStatus.SC_CREATED);
    }

    public static String getUri(final String fileId, final String type) {
        return String.format(URI, fileId, type);
    }
}
