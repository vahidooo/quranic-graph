package model.impl.chapter;

import base.NodeProperties;
import model.api.chapter.Chapter;
import model.api.verse.Verse;
import model.impl.base.NodeContainerImpl;
import org.neo4j.graphdb.Node;

/**
 * Created by vahidoo on 3/11/15.
 */
public class ChapterImpl extends NodeContainerImpl implements Chapter{

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
        throw new RuntimeException();
    }
}
