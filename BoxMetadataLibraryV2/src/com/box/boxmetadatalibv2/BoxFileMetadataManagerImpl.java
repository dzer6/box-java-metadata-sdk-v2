package com.box.boxmetadatalibv2;

import java.util.Map;

import org.apache.http.HttpStatus;

import com.box.boxjavalibv2.IBoxConfig;
import com.box.boxjavalibv2.dao.IBoxType;
import com.box.boxjavalibv2.exceptions.AuthFatalFailureException;
import com.box.boxjavalibv2.exceptions.BoxServerException;
import com.box.boxjavalibv2.jsonparsing.IBoxJSONParser;
import com.box.boxjavalibv2.jsonparsing.IBoxResourceHub;
import com.box.boxjavalibv2.resourcemanagers.AbstractBoxResourceManager;
import com.box.boxmetadatalibv2.dao.BoxFileMetadata;
import com.box.boxmetadatalibv2.dao.BoxMetadataType;
import com.box.boxmetadatalibv2.requests.CreateFileMetadataRequest;
import com.box.boxmetadatalibv2.requests.GetFileMetadataRequest;
import com.box.boxmetadatalibv2.requests.PutFileMetadataRequest;
import com.box.restclientv2.IBoxRESTClient;
import com.box.restclientv2.authorization.IBoxRequestAuth;
import com.box.restclientv2.exceptions.BoxRestException;
import com.box.restclientv2.requestsbase.BoxDefaultRequestObject;
import com.box.restclientv2.requestsbase.CreateMetadataRequestObject;
import com.box.restclientv2.requestsbase.MetadataJSONPatchRequestObject;

/**
 * BoxFileMetadataManager, which can be used to make API calls on file metadata endpoints.
 */
public class BoxFileMetadataManagerImpl extends AbstractBoxResourceManager implements IBoxFileMetadataManager {

    /**
     * BoxFileMetadataManager, which can be used to make API calls on file metadata endpoints.
     * 
     * @param config
     * @param resourceHub
     * @param parser
     * @param auth
     * @param restClient
     */
    public BoxFileMetadataManagerImpl(IBoxConfig config, IBoxResourceHub resourceHub, IBoxJSONParser parser, IBoxRequestAuth auth, IBoxRESTClient restClient) {
        super(config, resourceHub, parser, auth, restClient);
    }

    @Override
    protected Class<? extends Object> getClassFromType(final IBoxType type) {
        if (type == BoxMetadataType.FILE_METADATA) {
            return BoxFileMetadata.class;
        }
        return Object.class;
    }

    @Override
    public BoxFileMetadata getMetadata(String fileId, String metadataType, BoxDefaultRequestObject requestObject) throws BoxRestException,
        AuthFatalFailureException, BoxServerException {
        GetFileMetadataRequest request = new GetFileMetadataRequest(getConfig(), getJSONParser(), fileId, metadataType, requestObject);
        return (BoxFileMetadata) getResponseAndParseAndTryCast(request, BoxMetadataType.FILE_METADATA, getJSONParser());
    }

    @Override
    public BoxFileMetadata createMetadata(String fileId, String metadataType, CreateMetadataRequestObject requestObject) throws BoxRestException,
        AuthFatalFailureException, BoxServerException {
        CreateFileMetadataRequest request = new CreateFileMetadataRequest(getConfig(), getJSONParser(), fileId, metadataType, requestObject);
        return (BoxFileMetadata) getResponseAndParseAndTryCast(request, BoxMetadataType.FILE_METADATA, getJSONParser());
    }

    @Override
    public BoxFileMetadata createOrUpdateMetadata(String fileId, String metadataType, Map<String, String> keyValues) throws BoxRestException,
        AuthFatalFailureException, BoxServerException {
        try {
            return createMetadata(fileId, metadataType, keyValues);
        }
        catch (BoxServerException e) {
            int status = e.getStatusCode();
            if (status == HttpStatus.SC_CONFLICT) {
                return updateMetadata(fileId, metadataType, keyValues);
            }
            else {
                throw e;
            }
        }
    }

    @Override
    public BoxFileMetadata createMetadata(String fileId, String metadataType, Map<String, String> keyValues) throws BoxRestException,
        AuthFatalFailureException, BoxServerException {
        CreateMetadataRequestObject obj = CreateMetadataRequestObject.createMetadataRequestObject(keyValues);
        return createMetadata(fileId, metadataType, obj);
    }

    @Override
    public BoxFileMetadata updateMetadata(String fileId, String metadataType, Map<String, String> keyValues) throws BoxRestException,
        AuthFatalFailureException, BoxServerException {
        MetadataJSONPatchRequestObject obj = MetadataJSONPatchRequestObject.batchOperation();
        for (Map.Entry<String, String> entry : keyValues.entrySet()) {
            obj.appendAddOperation(entry.getKey(), entry.getValue());
        }
        return executeBatchMetadataRequest(fileId, metadataType, obj);
    }

    @Override
    public BoxFileMetadata updateMetadata(String fileId, String metadataType, String key, String value) throws BoxRestException, AuthFatalFailureException,
        BoxServerException {
        MetadataJSONPatchRequestObject obj = MetadataJSONPatchRequestObject.addOperation(key, value);
        return executeBatchMetadataRequest(fileId, metadataType, obj);
    }

    @Override
    public BoxFileMetadata replaceMetadata(String fileId, String metadataType, String key, String value) throws BoxRestException, AuthFatalFailureException,
        BoxServerException {
        MetadataJSONPatchRequestObject obj = MetadataJSONPatchRequestObject.replaceOperation(key, value);
        return executeBatchMetadataRequest(fileId, metadataType, obj);
    }

    @Override
    public BoxFileMetadata deleteMetadata(String fileId, String metadataType, String key) throws BoxRestException, AuthFatalFailureException,
        BoxServerException {
        MetadataJSONPatchRequestObject obj = MetadataJSONPatchRequestObject.removeOperation(key);
        return executeBatchMetadataRequest(fileId, metadataType, obj);
    }

    @Override
    public BoxFileMetadata testMetadata(String fileId, String metadataType, String key, String value) throws BoxRestException, AuthFatalFailureException,
        BoxServerException {
        MetadataJSONPatchRequestObject obj = MetadataJSONPatchRequestObject.testOperation(key, value);
        return executeBatchMetadataRequest(fileId, metadataType, obj);
    }

    @Override
    public BoxFileMetadata executeBatchMetadataRequest(String fileId, String metadataType, MetadataJSONPatchRequestObject requestObject)
        throws BoxRestException, AuthFatalFailureException, BoxServerException {
        PutFileMetadataRequest request = new PutFileMetadataRequest(getConfig(), getJSONParser(), fileId, metadataType, requestObject);
        return (BoxFileMetadata) getResponseAndParseAndTryCast(request, BoxMetadataType.FILE_METADATA, getJSONParser());
    }
}
