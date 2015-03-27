package alg.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by vahidoo on 3/28/15.
 */
public class ScoredList<T, N extends Comparable> extends ArrayList<Scored<T, N>> {

    public void descendSort() {
        Collections.sort(this, new Comparator<Scored<T, N>>() {
                    @Override
                    public int compare(Scored<T, N> o1, Scored<T, N> o2) {
                        return o2.getScore().compareTo(o1.getScore());
                    }
                }
        );
    }

    public void ascendSort() {
        Collections.sort(this, new Comparator<Scored<T, N>>() {
                    @Override
                    public int compare(Scored<T, N> o1, Scored<T, N> o2) {
                        return o1.getScore().compareTo(o2.getScore());
                    }
                }
        );

    }


}
