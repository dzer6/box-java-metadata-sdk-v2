package com.box.boxmetadatalibv2;

import com.box.boxjavalibv2.BoxClient;
import com.box.boxjavalibv2.resourcemanagers.IResourceManagerPlugin;

public class BoxFileMetadataPlugin implements IResourceManagerPlugin {

    public static final String FILE_METADATA_MANAGER_KEY = BoxFileMetadataPlugin.class.getName();

    @Override
    public IBoxFileMetadataManager plugin(BoxClient client) {
        return (IBoxFileMetadataManager) client.pluginResourceManager(FILE_METADATA_MANAGER_KEY, new BoxFileMetadataManagerBuilder());
    }
}
