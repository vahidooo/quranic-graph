package quran.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import quran.entity.Root;

import java.util.Set;

/**
 * Created by vahidoo on 3/3/16.
 */
public interface RootRepository extends GraphRepository<Root> {

    @Query("MATCH (r:Root) WHERE r.form =~ {0} return r")
    Set<Root> findByFormLike(String form);

    @Query("MATCH (r:Root) WHERE r.form = {0} return r")
    Root findByForm(String form);

}
