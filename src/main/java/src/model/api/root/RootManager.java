package model.api.root;

/**
 * Created by vahidoo on 3/13/15.
 */
public interface RootManager {
    Root getRootByArabic(String arabic);

    Iterable<Root> getAll();
}
