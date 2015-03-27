package data.schema;

/**
 * Created by vahidoo on 3/6/15.
 */
public class NodeProperties {

    //TODO
    public class General {
        public static final String index = "index";
        public static final String value = "value";
        public static final String address = "address";
        public static final String indexInQuran = "indexInQuran";
    }

    public class GeneralText {
        public static final String arabic = "arabic";
        public static final String simpleArabic = "simpleArabic";
        public static final String buckwalter = "buckwalter";
        public static final String simpleBuckwalter = "simpleBuckwalter";
    }

    public class Chapter {
        public static final String index = General.index;
        public static final String order = "order";
        public static final String ayas = "ayas";
        public static final String name = "name";
        public static final String type = "type";
    }


    public class Verse {
        public static final String text = "text";
        public static final String index = General.index;
        public static final String address = General.address;
        public static final String indexInQuran = General.indexInQuran;
    }


    public class Token {
        public static final String index = General.index;
        public static final String indexInQuran = General.indexInQuran;
        public static final String address = General.address;
    }

    public class Word {
        public static final String indexInQuran = General.indexInQuran;
    }


    public class DataFiller {
        public static final String clazz = "clazz";
        public static final String state = "state";
        public static final String progress = "progress";
    }





}
