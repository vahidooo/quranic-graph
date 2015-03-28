package model.api.base;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by vahidoo on 3/13/15.
 */
public interface Textual {

    String getArabic();
    String getBuckwalter();

    @JsonIgnore
    String getSimpleArabic();
    @JsonIgnore
    String getSimpleBuckwalter();
}
