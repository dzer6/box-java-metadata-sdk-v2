package com.box.boxmetadatalibv2.utils;

public class BoxMetadataUtils {

    public static final char SLASH = '/';
    public static final String JSONPOINTER_ESCAPED_SLASH = "~1";
    public static final char TILDE = '~';
    public static final String JSONPOINTER_ESCAPED_TILDE = "~0";

    /**
     * Escape <a href="http://tools.ietf.org/html/rfc6901">JSON pointer</a> String.
     */
    public static String escapeJSONPointerKey(String key) {
        StringBuilder sbr = new StringBuilder();
        int len = key.length();
        for (int i = 0; i < len; i++) {
            char c = key.charAt(i);
            if (c == SLASH) {
                sbr.append(JSONPOINTER_ESCAPED_SLASH);
            }
            else if (c == TILDE) {
                sbr.append(JSONPOINTER_ESCAPED_TILDE);
            }
            else {
                sbr.append(c);
            }
        }
        return sbr.toString();
    }
}
