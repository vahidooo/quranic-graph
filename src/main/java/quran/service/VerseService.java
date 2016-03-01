package quran.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import quran.entity.Chapter;
import quran.entity.Verse;
import quran.repository.ChapterRepository;

/**
 * Created by vahidoo on 2/26/16.
 */

@RestController
public class VerseService {

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private Neo4jTemplate template;

    @RequestMapping(value = "/verse/{chapterIndex}:{index}", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public Verse findByIndex(@PathVariable Integer chapterIndex, @PathVariable Integer index) {
        Chapter chapter = chapterRepository.findByIndex(chapterIndex);
        Verse verse = chapter.getVerses().get(index);
        return template.fetch(verse);
//        return null;
    }
}
