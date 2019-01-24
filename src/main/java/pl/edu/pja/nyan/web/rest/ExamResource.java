package pl.edu.pja.nyan.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;
import pl.edu.pja.nyan.domain.Tag;
import pl.edu.pja.nyan.domain.User;
import pl.edu.pja.nyan.service.ExamQueryService;
import pl.edu.pja.nyan.service.ExamService;
import pl.edu.pja.nyan.service.TagService;
import pl.edu.pja.nyan.service.UserService;
import pl.edu.pja.nyan.service.WordService;
import pl.edu.pja.nyan.service.dto.ExamCriteria;
import pl.edu.pja.nyan.service.dto.ExamDTO;
import pl.edu.pja.nyan.service.dto.WordDTO;
import pl.edu.pja.nyan.service.util.RandomUtil;
import pl.edu.pja.nyan.web.rest.errors.BadRequestAlertException;
import pl.edu.pja.nyan.web.rest.errors.UserNotLoggedInException;
import pl.edu.pja.nyan.web.rest.util.HeaderUtil;
import pl.edu.pja.nyan.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Exam.
 */
@RestController
@RequestMapping("/api")
public class ExamResource {

    private final Logger log = LoggerFactory.getLogger(ExamResource.class);

    private static final String ENTITY_NAME = "exam";

    private final ExamService examService;

    private final ExamQueryService examQueryService;

    private final UserService userService;

    private final TagService tagService;

    private final WordService wordService;

    public ExamResource(ExamService wordsTestService, ExamQueryService wordsTestQueryService,
        UserService userService, TagService tagService, WordService wordService) {
        this.examService = wordsTestService;
        this.examQueryService = wordsTestQueryService;
        this.userService = userService;
        this.tagService = tagService;
        this.wordService = wordService;
    }


    /**
     * POST  /exams : Create a new exam.
     *
     * @param examDTO the examDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new examDTO, or with status 400 (Bad Request) if the exam has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/exams")
    @Timed
    public ResponseEntity<ExamDTO> createExam(
        @Valid @RequestBody ExamDTO examDTO,
        @RequestParam(name = "tags", required = false) List<String> tagsToUseWordsFrom
    ) throws URISyntaxException {
        log.debug("REST request to save Exam : {}", examDTO);
        if (examDTO.getId() != null) {
            throw new BadRequestAlertException("A new exam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User user = userService.getUserWithAuthorities()
            .orElseThrow(UserNotLoggedInException::new);
        examDTO.setCreatorId(user.getId());
        examDTO.setCreatorLogin(user.getLogin());
        examDTO.setCode(generateTestCode());
        if (tagsToUseWordsFrom != null) {
            setWordsHavingTags(examDTO, tagsToUseWordsFrom);
        }
        ExamDTO result = examService.save(examDTO);
        return ResponseEntity.created(new URI("/api/exams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /exams : Updates an existing exam.
     *
     * @param examDTO the examDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated examDTO,
     * or with status 400 (Bad Request) if the examDTO is not valid,
     * or with status 500 (Internal Server Error) if the examDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/exams")
    @Timed
    public ResponseEntity<ExamDTO> updateExam(
        @Valid @RequestBody ExamDTO examDTO,
        @RequestParam(name = "tags", required = false) List<String> tagsToUseWordsFrom
    ) throws URISyntaxException {
        log.debug("REST request to update Exam : {}", examDTO);
        if (examDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id null");
        }
        if (tagsToUseWordsFrom != null) {
            setWordsHavingTags(examDTO, tagsToUseWordsFrom);
        }
        ExamDTO result = examService.save(examDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, examDTO.getId().toString()))
            .body(result);
    }


    private void setWordsHavingTags(ExamDTO wordsTestDTO, List<String> tagsToUseWordsFrom) {
        List<Tag> tags = tagService.findByTagNames(tagsToUseWordsFrom);
        List<WordDTO> words = wordService.findByTags(tags);
        wordsTestDTO.setWords(new HashSet<>(words));
    }

    private String generateTestCode() {
        while (true) {
            String code = RandomUtil.generateExamCode();
            if (!examService.examAlreadyExists(code)) {
                return code;
            }
        }
    }


    /**
     * GET  /exams : get all the exams.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of exams in body
     */
    @GetMapping("/exams")
    @Timed
    public ResponseEntity<List<ExamDTO>> getAllExams(ExamCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Exams by criteria: {}", criteria);
        Page<ExamDTO> page = examQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/exams");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /exams/:id : get the "id" exam.
     *
     * @param id the id of the examDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the examDTO, or with status 404 (Not Found)
     */
    @GetMapping("/exams/{id}")
    @Timed
    public ResponseEntity<ExamDTO> getExam(@PathVariable Long id) {
        log.debug("REST request to get Exam : {}", id);
        Optional<ExamDTO> examDTO = examService.findOne(id);
        return ResponseUtil.wrapOrNotFound(examDTO);
    }

    /**
     * DELETE  /exams/:id : delete the "id" exam.
     *
     * @param id the id of the examDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/exams/{id}")
    @Timed
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        log.debug("REST request to delete Exam : {}", id);
        examService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
