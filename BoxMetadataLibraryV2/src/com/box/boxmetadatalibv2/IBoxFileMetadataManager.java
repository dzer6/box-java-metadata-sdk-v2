package com.box.boxmetadatalibv2;

import java.util.Map;

import com.box.boxjavalibv2.exceptions.AuthFatalFailureException;
import com.box.boxjavalibv2.exceptions.BoxServerException;
import com.box.boxjavalibv2.resourcemanagers.IBoxResourceManager;
import com.box.boxmetadatalibv2.dao.BoxFileMetadata;
import com.box.restclientv2.exceptions.BoxRestException;
import com.box.restclientv2.requestsbase.BoxDefaultRequestObject;
import com.box.restclientv2.requestsbase.CreateMetadataRequestObject;
import com.box.restclientv2.requestsbase.MetadataJSONPatchRequestObject;

public interface IBoxFileMetadataManager extends IBoxResourceManager {

    public final static String METADATA_TYPE_PROPERTIES = "properties";

    /**
     * Get metadata of a file.
     */
    public BoxFileMetadata getMetadata(String fileId, String metadataType, BoxDefaultRequestObject requestObject) throws BoxRestException,
        AuthFatalFailureException, BoxServerException;

    /**
     * Create metadata for a file under a metadata type
     * 
     * @param fileId
     *            id of the file
     * @param metadataType
     *            type of metadata, right now only supports BoxFileMetadataManager.METADATA_TYPE_PROPERTIES.
     * @param requestObject
     *            request object
     * @return
     * @throws BoxRestException
     * @throws AuthFatalFailureException
     * @throws BoxServerException
     */
    public BoxFileMetadata createMetadata(String fileId, String metadataType, CreateMetadataRequestObject requestObject) throws BoxRestException,
        AuthFatalFailureException, BoxServerException;

    /**
     * Create metadata for a file under a metadata type
     * 
     * @param fileId
     *            id of the file
     * @param metadataType
     *            type of metadata, right now only supports BoxFileMetadataManager.METADATA_TYPE_PROPERTIES.
     * @param keyValues
     *            key values of the metadata.
     */
    public BoxFileMetadata createMetadata(String fileId, String metadataType, Map<String, String> keyValues) throws BoxRestException,
        AuthFatalFailureException, BoxServerException;

    /**
     * Create or update metadata
     * 
     * @param fileId
     *            id of the file.
     * @param metadataType
     *            type of metadata, right now only supports BoxFileMetadataManager.METADATA_TYPE_PROPERTIES.
     * @param key
     *            key of the metadata.
     * @param value
     *            new value of the metadata.
     */
    public BoxFileMetadata updateMetadata(String fileId, String metadataType, String key, String value) throws BoxRestException, AuthFatalFailureException,
        BoxServerException;

    /**
     * Delete the metadata with given key.
     */
    public BoxFileMetadata deleteMetadata(String fileId, String metadataType, String key) throws BoxRestException, AuthFatalFailureException,
        BoxServerException;

    /**
     * Test whether the value of the key is the same as the input value. In case of failure, BoxServerException will be thrown.
     */
    public BoxFileMetadata testMetadata(String fileId, String metadataType, String key, String value) throws BoxRestException, AuthFatalFailureException,
        BoxServerException;

    /**
     * Execute batch operations.
     * 
     * @param fileId
     * @param metadataType
     *            type of metadata, right now only supports BoxFileMetadataManager.METADATA_TYPE_PROPERTIES.
     * @param requestObject
     *            FileMetadataRequestObject, this can contain multiple operations. Operations are executed in orders as they are appended.
     */
    public BoxFileMetadata executeBatchMetadataRequest(String fileId, String metadataType, MetadataJSONPatchRequestObject requestObject)
        throws BoxRestException, AuthFatalFailureException, BoxServerException;

}