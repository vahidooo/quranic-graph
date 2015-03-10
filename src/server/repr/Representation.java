package server.repr;

/**
 * Created by vahidoo on 3/9/15.
 */
public abstract class Representation<T> {
    abstract public String represent(T object);

    public String represent(Iterable<T> it) {
        StringBuilder sb = new StringBuilder();
        for (T object : it) {
            sb.append(represent(object));
            sb.append("\n");
        }
        return sb.toString();
    }
}
