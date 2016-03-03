package quran.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import quran.entity.Root;

/**
 * Created by vahidoo on 3/3/16.
 */
public interface RootRepository extends GraphRepository<Root> {

    Root findByForm(String form);

    Root findByFormLike(String form);
}
