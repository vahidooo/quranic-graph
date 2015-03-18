package model.impl.verse;

import base.GraphIndices;
import base.NodeProperties;
import base.RelationshipTypes;
import model.api.chapter.Chapter;
import model.api.verse.Verse;
import model.api.word.Word;
import model.impl.base.NodeContainerImpl;
import model.impl.chapter.ChapterImpl;
import model.impl.word.WordImpl;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import util.NodeUtils;

/**
 * Created by vahidoo on 3/13/15.
 */
public class VerseImpl extends NodeContainerImpl implements Verse {
    public VerseImpl(Node node) {
        super(node);
    }

    @Override
    public String getAddress() {
        return (String) node.getProperty(NodeProperties.Verse.address);
    }

    @Override
    public Chapter getChapter() {
        Node chapter = NodeUtils.singleNeighborhood(node, RelationshipTypes.CONTAINS_VERSE, Direction.INCOMING, true);
        return (Chapter) NodeContainerImpl.createNewInstance(ChapterImpl.class, chapter);
    }

    @Override
    public Integer getIndex() {
        return (Integer) node.getProperty(NodeProperties.Verse.index);

    }

    @Override
    public Word getWord(int index) {
        GraphDatabaseService database = node.getGraphDatabase();
        Node wordNode = database.index().forNodes(GraphIndices.WordIndex).get(NodeProperties.General.address, NodeUtils.getNodeAddress(getAddress(),index) ).getSingle();
        if ( wordNode == null ){
            return null;
        }
        return new WordImpl(wordNode);
    }

    @Override
    public Verse getSuccessor() {
        Node n = NodeUtils.singleNeighborhood(node, RelationshipTypes.NEXT_VERSE, Direction.OUTGOING, false);
        return (Verse) NodeContainerImpl.createNewInstance(VerseImpl.class, n);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Verse) {
            Verse verse = (Verse) obj;
            return verse.getAddress().equals(getAddress());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getAddress().hashCode();
    }
}
