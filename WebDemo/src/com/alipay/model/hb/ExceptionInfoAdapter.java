package com.alipay.model.hb;

import com.alipay.utils.Utils;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by liuyangkly on 15/8/27.
 */
public class ExceptionInfoAdapter implements JsonSerializer<List<ExceptionInfo>> {
    @Override
    public JsonElement serialize(List<ExceptionInfo> exceptionInfos, Type type, JsonSerializationContext jsonSerializationContext) {
        if (Utils.isListEmpty(exceptionInfos)) {
            return null;
        }
        Object [] objects = exceptionInfos.toArray();//自己添加的
        return new JsonPrimitive(StringUtils.join(objects, "|"));
    }
}