package model.impl.token;

import model.api.base.Factory;
import model.api.base.Session;
import model.api.token.Token;
import org.neo4j.graphdb.Node;

/**
 * Created by vahidoo on 3/27/15.
 */
public class TokenFactory implements Factory<Token> {

    private Session session;

    public TokenFactory(Session session) {
        this.session = session;
    }

    @Override
    public Token create(Node node) {
        return new TokenImpl(node,session);
    }
}
