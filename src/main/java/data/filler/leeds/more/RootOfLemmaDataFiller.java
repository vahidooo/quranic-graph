package data.filler.leeds.more;

import data.filler.DataFiller;
import data.filler.TransactionalFiller;
import data.schema.NodeLabels;
import data.schema.RelationshipTypes;
import model.impl.base.ManagersSet;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.tooling.GlobalGraphOperations;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by vahidoo on 3/29/15.
 */
public class RootOfLemmaDataFiller extends DataFiller {
    public RootOfLemmaDataFiller(GraphDatabaseService database, ManagersSet managersSet, Properties properties) {
        super(database, managersSet, properties);
    }

    @Override
    protected List<TransactionalFiller> getTransactionalFillers() throws Throwable {
        List<TransactionalFiller> fillers = new ArrayList<>();
        fillers.add(new RootOfLemmaFiller());
        return fillers;
    }


    private class RootOfLemmaFiller implements TransactionalFiller {
        @Override
        public void fillInTransaction(GraphDatabaseService database) throws ParserConfigurationException, IOException, SAXException {


            TraversalDescription td = database.traversalDescription().depthFirst()
                    .relationships(RelationshipTypes.HAS_LEMMA, Direction.INCOMING)
                    .relationships(RelationshipTypes.HAS_ROOT, Direction.OUTGOING)
                    .evaluator(new Evaluator() {
                        @Override
                        public Evaluation evaluate(Path path) {
                            if (path.length() == 2) {
                                return Evaluation.INCLUDE_AND_PRUNE;
                            }
                            return Evaluation.EXCLUDE_AND_CONTINUE;
                        }
                    });

            ResourceIterable<Node> lemmas = GlobalGraphOperations.at(database).getAllNodesWithLabel(NodeLabels.LEMMA);
            for (Node lemma : lemmas) {
                ResourceIterator<Path> it = td.traverse(lemma).iterator();

                while (it.hasNext()) {
                    Path path = it.next();
                    Node root = path.endNode();
//                    Node lemma = path.startNode();

//                    check for duplicate root
//                if (lemma.getRelationships(RelationshipTypes.LEMMA_HAS_ROOT, Direction.OUTGOING).iterator().hasNext()) {
//                    Node prev = lemma.getRelationships(RelationshipTypes.LEMMA_HAS_ROOT, Direction.OUTGOING).iterator().next().getEndNode();
//                    if (!prev.equals(root)) {
//                        String buckwalter = NodeProperties.GeneralText.buckwalter;
//                        throw new RuntimeException(String.format("Lemma(%s) has many roots :[%s,%s]",
//                                lemma.getId(), prev.getId(), root.getId())
//                        );
//                    }
//                }

                    lemma.createRelationshipTo(root, RelationshipTypes.LEMMA_HAS_ROOT);
                }
            }

        }
    }
}
