package alg.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by vahidoo on 3/28/15.
 */
public class ResultList<T, N extends Comparable> extends ArrayList<ResultItem<T, N>> {

    protected N upperBound;
    protected N lowerBound;

    public ResultList() {
        super();
    }

    public ResultList(N upperBound, N lowerBound) {
        super();
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
    }

    public N getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(N upperBound) {
        this.upperBound = upperBound;
    }

    public N getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(N lowerBound) {
        this.lowerBound = lowerBound;
    }

    public List<ResultItem<T, N>> getItems() {
        return this;
    }

    public void descendSort() {
        Collections.sort(this, new Comparator<ResultItem<T, N>>() {
                    @Override
                    public int compare(ResultItem<T, N> o1, ResultItem<T, N> o2) {
                        return o2.getScore().compareTo(o1.getScore());
                    }
                }
        );
    }

    public void ascendSort() {
        Collections.sort(this, new Comparator<ResultItem<T, N>>() {
                    @Override
                    public int compare(ResultItem<T, N> o1, ResultItem<T, N> o2) {
                        return o1.getScore().compareTo(o2.getScore());
                    }
                }
        );

    }

}
