package model.impl.chapter;

import model.api.base.Factory;
import model.api.base.Session;
import model.api.chapter.Chapter;
import org.neo4j.graphdb.Node;

/**
 * Created by vahidoo on 3/27/15.
 */
public class ChapterFactory implements Factory<Chapter> {

    private Session session;

    public ChapterFactory(Session session) {
        this.session = session;
    }

    @Override
    public Chapter create(Node node) {
        return new ChapterImpl(node,session);
    }
}
