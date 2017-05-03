package com.spring.utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlUtil {


    private static final XmlMapper xmlMapper = new XmlMapper();

    private XmlUtil() {

    }

    /**
     * 输出全部属性 如果xml中存在，对象中没有，则自动忽略该属性 失败返回null
     *
     * @param xmlContent
     * @param clazz
     * @return
     */
    public static <T> T toNormalObject(String xmlContent, Class<T> clazz) {
        return xmlToObject(xmlContent, clazz);
    }

    /**
     * 输出全部属性
     * 如果xml中存在，对象中没有，则自动忽略该属性
     * 失败返回null
     * @param
     * @param
     * @return
     */
    public static <T> T toNormalObject(byte[] bytes, Class<T> clazz) {
        return xmlToObject(bytes, clazz);
    }

    /**
     * 输出全部属性 失败返回""
     *
     * @param object
     * @return
     */
    public synchronized static byte[] toNormalXml(Object object) {
        return objectToXml(Include.ALWAYS, object);
    }

    private static <T> T xmlToObject(byte[] bytes, Class<T> clazz) {
        try {
            return xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).readValue(bytes, clazz);
        } catch (Exception e) {
           System.out.println("XmlToObject failed:"+ e);
        }
        return null;
    }

    private static <T> T xmlToObject(String xmlContent, Class<T> clazz) {
        try {
            return xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).readValue(xmlContent, clazz);
        } catch (Exception e) {
             System.out.println("XmlToObject failed:"+ e);
        }
        return null;
    }

    private static <T> byte[] objectToXml(Include include, T object) {
        try {
            return xmlMapper.setSerializationInclusion(include).writerWithDefaultPrettyPrinter().writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
             System.out.println("ObjToXml failed:"+ e);
        }
        return null;
    }

}