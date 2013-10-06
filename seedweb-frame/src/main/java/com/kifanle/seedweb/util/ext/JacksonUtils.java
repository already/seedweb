package com.kifanle.seedweb.util.ext;

import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JacksonUtils {
	private static final Logger log = LoggerFactory.getLogger(JacksonUtils.class);
	
	public final static String dateFormat = "yyyy-MM-dd HH:mm:ss";
	
	private static final JsonFactory jsonFactory = new MappingJsonFactory();

	public static ObjectMapper getObjectMapper() {
		ObjectMapper om = (ObjectMapper) jsonFactory.getCodec();
		om.setDateFormat(new SimpleDateFormat(dateFormat));
		om.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);
		//om.setSerializationInclusion(Include.NON_NULL);
		return om;
	}
	
	public static ObjectMapper om = getObjectMapper();
	
	public static String obj2Json(Object obj){
		try {
			return om.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.error(e.toString());
		}
		return null;
	};
	
	public static <T> T json2Obj(String json,Class<T> clz){
		try {
			return om.readValue(json, clz); 
		} catch (Exception e) {
			log.error(e.toString());
		}
		return null;
	};

    public static int safeGetInt(Object value) {
        if (value == null) {
            return -1;
        } else {
            if (value instanceof Integer) {
                return ((Integer)value).intValue();
            } else if (value instanceof Long) {
                return ((Long)value).intValue();
            }
        }
        return 0;
    }
}
