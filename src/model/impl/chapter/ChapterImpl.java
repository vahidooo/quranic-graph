package model.impl.chapter;

import base.GraphIndices;
import base.NodeProperties;
import model.api.chapter.Chapter;
import model.api.verse.Verse;
import model.impl.base.NodeContainerImpl;
import model.impl.verse.VerseImpl;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import util.NodeUtils;

/**
 * Created by vahidoo on 3/11/15.
 */
public class ChapterImpl extends NodeContainerImpl implements Chapter {

    public ChapterImpl(Node node) {
        super(node);
    }


    @Override
    public String getName() {
        return (String) node.getProperty(NodeProperties.Chapter.name);
    }

    @Override
    public Integer getIndex() {
        return (Integer) node.getProperty(NodeProperties.General.index);
    }

    @Override
    public Verse getVerse(int verse) {
        GraphDatabaseService database = node.getGraphDatabase();
        Node verseNode = database.index().forNodes(GraphIndices.VerseIndex).get(NodeProperties.Verse.address, NodeUtils.getNodeAddress(getIndex(), verse)).getSingle();
        if ( verseNode == null ){
            return null;
        }
        return new VerseImpl(verseNode);
    }

    @Override
    public Chapter getNextChapter() {
        GraphDatabaseService database = node.getGraphDatabase();
        Node chapterNode = database.index().forNodes(GraphIndices.ChapterIndex).get(NodeProperties.Chapter.index, getIndex()+1 ).getSingle();
        if ( chapterNode == null ){
            return null;
        }
        return new ChapterImpl(chapterNode);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Chapter) {
            Chapter chapter = (Chapter) obj;
            return chapter.getIndex().equals(getIndex());
        }
        return false;
    }
}
