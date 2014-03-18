package com.box.boxmetadatalibv2.dao;

import java.util.HashMap;
import java.util.Map;

import com.box.boxjavalibv2.dao.BoxObject;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class BoxFileMetadata extends BoxObject {

    private static final String SYSTEM_ID = "$id";
    private static final String SYSTEM_TYPE = "$type";
    private static final String SYSTEM_PARENT = "$parent";

    @JsonIgnore
    public String getMetadataType() {
        return getValue(SYSTEM_TYPE);
    }

    @JsonIgnore
    public String getMetadataId() {
        return getValue(SYSTEM_ID);
    }

    @JsonIgnore
    public String getMetadataParent() {
        return getValue(SYSTEM_PARENT);
    }

    @Override
    public String getValue(String key) {
        return (String) super.getExtraData(key);
    }

    @JsonIgnore
    public Map<String, String> getAllMetadata() {
        Map<String, String> metadata = new HashMap<String, String>();
        Map<String, Object> extra = extraProperties();
        for (Map.Entry<String, Object> entry : extra.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String) {
                metadata.put(entry.getKey(), (String) value);
            }
        }
        return metadata;
    }
}
