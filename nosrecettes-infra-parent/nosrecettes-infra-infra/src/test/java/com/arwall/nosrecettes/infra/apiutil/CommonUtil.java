package com.arwall.nosrecettes.infra.apiutil;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtil {

    public static <T> T getObjectFromEntiy(HttpEntity entity, Class<T> valueType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        var createResponseBody = EntityUtils.toString(entity, "UTF-8");
        return mapper.readValue(createResponseBody, valueType);
    }

}
