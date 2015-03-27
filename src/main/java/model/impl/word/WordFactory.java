package model.impl.word;

import model.api.base.Factory;
import model.api.base.Session;
import model.api.word.Word;
import org.neo4j.graphdb.Node;

/**
 * Created by vahidoo on 3/27/15.
 */
public class WordFactory implements Factory<Word> {

    private Session session;

    public WordFactory(Session session) {
        this.session = session;
    }

    @Override
    public Word create(Node node) {
        return new WordImpl(node,session);
    }
}
