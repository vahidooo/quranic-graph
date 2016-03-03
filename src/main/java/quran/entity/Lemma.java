package quran.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;

import java.util.Set;

/**
 * Created by vahidoo on 3/3/16.
 */
@NodeEntity
public class Lemma {

    @GraphId
    @JsonIgnore
    private Long id;

    @Indexed
    private String form;

    @Fetch
    @Query("START l=node({self}) MATCH (l)<-[:LEMMA]-(t:Token)-[:ROOT]->(r) return distinct r")
    private Set<Root> roots;

    @RelatedTo(type = "LEMMA" , direction = Direction.INCOMING)
    @JsonIgnore
    private Set<Token> tokens;
    private String simple;

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

    public Set<Root> getRoots() {
        return roots;
    }

    public Set<Token> getTokens() {
        return tokens;
    }

    public void setSimple(String simple) {
        this.simple = simple;
    }

    public String getSimple() {
        return simple;
    }
}
