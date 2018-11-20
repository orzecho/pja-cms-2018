package pl.edu.pja.nyan.web.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.service.TagService;
import pl.edu.pja.nyan.service.VocabularyTestService;
import pl.edu.pja.nyan.service.dto.test.VocabularyTestItemDTO;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestResource {
    private final VocabularyTestService vocabularyTestService;
    private final TagService tagService;

    @GetMapping("/vocabulary/{type}/{tagIds}")
    public List<VocabularyTestItemDTO> generateVocabularyTest(@PathVariable String type, @PathVariable String tagIds) {
        return vocabularyTestService.generateTest(tagService.findByIds(tagIds),
            VocabularyTestService.VocabularyTestType.valueOf(type));
    }
}
