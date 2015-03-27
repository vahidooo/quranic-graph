package model.api.verse;

/**
 * Created by vahidoo on 3/27/15.
 */
public interface VerseManager {
    Verse get(int chapter, int verse);
    Verse get(String address);
}
