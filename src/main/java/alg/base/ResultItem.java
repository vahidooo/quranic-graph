package alg.base;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 * Created by vahidoo on 3/24/15.
 */

@JsonPropertyOrder(value = "score,item")
public class ResultItem<T,N extends Comparable> {
    private T item;
    private N score;

    public ResultItem(T item, N score) {
        this.item = item;
        this.score = score;
    }

    public T getItem() {
        return item;
    }


    public N getScore() {
        return score;
    }
}
