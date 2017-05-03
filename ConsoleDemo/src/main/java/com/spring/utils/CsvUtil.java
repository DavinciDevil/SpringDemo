package com.spring.utils;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class CsvUtil {

    private static final CsvMapper csvMapper = new CsvMapper();

    private CsvUtil(){

    }

    /**
     * 输出全部属性
     * 如果csv中存在，对象中没有，则自动忽略该属性
     * 失败返回null
     *
     * @param csvContent
     * @param clazz
     * @return
     */
    public static <T> List<T> toNormalObject(String csvContent, Class<T> clazz) {
        try {
            CsvSchema schema = CsvSchema.emptySchema().withHeader();
            MappingIterator<T> mappingIterator = csvMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).readerFor(clazz).with(schema).readValues(csvContent);
            return mappingIterator.readAll();
        } catch (Exception e) {
             System.out.println("CsvToObject failed:"+e);
        }
        return null;
    }

    /**
     * 输出全部属性
     * 如果csv中存在，对象中没有，则自动忽略该属性
     * 失败返回null
     * @param bytes
     * @param clazz
     * @return
     */
    public synchronized static <T> List<T> toNormalObject(byte[] bytes, Class<T> clazz) {
        try {
            CsvSchema schema = CsvSchema.emptySchema().withHeader();
            MappingIterator<T> mappingIterator = csvMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).readerFor(clazz).with(schema).readValues(bytes);
            return mappingIterator.readAll();
        } catch (Exception e) {
             System.out.println("CsvToObject failed:"+e);
        }
        return null;
    }

    /**
     * 输出全部属性 失败返回""
     *
     * @param object
     * @return
     */
    public static String toNormalCsv(Object object) {
        return objectToCsv(Include.ALWAYS, object);
    }

    private static <T> String objectToCsv(Include include, T object) {
        try {
            CsvSchema csvSchema = csvMapper.schemaFor(object.getClass());
            return csvMapper.setSerializationInclusion(include).writer(csvSchema).writeValueAsString(object);
        } catch (JsonProcessingException e) {
             System.out.println("ObjToCsv failed:"+e);
        }
        return "";
    }

}