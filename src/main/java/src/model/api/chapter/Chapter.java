package model.api.chapter;


import model.api.verse.Verse;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by vahidoo on 3/11/15.
 */
public interface Chapter {

    String getName();
    Integer getIndex();

    @JsonIgnore
    Verse getVerse(int verse);
    @JsonIgnore
    Chapter getNextChapter();
}
