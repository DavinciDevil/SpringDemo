package com.spring.utils;


import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {


    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 输出全部属性
     * 失败返回""
     * @param object
     * @return
     */
    public static String toNormalJson(Object object) {
        return toJson(Include.ALWAYS, object);
    }

    /**
     * 输出非空属性
     * 失败返回""
     * @param object
     * @return
     */
    public static String toNonNullJson(Object object) {
        return toJson(Include.NON_NULL, object);
    }

    /**
     * 输出非Null且非Empty(如List.isEmpty)的属性
     * 失败返回""
     * @param object
     * @return
     */
    public static String toNonEmptyJson(Object object) {
        return toJson(Include.NON_EMPTY, object);
    }

    /**
     * 转成Json
     * @param include
     * @param object
     * @return
     */
    private static String toJson(Include include, Object object) {
        try {
            objectMapper.setSerializationInclusion(include);
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
             System.out.println("Obj转Json报错："+ e);
        }
        return "";
    }

    /**
     * 输出全部属性
     * 如果json中存在，对象中没有，则自动忽略该属性
     * 失败返回null
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T toNormalObject(String json, Class<T> clazz) {
        return toObject(json, clazz);
    }

    /**
     * 转成Object
     * @param include
     * @param json
     * @param clazz
     * @return
     */
    private static <T> T toObject(String json, Class<T> clazz) {
        try {
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
             System.out.println("Json转Obj报错："+ e);
        }
        return null;
    }

}