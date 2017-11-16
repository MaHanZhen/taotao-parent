package com.taotao.sso.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.MappingJacksonValue;

public class CallBackUtil {

    public static Object jsonpCallBack(String callback,Object object){
        if(StringUtils.isEmpty(callback)){
            return object;
        }else{
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(object);
            mappingJacksonValue.setJsonpFunction(callback);
            return  mappingJacksonValue;
        }
    }
}
