package pl.edu.pja.nyan.web.rest;

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
import pl.edu.pja.nyan.service.ExamService;
import pl.edu.pja.nyan.service.TestGenerationService;
import pl.edu.pja.nyan.service.dto.ExamDTO;
import pl.edu.pja.nyan.service.dto.test.VocabularyTestItemDTO;

@Slf4j
@Controller
@RequestMapping("/api")
public class TestGenerationController {

    private final ExamService examService;

    private final TestGenerationService testGenerationService;

    public TestGenerationController(ExamService examService,
        TestGenerationService testGenerationService) {
        this.examService = examService;
        this.testGenerationService = testGenerationService;
    }

    /**
     * GET  /show-test/written/:code - generate written test based on a WordsTest with code :code.
     *
     * @param code code of the wordsTestDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and body, or with status 404 (Not Found)
     */
    @GetMapping("/generate-test/written/{code}")
    @Timed
    public ResponseEntity<List<VocabularyTestItemDTO>> generateWrittenTest(@PathVariable String code) {
        log.debug("REST request to generate written test by code: {}", code);
        Optional<ExamDTO> testOpt = examService.findByCode(code);
        if (testOpt.isPresent()) {
            ExamDTO test = testOpt.get();
            List<VocabularyTestItemDTO> vocabularyTest =
                testGenerationService.generateTestByExamCode(test.getCode());
            return ResponseEntity.ok(vocabularyTest);
        }
        log.error("There is no exam with code {}", code);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
