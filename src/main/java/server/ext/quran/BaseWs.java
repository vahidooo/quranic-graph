package server.ext.quran;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by vahidoo on 3/20/15.
 */
public class BaseWs {


    public String getJson(String objName, Object obj) {

//        List<SimpleClassBasedVisibility> visibilities = getPropertyGroupBasedClassBasedVisibilities(product);

//        if (visibilities == null || visibilities.size() == 0) {
//            return null;
//        }
//        CustomVisibilityChecker visibilityChecker = new CustomVisibilityChecker(visibilities);

//        ObjectWriter objectMapper = createObjectMapper(visibilityChecker);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        String ret = getJson(objName, obj, mapper.writer());
        return ret;
    }

    protected String getJson(String objName, Object obj, ObjectWriter objectMapper) {

        try {
            String res = null;
            if (obj instanceof Map) {
                Map map = (Map) obj;
                List<String> items = new ArrayList<>();

                for (Object key : map.keySet()) {
                    Object value = map.get(key);
                    items.add(getJson(key.toString(), value));
                }
                res = concatJson(items);
            } else if (obj instanceof Collection || obj instanceof Object[]) {
                StringBuilder resBuilder = new StringBuilder("[");
                int i = 0;
                if (obj instanceof Collection) {
                    Collection objs = (Collection) obj;
                    if (objs.size() > 0) {

                        for (Object o : objs) {
                            if (i++ > 0) {
                                resBuilder.append(", ");
                            }
                            resBuilder.append(objectMapper.writeValueAsString(o));
                        }
                    }
                } else {
                    for (Object o : ((Object[]) obj)) {

                        if (i++ > 0) {
                            resBuilder.append(", ");
                        }
                        resBuilder.append(objectMapper.writeValueAsString(o));
                    }
                }
                resBuilder.append("]");
                res = resBuilder.toString();
            } else {
                res = objectMapper.writeValueAsString(obj);
            }
            return objName == null ? res : "\"" + objName + "\": " + res;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    protected String concatJson(String... strings) {
        StringBuilder builder = new StringBuilder(2000);    //TODO: magic number,
        builder.append("{");
        builder.append(strings[0]);
        for (int i = 1; i < strings.length; i++) {
            String s = strings[i];
            builder.append(", ");
            builder.append(s);
        }
        builder.append("}");

        String stemp = builder.toString();

        return stemp;
    }

    protected String concatJson(List<String> strings) {
        StringBuilder builder = new StringBuilder(2000);    //TODO: magic number,
        builder.append("{");
        builder.append(strings.get(0));
        for (int i = 1; i < strings.size(); i++) {
            String s = strings.get(i);
            builder.append(", ");
            builder.append(s);
        }
        builder.append("}");

        String stemp = builder.toString();

        return stemp;
    }

}
