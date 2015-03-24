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

import java.util.ArrayList;
import java.util.List;

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
    public String getText() {
        return (String) node.getProperty(NodeProperties.Verse.text);

    }

    @Override
    public List<Word> getWords() {
        List<Word> words = new ArrayList<>();

        Word word = getWord(1);
        if (word != null) {
            words.add(word);
            word = word.getSuccessor();
        }
        return words;
    }

    @Override
    public Verse getNextInQuran() {
        int index = getIndexInQuran();
        Node next = node.getGraphDatabase().index().forNodes(GraphIndices.VerseIndex).get(NodeProperties.Verse.indexInQuran, index + 1).getSingle();
        if (next == null)
            return null;

        return new VerseImpl(next);

    }

    @Override
    public int getIndexInQuran() {
        return (int) node.getProperty(NodeProperties.Verse.indexInQuran);
    }

    @Override
    public Word getWord(int index) {
        GraphDatabaseService database = node.getGraphDatabase();
        Node wordNode = database.index().forNodes(GraphIndices.WordIndex).get(NodeProperties.General.address, NodeUtils.getNodeAddress(getAddress(), index)).getSingle();
        if (wordNode == null) {
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

    @Override
    public String toString() {
        return getAddress();
    }
}
