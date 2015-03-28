package model.api.token;

import model.api.base.Textual;
import model.api.lemma.Lemma;
import model.api.root.Root;
import model.api.verse.Verse;
import model.api.word.Word;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by vahidoo on 3/11/15.
 */
public interface Token extends Textual {

    @JsonIgnore
    Word getWord();


    boolean hasLemma();
    Lemma getLemma();

    boolean hasRoot();

    @JsonIgnore
    Root getRoot();

    TokenPosition getPosition();

    @JsonIgnore
    Integer getIndex();

    String getAddress();


    @JsonIgnore
    Verse getVerse();

    @JsonIgnore
    int getChapterIndex();

//    int getIndexInQuran();
}

