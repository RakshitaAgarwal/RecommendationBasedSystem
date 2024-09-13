package org.cafeteria.common.communicationProtocol;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class JSONSerializer {
    private static final Gson gson = new Gson();

    public static String serializeData(Object dataObject) {
        return gson.toJson(dataObject);
    }

    public static <K, V> String serializeMap(Map<K, V> map) {
        Type mapType = new TypeToken<Map<K, V>>() {
        }.getType();
        return gson.toJson(map, mapType);
    }

    public static <T> T deserializeData(String jsonData, Class<T> className) throws JsonSyntaxException {
        return gson.fromJson(jsonData, className);
    }

    public static <T> List<T> deserializeList(String jsonData, Class<T> className) throws JsonSyntaxException {
        Type type = TypeToken.getParameterized(List.class, className).getType();
        return gson.fromJson(jsonData, type);
    }

    public static <K, V> Map<K, V> deserializeMap(String json, Type typeOfMap) {
        return gson.fromJson(json, typeOfMap);
    }
}