package quran.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import quran.entity.Lemma;
import quran.entity.Root;
import quran.entity.Verse;
import quran.repository.RootRepository;
import quran.repository.VerseRepository;

import java.util.Set;

/**
 * Created by vahidoo on 2/26/16.
 */

@RestController
public class RootService {

    @Autowired
    private RootRepository rootRepository;

    @Autowired
    private Neo4jTemplate template;
    @Autowired
    private VerseRepository verseRepository;

    @RequestMapping(value = "/root/lemmas/{root}", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public Set<Lemma> getLemmas(@PathVariable String root) {
        Root r = rootRepository.findByForm(root);
        Set<Lemma> lemmas = r.getDrivens();
        return template.fetch(lemmas);
    }


//    @RequestMapping(value = "/root/verse/{seq}", method = RequestMethod.GET)
//    @ResponseBody
//    @Transactional
//    public Set<Lemma> getVerseByRootsSeq(@PathVariable String seq) {
//        Verse r = verseRepository.findByRootsSeq(roots);
//        Set<Lemma> lemmas = r.getDrivens();
//        return template.fetch(lemmas);
//    }


}