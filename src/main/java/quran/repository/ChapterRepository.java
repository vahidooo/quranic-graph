package quran.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;
import quran.entity.Chapter;

import java.util.List;

/**
 * Created by vahidoo on 2/26/16.
 */
@Repository
public interface ChapterRepository extends GraphRepository<Chapter> {
    public Chapter findByIndex(int index);


    @Query("MATCH (ch:Chapter) return ch ORDER BY ch.index")
    public List<Chapter> findAllOrderByIndex();
}
