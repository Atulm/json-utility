package com.example.demo.utils;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.MapUtils.emptyIfNull;

/**
 * This utility is created to filter JSON based on the JSON Paths passing as a list in the argument
 */
public class JSONUtils {

    /**
     * It applies filters on  the input JSON and return a reduced JSON where attribute name matches with the JSON Path
     *
     * @param jsonDataSourceString
     * @param jsonPathList
     * @throws Exception
     */
    public static String filterJSON(String jsonDataSourceString, List<String> jsonPathList) throws Exception {
        Map<Object, Object> leftMap = jsonToMap(jsonDataSourceString);
        DocumentContext tempContext = JsonPath.parse(jsonDataSourceString);
        for (String jsonPath : jsonPathList) {
            JsonPath compiledPath = JsonPath.compile(jsonPath);
            tempContext.delete(compiledPath);
        }
        Map<Object, Object> rightMap = jsonToMap(tempContext.jsonString());
        MapDifference<Object, Object> difference = Maps.difference(leftMap, rightMap);
        Map<Object, MapDifference.ValueDifference<Object>> differenceMap = difference.entriesDiffering();
        Map<Object, Object> transformedMap = new HashMap<>(difference.entriesOnlyOnLeft());

        Map<Object, List<Map<Object, Object>>> nestedAttributeMap = emptyIfNull(differenceMap).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> findNestedAttributeDifferences(e)));
        emptyIfNull(nestedAttributeMap).entrySet().stream()
                .forEach(entry -> transformedMap.put(entry.getKey(), entry.getValue()));
        return mapToJson(transformedMap);

    }

    /**
     * Iterate over the nested dirty attributes which are holding a collection of objects and couldn't identified by the utility MAP Difference
     *
     * @param childEntrySet
     * @return Collection of required Object for nested attribute
     */
    private static List<Map<Object, Object>> findNestedAttributeDifferences(
            Map.Entry<Object, MapDifference.ValueDifference<Object>> childEntrySet) {
        final MapDifference.ValueDifference<Object> differenceMap = childEntrySet.getValue();
        List<Map<Object, Object>> leftSubMapList = new Gson()
                .fromJson(new Gson().toJson(differenceMap.leftValue()), new TypeToken<List<HashMap<Object, Object>>>() {
                }.getType());
        List<Map<Object, Object>> rightSubMapList = new Gson().fromJson(new Gson().toJson(differenceMap.rightValue()),
                new TypeToken<List<HashMap<Object, Object>>>() {
                }.getType());
        List<Map<Object, Object>> childAttributeList = new ArrayList<>();
        for (int i = 0; i < leftSubMapList.size(); i++) {
            Map<Object, Object> leftMapObj = leftSubMapList.get(i);
            Map<Object, Object> rightMapObj = rightSubMapList.get(i);
            Map<Object, Object> differenceSub = Maps.difference(leftMapObj, rightMapObj).entriesOnlyOnLeft();
            if (MapUtils.isNotEmpty(differenceSub)) {
                childAttributeList.add(differenceSub);
            }
        }
        return childAttributeList;
    }

    /**
     * Converts Map to JSON
     *
     * @param object
     * @return JSON String
     */
    public static String mapToJson(Map<Object, Object> object) {
        String json = new Gson().toJson(object);
        return json;
    }

    /**
     * Converts String to Map of Object
     *
     * @param json
     * @return
     */
    public static Map<Object, Object> jsonToMap(String json) {
        Map<Object, Object> jsonMap = new Gson().fromJson(json, new TypeToken<HashMap<Object, Object>>() {
        }.getType());
        return jsonMap;
    }
}
