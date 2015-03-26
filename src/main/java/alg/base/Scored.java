package alg.base;

/**
 * Created by vahidoo on 3/24/15.
 */
public class Scored<T> {
    private T object;
    private double score;

    public Scored(T object, Double score) {
        this.object = object;
        this.score = score;
    }

    public T getObject() {
        return object;
    }

    public Double getScore() {
        return score;
    }
}
