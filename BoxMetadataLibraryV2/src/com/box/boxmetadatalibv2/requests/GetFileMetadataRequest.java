package com.box.boxmetadatalibv2.requests;

import com.box.boxjavalibv2.IBoxConfig;
import com.box.boxjavalibv2.jsonparsing.IBoxJSONParser;
import com.box.restclientv2.RestMethod;
import com.box.restclientv2.exceptions.BoxRestException;
import com.box.restclientv2.requestsbase.BoxDefaultRequestObject;
import com.box.restclientv2.requestsbase.DefaultBoxRequest;

public class GetFileMetadataRequest extends DefaultBoxRequest {

    private static final String URI = "/files/%s/metadata/%s";

    public GetFileMetadataRequest(final IBoxConfig config, final IBoxJSONParser parser, String fileId, String type, BoxDefaultRequestObject requestObject)
        throws BoxRestException {
        super(config, parser, getUri(fileId, type), RestMethod.GET, requestObject);
    }

    public static String getUri(final String fileId, final String type) {
        return String.format(URI, fileId, type);
    }
}