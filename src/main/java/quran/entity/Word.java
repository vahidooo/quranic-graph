package quran.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;

import java.util.List;

/**
 * Created by vahidoo on 3/1/16.
 */

@NodeEntity
public class Word {

    @GraphId
    @JsonIgnore
    private Long id;

    private int index;

    @RelatedTo(type = "WORD", direction = Direction.OUTGOING)
    private Verse verse;

    @Query("START w=node({self}) MATCH (w)-[:TOEKN]-(t:Token) return t ORDER BY t.index")
    private List<Token> tokens;

    @RelatedTo(direction = Direction.OUTGOING, type = "STEM")
    private Token stem;

    @Indexed
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Verse getVerse() {
        return verse;
    }

    public void setVerse(Verse verse) {
        this.verse = verse;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Token getStem() {
        return stem;
    }

    public void setStem(Token stem) {
        this.stem = stem;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getTokensCount(){
        return tokens.size();
    }

    public String getAddress() {
        return address;
    }

    public int getIndex() {
        return index;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
