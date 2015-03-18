package model.api.word;

import model.api.token.Token;
import model.api.verse.Verse;

/**
 * Created by vahidoo on 3/11/15.
 */
public interface Word {
    Word getSuccessor();
    Word getPredecessor();

    Token getStem();
    Verse getVerse();

    int getIndexInQuran();

    Word getSuccessorInQuran();

    String getAddress();
}
