/**
 *
 */
package com.wuhulala.websocket.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import com.wuhulala.websocket.base.BizException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Json实用类，扩展封装Jackson. 
 * 整合了com.ccps.core中的JsonUtils
  * @author zjhua
 *  @version 1.0.0
 */
public class JsonUtils {

    private static final Logger log = Logger.getLogger(JsonUtils.class);

    final static ObjectMapper objectMapper;

    /**
     * 是否打印美观格式
     */
    static boolean isPretty = false;

    static {
		DefaultSerializerProvider sp = new DefaultSerializerProvider.Impl();
        sp.setNullValueSerializer(NullSerializer.instance);
        objectMapper = new ObjectMapper(null, sp, null);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
    /**
     * JSON串转换为Java泛型对象，可以是各种类型，此方法最为强大。用法看测试用例。
     * @param <T>
     * @param jsonString JSON字符串
    * @param tr TypeReference,例如: new TypeReference< List<FamousUser> >(){}
     * @return List对象列表
     */
    @SuppressWarnings("unchecked")
	public static <T> T json2GenericObject(String jsonString, TypeReference<T> tr) {

        if (jsonString == null || "".equals(jsonString)) {
            return null;
        } else {
            try {
                return (T) objectMapper.readValue(jsonString, tr);
            } catch (Exception e) {
                log.warn("json error:" + e.getMessage());
            }
        }
        return null;
    }

    /**
     * Java对象转Json字符串
     *
     * @param object Java对象，可以是对象，数组，List,Map等
     * @return json 字符串
     */
    public static String toJson(Object object) {
        return toJson(object, isPretty);
    }

	public static String toJson(Object object, boolean pretty) {
		String jsonString = "";
		try {
			if (pretty) {
				jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
			} else {
				jsonString = objectMapper.writeValueAsString(object);
			}
		} catch (Exception e) {
			log.warn("json error:" + e.getMessage());
		}
		return jsonString;
	}

	public static void printJson(Object object) {
    	printJson(object, isPretty);
	}

	public static void printJson(Object object, boolean pretty) {
		System.out.println(toJson(object, pretty));
	}

    /**
     * Json字符串转Java对象
     *
     * @param jsonString
     * @param c
     * @return
     */
    public static Object json2Object(String jsonString, Class<?> c) {
        if (jsonString == null || "".equals(jsonString)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(jsonString, c);
            } catch (Exception e) {
                log.warn("json error:" + e.getMessage(), e);
            }
        }
        return null;
    }

    public static <T> List<T> json2List(String jsonString, Class<T> clazz){
    	List<T> list = null;
        try {
			list = objectMapper.readValue(jsonString, getCollectionType(List.class, clazz));
		} catch (JsonParseException e) {
        	log.warn("json error:" + e.getMessage());
        } catch (JsonMappingException e) {
        	log.warn("json error:" + e.getMessage());
        } catch (IOException | JSONException e) {
        	log.warn("json error:" + e.getMessage());
        }
		return list;
    }

	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	public static <T> List<T> json2ListAppointed (String content, Class<T> clazz){
    	JavaType valueType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
    	try {
			return objectMapper.readValue(content, valueType);
		} catch (JsonParseException e) {
			log.error("json error:" + e.getMessage());
		} catch (JsonMappingException e) {
			log.error("json error:" + e.getMessage());
		} catch (IOException e) {
			log.error("json error:" + e.getMessage());
		}
    	return null;
    }

    public static String getNodeJson(String content, String nodeName){
		try {
			JsonNode nodes = objectMapper.readTree(content);
			return nodes.get(nodeName).toString();
		} catch (IOException e) {
			log.error("json error:" + e.getMessage());
		}
		return null;
    }
    
    private static final JsonConverter converter = new JackJsonConverter();

	/**
	 * 序列化,即对象转成json字符串.
	 * 
	 * @param value
	 * @return
	 * 
	 */
	public static String serialize(Object value) {
		return converter.serialize(value);
	}

	/**
	 * 反序列化,即json字符串转成对象
	 * 
	 * @param json
	 * @param valueType
	 *            对象类型
	 * @return
	 * 
	 */
	public static <T> T deserialize(String json, Class<T> valueType) {
		return converter.deserialize(json, valueType);
	}

	/** 功能说明: Json数据转换器接口 */
	public interface JsonConverter {

		/**
		 * 序列化,即对象转成json字符串.
		 * 
		 * @param value
		 * @return
		 * 
		 */
		String serialize(Object value);

		/**
		 * 反序列化,即json字符串转成对象
		 * 
		 * @param json
		 * @param valueType
		 *            对象类型
		 * @return
		 * 
		 */
		<T> T deserialize(String json, Class<T> valueType);
	}

	/**
	 * 功能说明: 使用字段名作为json字符串中键值策略.<br>
	 * 注意事项: jackjson默认策略是直接从get\set方法去掉前缀后,取接下来的大写字母(如果存在连续大写,则一并转换)转换成小写.<br>
	 * 例如,实际字段名为aBc,按照标准JavaBean规范,对应存在getABc\setABc方法,这时下面的defaultName是abc.<br>
	 * 系统版本: v1.0<br>
	 * 开发人员: wangkx07777@hundsun.com<br>
	 * 开发时间: 2014-4-4<br>
	 */
	static class UseFieldNameStrategy extends PropertyNamingStrategy {

		@Override
		public String nameForGetterMethod(MapperConfig<?> config,
										  AnnotatedMethod method, String defaultName) {
			return nameForMethod(method, defaultName, "get");
		}

		@Override
		public String nameForSetterMethod(MapperConfig<?> config,
				AnnotatedMethod method, String defaultName) {
			return nameForMethod(method, defaultName, "set");
		}

		private String nameForMethod(AnnotatedMethod method,
				String defaultName, String methodNamePrefix) {
			String fieldName = method.getName().replaceFirst(methodNamePrefix,
					"");
			if (fieldName.length() >= 2) {
				StringBuilder result = new StringBuilder(fieldName);
				// 如果第1个是大写并且第2个也是大写,则第一个转为小写
				if (isUpperCase(result.charAt(0))
						&& isUpperCase(result.charAt(1))) {
					result.setCharAt(0, Character.toLowerCase(result.charAt(0)));
					defaultName = result.toString();
				}
			}
			return defaultName;
		}

		private boolean isUpperCase(char c) {
			return Character.isUpperCase(c);
		}

	}

	/** JackJson转换器 */
	static class JackJsonConverter implements JsonConverter {
		/** 全局变量,线程安全,默认不过滤null属性.*/
		private static final ObjectMapper MAPPER = new ObjectMapper();

		static {
			// 忽略未知属性
			MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			// 允许字段名没有引号
			MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			// 序列化时不输出null值的键值对.
			MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			// 定制字段名策略
			MAPPER.setPropertyNamingStrategy(new UseFieldNameStrategy());
		}

		@Override
		public String serialize(Object value) {
			try {
				return MAPPER.writeValueAsString(value);
			} catch (Exception e) {
				log.error("要序列化的对象：" + value.toString());
				throw new BizException(-1, e, "序列化失败");
			}
		}

		@Override
		public <T> T deserialize(String json, Class<T> valueType) {
			try {
				return MAPPER.readValue(json, valueType);
			} catch (Exception e) {
				log.error("要反序列化的字符串：" + json);
				throw new BizException(-1, e, "反序列化失败");
			}
		}

	}

	/** FastJson转换器 */
	static class FastJsonConverter implements JsonConverter {

		@Override
		public String serialize(Object value) {
			try {
				return JSON.toJSONString(value);
			} catch (Exception e) {
				log.error("要序列化的对象：" + value.toString());
				throw new BizException(-1, e, "序列化失败");
			}
		}

		@Override
		public <T> T deserialize(String json, Class<T> valueType) {
			try {
				return JSON.parseObject(json, valueType);
			} catch (Exception e) {
				log.error("要反序列化的字符串：" + json);
				throw new BizException(-1, e, "反序列化失败");
			}
		}
	}
}