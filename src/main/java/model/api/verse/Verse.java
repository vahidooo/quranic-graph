package model.api.verse;

import model.api.base.NodeContainer;
import model.api.chapter.Chapter;
import model.api.word.Word;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.List;

/**
 * Created by vahidoo on 3/11/15.
 */
public interface Verse extends NodeContainer {

    String getAddress();
    Chapter getChapter();
    Integer getIndex();
    String getText();

    @JsonIgnore
    Word getWord(int i);

    @JsonIgnore
    Verse getSuccessor();

    @JsonIgnore
    List<Word> getWords();

    @JsonIgnore
    Verse getNextInQuran();

    @JsonIgnore
    int getIndexInQuran();

}
