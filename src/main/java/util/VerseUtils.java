package util;

import model.api.root.Root;
import model.api.verse.Verse;
import model.api.word.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vahidoo on 3/21/15.
 */
public class VerseUtils {

    public static List<Root> getRootList(Verse verse) {
        List<Root> roots = new ArrayList<>();
        Word word = verse.getWord(1);
        while (word != null) {
            Root r = word.getStem().getRoot();
            if (r != null) {
                roots.add(r);
            }

            word = word.getSuccessor();
        }
        return roots;
    }


}
