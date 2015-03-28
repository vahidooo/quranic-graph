package model.impl.block;

import model.api.block.VerseBlock;
import model.api.verse.Verse;

/**
 * Created by vahidoo on 3/17/15.
 */
public class VerseBlockImpl implements VerseBlock {

    private Verse start;
    private Verse end;

    public VerseBlockImpl(Verse start, Verse end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Verse getStartVerse() {
        return start;
    }

    @Override
    public Verse getEndVerse() {
        return end;
    }

    @Override
    public int getVerseCount() {
        if (start.getChapter().equals(end.getChapter())) {
            return end.getIndex() - start.getIndex() + 1;
        } else {

        }
        return 0;
    }

    @Override
    public String getStartAddress() {
        return getStartVerse().getAddress();
    }

    @Override
    public String getEndAddress() {
        return getEndVerse().getAddress();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VerseBlock) {
            VerseBlock vb = (VerseBlock) obj;

            return vb.getStartVerse().equals(start) && vb.getEndVerse().equals(end);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return start.hashCode()+end.hashCode();
    }
}
