package pl.edu.pja.nyan.web.rest;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.enumeration.TestType;
import pl.edu.pja.nyan.service.FillingGapsTestItemService;
import pl.edu.pja.nyan.service.TagService;
import pl.edu.pja.nyan.service.TestGenerationService;
import pl.edu.pja.nyan.service.dto.FillingGapsTestItemDTO;
import pl.edu.pja.nyan.service.dto.test.VocabularyTestItemDTO;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestResource {

    private final TestGenerationService testGenerationService;
    private final FillingGapsTestItemService fillingGapsTestItemService;
    private final TagService tagService;

    @GetMapping("/vocabulary/{type}/{tags}")
    public List<VocabularyTestItemDTO> generateVocabularyTest(@PathVariable String type, @PathVariable String tags) {
        return testGenerationService.generateTestByTags(tagService.findByTagNames(tags),
            TestType.valueOf(type));
    }

    @GetMapping("/gaps/{tags}")
    public List<FillingGapsTestItemDTO> generateFillingGapsTest(@PathVariable String tags) {
        return fillingGapsTestItemService.findByTags(Arrays.asList(tags.split(",")));
    }

}
