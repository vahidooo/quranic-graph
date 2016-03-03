package quran.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;

import java.util.List;

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

    @Query("START v=node({self}) MATCH (v)-[:WORD]->(w:Word) return w ORDER BY w.index")
    @JsonIgnore
    private List<Word> words;

    @Indexed
    private String address;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public int getWordsCount(){
        return words.size();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
