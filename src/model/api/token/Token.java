package model.api.token;

import model.api.base.Textual;
import model.api.root.Root;
import model.api.verse.Verse;
import model.api.word.Word;

/**
 * Created by vahidoo on 3/11/15.
 */
public interface Token extends Textual {

    Word getWord();


    boolean hasRoot();
    Root getRoot();

    TokenPosition getPosition();
    Object getIndex();


    Verse getVerse();
}

