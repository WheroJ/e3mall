package com.wheroj.common;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	public static String objectToJson(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String listToJson(List<?> list) {
		try {
			String objectToJson = objectMapper.writeValueAsString(list);
			JSONArray jsonArray = new JSONArray(objectToJson);
			JSONArray result = new JSONArray();
			for (int i = 0; i < jsonArray.length(); i++) {
				result.put(jsonArray.optJSONArray(i).optJSONObject(1));
			}
			return result.toString();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T> T jsonToObject(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
    /**
     * 将json数据转换成pojo对象list
     * <p>Title: jsonToList</p>
     * <p>Description: </p>
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
    	JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, beanType);
    	try {
    		List<T> list = objectMapper.readValue(jsonData, javaType);
    		return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    public static <T> T jsonToObject(String jsonData, TypeReference<T> typeReference) {
    	try {
    		T t = objectMapper.readValue(jsonData, typeReference);
    		return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    static {
    	objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    	objectMapper.configure(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
    	objectMapper.configure(Feature.ALLOW_COMMENTS, true);
    	objectMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
    	objectMapper.setSerializationInclusion(Include.NON_NULL);
    	objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    	objectMapper.enableDefaultTyping();
    }

	public static JSONArray turnJsonArray(String objectToJson) {
		JSONArray jsonArray = new JSONArray(objectToJson);
		JSONArray result = new JSONArray();
		for (int i = 0; i < jsonArray.length(); i++) {
			result.put(jsonArray.optJSONArray(i).optJSONObject(1));
		}
		return result;
	}
}
