package model.api.chapter;


import model.api.verse.Verse;

/**
 * Created by vahidoo on 3/11/15.
 */
public interface Chapter {


    Object getName();
    Integer getIndex();
    Verse getVerse(int verse);

    Chapter getNextChapter();
}
