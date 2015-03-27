package server.ext.quran.ws;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
* Created by vahidoo on 3/26/15.
*/
@Provider
public class MyConverterProvider implements ParamConverterProvider {
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        System.out.println( ":::::::::::::::::::::");
        if (rawType.equals(String.class)) {
            return (ParamConverter<T>) new MyConverter();
        } else {
            return null;
        }
    }

    private class MyConverter implements ParamConverter<String> {
        @Override
        public String fromString(String value) {
            return value.replaceAll("ی", "ي");
        }

        @Override
        public String toString(String value) {
            return value;
        }
    }
}
