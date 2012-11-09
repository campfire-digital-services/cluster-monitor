package au.com.permeance.clusterMonitor.util;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.KeyValuePair;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.valueOf;

public class JSONObjectUtil {

    public static List<KeyValuePair> asKeyValues(final JSONObject jsonObject) {
        final List<KeyValuePair> keyValues = new LinkedList<KeyValuePair>();

        final JSONArray names = jsonObject.names();
        for (int i = 0; i < names.length(); i++) {
            final String name = names.getString(i);
            if (hasStringProperty(jsonObject, name)) {
                keyValues.add(new KeyValuePair(name, jsonObject.getString(name)));
            }
            else if (hasLongProperty(jsonObject, name)) {
                keyValues.add(new KeyValuePair(name, valueOf(jsonObject.getLong(name))));
            }
            else if (hasDoubleProperty(jsonObject, name)) {
                keyValues.add(new KeyValuePair(name, valueOf(jsonObject.getDouble(name))));
            }
            else if (hasIntProperty(jsonObject, name)) {
                keyValues.add(new KeyValuePair(name, valueOf(jsonObject.getInt(name))));
            }
            else if (hasBooleanProperty(jsonObject, name)) {
                keyValues.add(new KeyValuePair(name, valueOf(jsonObject.getBoolean(name))));
            }
        }

        Collections.sort(keyValues, new Comparator<KeyValuePair>() {
            @Override
            public int compare(final KeyValuePair o1, final KeyValuePair o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        return keyValues;
    }

    protected static boolean hasStringProperty(final JSONObject jsonObject,
                                               final String name) {
        return jsonObject.getString(name, null) != null;
    }

    protected static boolean hasLongProperty(final JSONObject jsonObject,
                                             final String name) {
        return !(jsonObject.getLong(name, Long.MIN_VALUE) == Long.MIN_VALUE
                 && jsonObject.getLong(name, Long.MAX_VALUE) == Long.MAX_VALUE);
    }

    protected static boolean hasDoubleProperty(final JSONObject jsonObject,
                                               final String name) {
        return !(jsonObject.getDouble(name, Double.MIN_VALUE) == Double.MIN_VALUE
                 && jsonObject.getDouble(name, Double.MAX_VALUE) == Double.MAX_VALUE);
    }

    protected static boolean hasIntProperty(final JSONObject jsonObject,
                                            final String name) {
        return !(jsonObject.getLong(name, Integer.MIN_VALUE) == Integer.MIN_VALUE
                 && jsonObject.getLong(name, Integer.MAX_VALUE) == Integer.MAX_VALUE);
    }

    protected static boolean hasBooleanProperty(final JSONObject jsonObject,
                                                final String name) {
        return !(jsonObject.getBoolean(name, true)
                 && !jsonObject.getBoolean(name, false));
    }

}
