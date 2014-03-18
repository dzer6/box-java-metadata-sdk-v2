package com.box.boxmetadatalibv2;

import com.box.boxjavalibv2.IBoxConfig;
import com.box.boxjavalibv2.jsonparsing.IBoxJSONParser;
import com.box.boxjavalibv2.jsonparsing.IBoxResourceHub;
import com.box.boxjavalibv2.resourcemanagers.IPluginResourceManagerBuilder;
import com.box.restclientv2.IBoxRESTClient;
import com.box.restclientv2.authorization.IBoxRequestAuth;

public class BoxFileMetadataManagerBuilder implements IPluginResourceManagerBuilder {

    public static final String FILE_METADATA_MANAGER_KEY = BoxFileMetadataManagerBuilder.class.getName();

    @Override
    public IBoxFileMetadataManager build(IBoxConfig config, IBoxResourceHub resourceHub, IBoxJSONParser parser, IBoxRequestAuth auth, IBoxRESTClient restClient) {
        return new BoxFileMetadataManagerImpl(config, resourceHub, parser, auth, restClient);
    }
}
