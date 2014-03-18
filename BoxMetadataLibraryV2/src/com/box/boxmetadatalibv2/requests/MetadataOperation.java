package com.box.boxmetadatalibv2.requests;

import java.util.HashMap;

import com.box.boxjavalibv2.exceptions.BoxJSONException;
import com.box.boxjavalibv2.jsonentities.MapJSONStringEntity;
import com.box.boxjavalibv2.jsonparsing.IBoxJSONParser;
import com.box.boxmetadatalibv2.utils.BoxMetadataUtils;

public class MetadataOperation extends MapJSONStringEntity {

    private static final long serialVersionUID = 1L;

    public static enum Operation {
        ADD, REMOVE, TEST, REPLACE;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    // Static map for performance consideration. String.toLowerCase() is slow.
    private static HashMap<String, Operation> operationMap = new HashMap<String, Operation>();
    static {
        for (Operation op : Operation.values()) {
            operationMap.put(op.toString(), op);
        }
    }

    public static final String OP = "op";
    public static final String PATH = "path";
    public static final String VALUE = "value";

    public MetadataOperation() {
        // For jackson parser
    }

    public MetadataOperation(Operation operation, String key, String value) {
        setOperation(operation);
        setKey(key);
        setValue(value);
    }

    public Operation getOperation() {
        return operationMap.get(get(OP));
    }

    // For jackson parser
    protected void setOperation(Operation operation) {
        put(OP, operation.toString());
    }

    public String getKey() {
        return getKeyFromPath((String) get(PATH));
    }

    // For jackson parser
    protected void setKey(String key) {
        put(PATH, createPath(key));
    }

    public String getValue() {
        return (String) get(VALUE);
    }

    // For jackson parser
    protected void setValue(String value) {
        put(VALUE, value);
    }

    @Override
    public String toJSONString(IBoxJSONParser parser) throws BoxJSONException {
        if (containsKey(VALUE) && getValue() == null) {
            remove(VALUE);
        }
        return super.toJSONString(parser);
    }

    private static String createPath(String key) {
        return BoxMetadataUtils.SLASH + BoxMetadataUtils.escapeJSONPointerKey(key);
    }

    private static String getKeyFromPath(String path) {
        return path.substring(1);
    }

}
