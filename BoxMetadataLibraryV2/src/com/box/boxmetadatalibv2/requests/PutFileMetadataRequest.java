package com.box.boxmetadatalibv2.requests;

import com.box.boxjavalibv2.IBoxConfig;
import com.box.boxjavalibv2.jsonparsing.IBoxJSONParser;
import com.box.restclientv2.RestMethod;
import com.box.restclientv2.exceptions.BoxRestException;
import com.box.restclientv2.requestsbase.DefaultBoxRequest;
import com.box.restclientv2.requestsbase.MetadataJSONPatchRequestObject;

public class PutFileMetadataRequest extends DefaultBoxRequest {

    private static final String URI = "/files/%s/metadata/%s";

    public PutFileMetadataRequest(final IBoxConfig config, final IBoxJSONParser parser, String fileId, String metadataType,
        MetadataJSONPatchRequestObject requestObject) throws BoxRestException {
        super(config, parser, getUri(fileId, metadataType), RestMethod.PUT, requestObject);
    }

    public static String getUri(final String fileId, final String type) {
        return String.format(URI, fileId, type);
    }
}