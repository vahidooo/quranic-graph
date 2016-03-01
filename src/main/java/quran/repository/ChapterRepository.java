package quran.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;
import quran.entity.Chapter;

/**
 * Created by vahidoo on 2/26/16.
 */
@Repository
public interface ChapterRepository extends GraphRepository<Chapter> {
    public Chapter findByIndex(int index);
}
