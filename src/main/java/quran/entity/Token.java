package quran.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;

/**
 * Created by vahidoo on 3/3/16.
 */
@NodeEntity
public class Token {


    public enum TokenAffix {
        SUFFIX, PREFIX, STEM
    }

    @GraphId
    @JsonIgnore
    private Long id;

    private int index;
    private String tag;

    @Indexed
    private String form;
    @Indexed
    private String simple;

    @Indexed
    @GraphProperty(propertyType = String.class)
    private TokenAffix affix;
    @JsonIgnore
    @RelatedTo(direction = Direction.INCOMING, type = "TOKEN")
    private Word word;
    @RelatedTo(direction = Direction.OUTGOING, type = "ROOT")
    private Root root;
    @RelatedTo(direction = Direction.OUTGOING, type = "LEMMA")
    private Lemma lemma;

    @Indexed
    private String address;

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

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getSimple() {
        return simple;
    }

    public void setSimple(String simple) {
        this.simple = simple;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public TokenAffix getAffix() {
        return affix;
    }

    public void setAffix(TokenAffix affix) {
        this.affix = affix;
    }

    public Root getRoot() {
        return root;
    }

    public void setRoot(Root root) {
        this.root = root;
    }

    public Lemma getLemma() {
        return lemma;
    }

    public void setLemma(Lemma lemma) {
        this.lemma = lemma;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
