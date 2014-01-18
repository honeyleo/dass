package com.huizhi.dass.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


/**
 * json工具类
 * base on jackson
 * @author Jiang Shu
 *
 */
public class JsonUtils {
	
	
	protected static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		//写json
		// (without this setting, an exception is thrown in those cases)
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		
		//读json
		// to prevent exception when encountering unknown property:
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// to allow coercion of JSON empty String ("") to null Object value:
		mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
	}
	
	/**
	 * bean to json
	 * 
	 * @param bean
	 * @return
	 */
	public static String beanToJson(Object bean) {
		try {
			return mapper.writeValueAsString(bean);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

	
	/**
	 * jackson转换json to object
	 * 
	 * @param json
	 * @param pojoClass
	 * @return
	 */
	public static <T> T jsonToBean(String json, Class<T> pojoClass) {
		if (json == null || !(json.startsWith("{") && json.endsWith("}"))) {
			return null;
		}
		T obj = null;
		try {
			obj = (T) mapper.readValue(json, pojoClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return obj;
	}
	
	/**
	 * json to list
	 * @param json
	 * @return
	 */
	public static <T> List<T> jsonToList(String json) {
		List<T> list = new ArrayList<T>();
		try {
			list = mapper.readValue(json, new TypeReference<List<T>>() {});
		}  catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	/**
	 * json to map
	 * @param json
	 * @return
	 */
	public static <K,V> Map<K,V> jsonToMap(String json) {
		Map<K, V> map = null;
		try {
			map = mapper.readValue(json, new TypeReference<Map<K,V>>() {});
		}  catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * json 二级节点转换bean
	 * @param json
	 * @param node
	 * @param clazz
	 * @return
	 */
	public static <T> T jsonNodeToBean(String json, String node, Class<T> clazz) {
		T bean = null;
		try {
			ObjectNode root = (ObjectNode) mapper.readTree(json);
			bean = mapper.readValue(root.get(node).toString(), clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bean;
	}

	/**
	 * json 二级节点转换List
	 * @param json
	 * @param node
	 * @return
	 */
	public static <T> List<T> jsonNodeToList(String json, String node) {
		List<T> list = new ArrayList<T>();
		try {
			ObjectNode root = (ObjectNode) mapper.readTree(json);
			list = mapper.readValue(root.get(node).toString(), new TypeReference<List<T>>() {});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	/**
	 * json集合 二级节点 转换bean
	 * @param json
	 * @param index
	 * @param clazz
	 * @return
	 */
	public static <T> T jsonArrayNodeToBean(String json, int index, Class<T> clazz) {
		T bean = null;
		try {
			ArrayNode root = (ArrayNode) mapper.readTree(json);
			JsonNode node = root.get(index);
			bean = mapper.readValue(node.toString(), clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	
	
 	
	public static boolean checkJsonObject(String json) {
		if (json.startsWith("{") && json.endsWith("}")) {
			return true;
		}
		return false;
	}

	public static boolean checkJsonList(String json) {
		if (json.startsWith("[") && json.endsWith("]")) {
			return true;
		}
		return false;
	}



	public static ObjectMapper getMapper() {
		return mapper;
	}
	

}
