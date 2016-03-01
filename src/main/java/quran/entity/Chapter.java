package quran.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.List;

/**
 * Created by vahidoo on 3/11/15.
 */
@NodeEntity
public class Chapter {

    @GraphId
    @JsonIgnore
    private Long id;

    @Indexed
    private String name;
    @Indexed
    private int index;
    private int order;
    private ChapterType type;

    @RelatedTo( type = "VERSE", direction = Direction.OUTGOING)
    @JsonIgnore
    private List<Verse> verses;

//    @RelatedTo(direction = Direction.OUTGOING)
//    private List<Roku> rokus;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<Verse> getVerses() {
        return verses;
    }

    public void setVerses(List<Verse> verses) {
        this.verses = verses;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public ChapterType getType() {
        return type;
    }

    public void setType(ChapterType type) {
        this.type = type;
    }

//    String getName();
//    Integer getIndex();
//
//    @JsonIgnore
//    Verse getVerse(int verse);
//    @JsonIgnore
//    Chapter getNextChapter();


    public int getVersesCount(){
        return verses.size();
    }
}
