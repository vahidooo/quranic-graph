package data.filler;

import data.filler.leeds.LeedsCorpusDataFiller;
import data.filler.leeds.more.LeedsCorpusNextDataFiller;
import data.filler.leeds.more.NextVerseInQuranDataFiller;
import data.filler.leeds.more.RootOfLemmaDataFiller;
import data.filler.leeds.more.WordIndexInQuranDataFiller;
import data.filler.tanzil.TanzilDataFiller;
import data.schema.GraphIndices;
import data.schema.NodeLabels;
import data.schema.NodeProperties;
import data.schema.RelationshipTypes;
import org.neo4j.graphdb.*;
import util.NodeUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by vahidoo on 3/20/15.
 */
public class DataFillerManager {
    private static final Logger logger = Logger.getLogger(DataFillerManager.class.getName());

    private Map<Class<? extends DataFiller>, Set<Class<? extends DataFiller>>> dependencyGraph;

    private GraphDatabaseService database;

    public DataFillerManager(GraphDatabaseService database) {
        this.database = database;

        dependencyGraph = new HashMap<>();
        addDataFiller(TanzilDataFiller.class, null);
        addDataFiller(LeedsCorpusDataFiller.class, TanzilDataFiller.class);
        addDataFiller(LeedsCorpusNextDataFiller.class, LeedsCorpusDataFiller.class);
        addDataFiller(WordIndexInQuranDataFiller.class, LeedsCorpusNextDataFiller.class);
        addDataFiller(NextVerseInQuranDataFiller.class, LeedsCorpusNextDataFiller.class);
        addDataFiller(RootOfLemmaDataFiller.class, LeedsCorpusDataFiller.class);


        buildGraph();
    }

    private void buildGraph() {

        try (Transaction tx = database.beginTx()) {

            //create DataFiller nodes
            for (Class<? extends DataFiller> clazz : dependencyGraph.keySet()) {
                Node node = database.index().forNodes(GraphIndices.DataFillerIdx).get(NodeProperties.DataFiller.clazz, clazz.getName()).getSingle();
                if (node == null) {
                    node = database.createNode();
                    node.addLabel(NodeLabels.DataFiller);
                    NodeUtils.setPropertyAndIndex(node, NodeProperties.DataFiller.clazz, GraphIndices.DataFillerIdx, clazz.getName());
                    node.setProperty(NodeProperties.DataFiller.state, DataFiller.State.NOT_FILLED.name());
                    node.setProperty(NodeProperties.DataFiller.progress, DataFiller.INITIAL_PROGRESS);
                }
            }

            //connect depended nodes
            for (Class<? extends DataFiller> clazz : dependencyGraph.keySet()) {
                Node node = database.index().forNodes(GraphIndices.DataFillerIdx).get(NodeProperties.DataFiller.clazz, clazz.getName()).getSingle();
                for (Class<? extends DataFiller> dependencyClazz : getDependencies(clazz)) {
                    Node dependencyNode = database.index().forNodes(GraphIndices.DataFillerIdx).get(NodeProperties.DataFiller.clazz, dependencyClazz.getName()).getSingle();

                    boolean notConnected = true;
                    Iterable<Relationship> it = node.getRelationships(RelationshipTypes.DEPENDS, Direction.OUTGOING);
                    for (Relationship relationship : it) {
                        if (relationship.getEndNode().equals(dependencyNode)) {
                            notConnected = false;
                        }
                    }
                    if (notConnected) {
                        node.createRelationshipTo(dependencyNode, RelationshipTypes.DEPENDS);
                    }

                    if (DataFiller.State.valueOf((String) dependencyNode.getProperty(NodeProperties.DataFiller.state)) != DataFiller.State.FILLED) {
                        node.setProperty(NodeProperties.DataFiller.state, DataFiller.State.DEPENDENCY_ERROR.name());
                    }
                }
            }

            tx.success();
        }
    }

    private void addDataFiller(Class<? extends DataFiller> clazz, Class<? extends DataFiller>... dependencies) {

        Set<Class<? extends DataFiller>> dependenciesSet = new HashSet<>();
        if (dependencies != null) {
            dependenciesSet.addAll(Arrays.asList(dependencies));
        }

        dependencyGraph.put(clazz, dependenciesSet);

    }

    public List<Class<? extends DataFiller>> getTopologicalSorted() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        logger.info("start sorting");
        List<Class<? extends DataFiller>> topologicalSorted = new ArrayList<>();

        if (dependencyGraph.isEmpty())
            return topologicalSorted;

        while (dependencyGraph.size() != topologicalSorted.size()) {
            int added = 0;
            for (Class<? extends DataFiller> filler : dependencyGraph.keySet()) {

                boolean flag = true;
                Set<Class<? extends DataFiller>> dependencies = dependencyGraph.get(filler);
                for (Class<? extends DataFiller> dependency : dependencies) {
                    if (!topologicalSorted.contains(dependency)) {
                        flag = false;
                        break;
                    }
                }

                if (flag && !topologicalSorted.contains(filler)) {
                    topologicalSorted.add(filler);
                    added++;
                }

            }
            if (added == 0) {
                throw new RuntimeException("Dependency Graph isn't DAG");
            }
        }

        logger.info("sorting was finished");
        return topologicalSorted;
    }

    public Set<Class<? extends DataFiller>> getDependencies(Class<? extends DataFiller> clazz) {
        return dependencyGraph.get(clazz);
    }

    public Set<Class<? extends DataFiller>> getFillers() {
        return dependencyGraph.keySet();
    }
}
