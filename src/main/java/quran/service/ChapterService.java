package quran.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import quran.entity.Chapter;
import quran.repository.ChapterRepository;

/**
 * Created by vahidoo on 2/26/16.
 */

@RestController
public class ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;

    @RequestMapping(value = "/chapter/{index}", method = RequestMethod.GET)
    @ResponseBody
    public Chapter findByIndex(@PathVariable Integer index) {
        return chapterRepository.findByIndex(index);
//        return null;
    }
}
