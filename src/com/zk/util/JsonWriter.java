package com.zk.util;

import java.util.Collection;
import java.util.Map;

/**
 * Tiny zero-dependency JSON serializer.
 * Handles String, Number, Boolean, Map, Collection, and null.
 * Good enough for emitting API responses without pulling in Jackson/Gson.
 */
public final class JsonWriter {

    private JsonWriter() {}

    public static String write(Object value) {
        StringBuilder sb = new StringBuilder();
        append(sb, value);
        return sb.toString();
    }

    private static void append(StringBuilder sb, Object value) {
        if (value == null) {
            sb.append("null");
        } else if (value instanceof String) {
            appendString(sb, (String) value);
        } else if (value instanceof Number || value instanceof Boolean) {
            sb.append(value.toString());
        } else if (value instanceof Map) {
            appendMap(sb, (Map<?, ?>) value);
        } else if (value instanceof Collection) {
            appendCollection(sb, (Collection<?>) value);
        } else if (value.getClass().isArray()) {
            sb.append("[");
            int len = java.lang.reflect.Array.getLength(value);
            for (int i = 0; i < len; i++) {
                if (i > 0) sb.append(",");
                append(sb, java.lang.reflect.Array.get(value, i));
            }
            sb.append("]");
        } else {
            // Fallback: toString as string
            appendString(sb, value.toString());
        }
    }

    private static void appendMap(StringBuilder sb, Map<?, ?> map) {
        sb.append("{");
        boolean first = true;
        for (Map.Entry<?, ?> e : map.entrySet()) {
            if (!first) sb.append(",");
            first = false;
            appendString(sb, String.valueOf(e.getKey()));
            sb.append(":");
            append(sb, e.getValue());
        }
        sb.append("}");
    }

    private static void appendCollection(StringBuilder sb, Collection<?> col) {
        sb.append("[");
        boolean first = true;
        for (Object o : col) {
            if (!first) sb.append(",");
            first = false;
            append(sb, o);
        }
        sb.append("]");
    }

    private static void appendString(StringBuilder sb, String s) {
        sb.append('"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n");  break;
                case '\r': sb.append("\\r");  break;
                case '\t': sb.append("\\t");  break;
                case '\b': sb.append("\\b");  break;
                case '\f': sb.append("\\f");  break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append('"');
    }
}
