package quran.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import quran.entity.Chapter;
import quran.repository.ChapterRepository;

import java.util.List;

/**
 * Created by vahidoo on 2/26/16.
 */

@RestController
public class ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;

    @RequestMapping(value = "/chapter/{index}", method = RequestMethod.GET)
    @ResponseBody
    public Chapter getChapterByIndex(@PathVariable Integer index) {
        return chapterRepository.findByIndex(index);
    }


    @RequestMapping(value = "/chapter/all", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public List<Chapter> getAll() {
        return chapterRepository.findAllOrderByIndex();
    }

}
