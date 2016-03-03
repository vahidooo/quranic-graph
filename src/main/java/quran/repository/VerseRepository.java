package quran.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;
import quran.entity.Verse;

import java.util.Set;

/**
 * Created by vahidoo on 2/26/16.
 */
@Repository
public interface VerseRepository extends GraphRepository<Verse> {

    @Query("MATCH (v:Verse)<-[r:VERSE]-(ch:Chapter) where ch.index={0} and v.index={1} return v")
    Verse findByChapterAndIndex(int chapter , int index);
    Verse findByAddress(String address);

//    @Query("start node MATCH (v:Verse)<-[r:VERSE]-(ch:Chapter) where ch.index={0} and v.index={1} return v")
//    Iterable<Verse> findByRootsSeq()




    @Query("MATCH (r:Root) WHERE r.form =~ {0} with r MATCH (r)-[:ROOT]-(t:Token)-[:TOKEN]-(w:Word)-[:WORD]-(v) with distinct v,count(distinct r) as c where c >= {1} return v")
    Set<Verse> getVerseByRootsPattern(String pattern , int threshold);

}
