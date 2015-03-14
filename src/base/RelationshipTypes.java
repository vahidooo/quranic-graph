package base;


import org.neo4j.graphdb.RelationshipType;

/**
 * Created by vahidoo on 3/5/15.
 */
public enum RelationshipTypes implements RelationshipType {
    CONTAINS_VERSE,     //CHAPTER -->  VERSE
    CONTAINS_WORD,      //VERSE --> WORD
    CONTAINS_TOKEN,     //WORD --> TOKEN
    HAS_TAG, HAS_ROOT, HAS_LEMMA, HAS_POS,
    NEXT_WORD,NEXT_VERSE;

}

