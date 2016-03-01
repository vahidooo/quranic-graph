package quran.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

/**
 * Created by vahidoo on 3/11/15.
 */
@NodeEntity
public class Verse {

    @GraphId
    @JsonIgnore
    private Long id;

    private int index;
    private String text;

    @RelatedTo(type = "VERSE" , direction = Direction.INCOMING)
    @JsonIgnore
    private Chapter chapter;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
