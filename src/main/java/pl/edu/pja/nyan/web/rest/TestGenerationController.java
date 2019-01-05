package pl.edu.pja.nyan.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codahale.metrics.annotation.Timed;

import lombok.extern.slf4j.Slf4j;
import pl.edu.pja.nyan.service.VocabularyTestService;
import pl.edu.pja.nyan.service.WordsTestService;
import pl.edu.pja.nyan.service.dto.WordsTestDTO;
import pl.edu.pja.nyan.service.dto.test.VocabularyTestItemDTO;

@Slf4j
@Controller
@RequestMapping("/api")
public class TestGenerationController {

    private final WordsTestService wordsTestService;

    private final VocabularyTestService vocabularyTestService;

    public TestGenerationController(WordsTestService wordsTestService,
        VocabularyTestService vocabularyTestService) {
        this.wordsTestService = wordsTestService;
        this.vocabularyTestService = vocabularyTestService;
    }

    /**
     * GET  /show-test/written/:code - generate written test based on a WordsTest with code :code.
     *
     * @param testCode code of the wordsTestDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and body, or with status 404 (Not Found)
     */
    @GetMapping("/show-test/written/{testCode}")
    @Timed
    public ResponseEntity<List<VocabularyTestItemDTO>> generateWrittenTest(@PathVariable String testCode) {
        log.debug("REST request to get written test by code: {}", testCode);
        Optional<WordsTestDTO> testOpt = wordsTestService.findByCode(testCode);
        if (testOpt.isPresent()) {
            WordsTestDTO test = testOpt.get();
            if (!vocabularyTestService.isWrittenTypeOfTest(test.getType())) {
                return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
            }

            List<VocabularyTestItemDTO> vocabularyTest = vocabularyTestService.generateTest(test);
            return ResponseEntity.ok(vocabularyTest);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
