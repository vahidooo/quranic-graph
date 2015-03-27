package model.api.word;

import model.api.token.Token;
import model.api.verse.Verse;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by vahidoo on 3/11/15.
 */
public interface Word {
    @JsonIgnore
    Word getSuccessor();

    @JsonIgnore
    Word getPredecessor();

    @JsonIgnore
    Token getStem();

    @JsonIgnore
    Verse getVerse();

    @JsonIgnore
    int getIndexInQuran();

    @JsonIgnore
    Word getSuccessorInQuran();

    String getAddress();
}
