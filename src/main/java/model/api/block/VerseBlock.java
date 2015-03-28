package model.api.block;

import model.api.verse.Verse;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by vahidoo on 3/17/15.
 */
public interface VerseBlock {
    @JsonIgnore
    Verse getStartVerse();
    @JsonIgnore
    Verse getEndVerse();

    @JsonIgnore
    int getVerseCount();

    String getStartAddress();
    String getEndAddress();

}
