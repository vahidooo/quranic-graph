package model.api.block;

import model.api.verse.Verse;

/**
 * Created by vahidoo on 3/17/15.
 */
public interface VerseBlock {
    Verse getStartVerse();
    Verse getEndVerse();
    int getVerseCount();
}
