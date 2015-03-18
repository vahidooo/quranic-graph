package server.repr;


import model.api.block.VerseBlock;

import java.util.Map;

/**
 * Created by vahidoo on 3/9/15.
 */
public class MapRepresentation extends Representation<Map> {


    @Override
    public String represent(Map map) {


        StringBuilder sb = new StringBuilder();

        for (Object key : map.keySet()) {
            Object value = map.get(key);

            String keyStr = key.toString();
            String valueStr = value.toString();
//            if ( key instanceof Representation )
//                keyStr = ((Representation) key).represent(key);

            //TODO
            if ( key instanceof VerseBlock)
                keyStr = new VerseBlockRepresentation().represent((VerseBlock) key);

            sb.append(keyStr);
            sb.append("\t");
            sb.append(valueStr);
            sb.append("\n");
        }


        return sb.toString();

    }
}
