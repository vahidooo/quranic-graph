package model.impl.token;

import data.schema.NodeLabels;
import model.api.base.Session;
import model.api.token.Token;
import model.api.token.TokenManager;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.tooling.GlobalGraphOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vahidoo on 3/11/15.
 */
public class TokenManagerImpl implements TokenManager {

    private GraphDatabaseService database;
    private Session session;


    public TokenManagerImpl(Session session, GraphDatabaseService database) {
        this.database = database;
        this.session = session;
    }


    public Iterable<Token> getAll() {
        List<Token> tokens = new ArrayList<>();
        ResourceIterable<Node> nodes = GlobalGraphOperations.at(database).getAllNodesWithLabel(NodeLabels.TOKEN);

        for (Node node : nodes) {
            Token root = session.get(Token.class,node);
            tokens.add(root);
        }
        return tokens;
    }

}
