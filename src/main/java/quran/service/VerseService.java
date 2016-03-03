package quran.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import quran.entity.Chapter;
import quran.entity.Verse;
import quran.repository.ChapterRepository;

import java.util.ArrayList;
import java.util.List;

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
    public Verse getVerse(@PathVariable Integer chapterIndex, @PathVariable Integer index) {
        Chapter chapter = chapterRepository.findByIndex(chapterIndex);
        Verse verse = chapter.getVerses().get(index - 1);
        return template.fetch(verse);
    }

    @RequestMapping(value = "/verse/{chapterIndex}:{startIndex}/{endIndex}", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public List<Verse> getVerseRange(@PathVariable Integer chapterIndex, @PathVariable Integer startIndex, @PathVariable Integer endIndex) {
        Chapter chapter = chapterRepository.findByIndex(chapterIndex);
        ArrayList<Verse> verses = new ArrayList<>();

        for (int i = startIndex - 1; i < endIndex; i++) {
            verses.add(chapter.getVerses().get(i));
        }

        return template.fetch(verses);
    }


    @RequestMapping(value = "/verse/{chapterIndex}:all", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public List<Verse> getAllVerses(@PathVariable Integer chapterIndex) {
        Chapter chapter = chapterRepository.findByIndex(chapterIndex);
        return template.fetch(chapter.getVerses());
    }

}
