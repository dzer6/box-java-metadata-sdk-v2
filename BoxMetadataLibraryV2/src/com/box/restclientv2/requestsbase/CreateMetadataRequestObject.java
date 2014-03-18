package com.box.restclientv2.requestsbase;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.box.boxjavalibv2.jsonparsing.IBoxJSONParser;
import com.box.restclientv2.exceptions.BoxRestException;

public class CreateMetadataRequestObject extends BoxDefaultRequestObject {

    public static final String JSON_CONTENT_TYPE = "application/json";

    private CreateMetadataRequestObject() {
    }

    public static CreateMetadataRequestObject createMetadataRequestObject(Map<String, String> keyValues) {
        CreateMetadataRequestObject obj = new CreateMetadataRequestObject();
        obj.setMetadata(keyValues);
        return obj;
    }

    private void setMetadata(Map<String, String> keyValues) {
        for (Map.Entry<String, String> entry : keyValues.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    HttpEntity getEntity(IBoxJSONParser parser) throws BoxRestException {
        try {
            StringEntity entity = (StringEntity) super.getEntity(parser);
            entity.setContentType(JSON_CONTENT_TYPE);
            return entity;
        }
        catch (Exception e) {
            throw new BoxRestException(e);
        }
    }
}
