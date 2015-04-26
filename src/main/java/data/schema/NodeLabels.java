package data.schema;

import org.neo4j.graphdb.Label;

/**
 * Created by vahidoo on 3/5/15.
 */
public enum NodeLabels implements Label {
    CHAPTER , VERSE , WORD , TOKEN, TAG,
    STEM , SUFFIX , PREFIX ,
    ROOT , LEMMA, POS,
    DataFiller,



}
