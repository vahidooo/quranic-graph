package alg.base;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 * Created by vahidoo on 3/24/15.
 */

@JsonPropertyOrder(value = "score,object")
public class Scored<T,N extends Comparable> {
    private T object;
    private N score;

    public Scored(T object, N score) {
        this.object = object;
        this.score = score;
    }

    public T getObject() {
        return object;
    }


    public N getScore() {
        return score;
    }
}
