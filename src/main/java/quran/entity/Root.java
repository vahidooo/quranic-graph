package quran.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;

import java.util.Set;

/**
 * Created by vahidoo on 3/3/16.
 */
@NodeEntity
public class Root {

    @GraphId
    @JsonIgnore
    private Long id;

    @Indexed
    private String form;

    @RelatedTo(type = "ROOT", direction = Direction.INCOMING)
    @JsonIgnore
    private Set<Token> tokens;

    @Query("START r=node({self}) MATCH (l)<-[:LEMMA]-(t:Token)-[:ROOT]->(r) return distinct l")
    @JsonIgnore
    private Set<Lemma> drivens;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public Set<Token> getTokens() {
        return tokens;
    }

    public void setTokens(Set<Token> tokens) {
        this.tokens = tokens;
    }

    public Set<Lemma> getDrivens() {
        return drivens;
    }

}
