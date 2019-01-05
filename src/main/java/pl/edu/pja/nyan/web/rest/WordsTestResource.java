package pl.edu.pja.nyan.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
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
import pl.edu.pja.nyan.service.TagService;
import pl.edu.pja.nyan.service.UserService;
import pl.edu.pja.nyan.service.VocabularyTestService;
import pl.edu.pja.nyan.service.WordService;
import pl.edu.pja.nyan.service.WordsTestQueryService;
import pl.edu.pja.nyan.service.WordsTestService;
import pl.edu.pja.nyan.service.dto.WordDTO;
import pl.edu.pja.nyan.service.dto.WordsTestCriteria;
import pl.edu.pja.nyan.service.dto.WordsTestDTO;
import pl.edu.pja.nyan.service.dto.test.VocabularyTestItemDTO;
import pl.edu.pja.nyan.service.util.RandomUtil;
import pl.edu.pja.nyan.web.rest.errors.BadRequestAlertException;
import pl.edu.pja.nyan.web.rest.util.HeaderUtil;
import pl.edu.pja.nyan.web.rest.util.PaginationUtil;
import springfox.documentation.swagger.readers.operation.ResponseHeaders;

/**
 * REST controller for managing WordsTest.
 */
@RestController
@RequestMapping("/api")
public class WordsTestResource {

    private final Logger log = LoggerFactory.getLogger(WordsTestResource.class);

    private static final String ENTITY_NAME = "wordsTest";

    private final WordsTestService wordsTestService;

    private final WordsTestQueryService wordsTestQueryService;

    private final UserService userService;

    private final TagService tagService;

    private final WordService wordService;

    private final VocabularyTestService vocabularyTestService;

    public WordsTestResource(WordsTestService wordsTestService, WordsTestQueryService wordsTestQueryService,
        UserService userService, TagService tagService, WordService wordService,
        VocabularyTestService vocabularyTestService) {
        this.wordsTestService = wordsTestService;
        this.wordsTestQueryService = wordsTestQueryService;
        this.userService = userService;
        this.tagService = tagService;
        this.wordService = wordService;
        this.vocabularyTestService = vocabularyTestService;
    }

    /**
     * POST  /words-tests : Create a new wordsTest.
     *
     * @param wordsTestDTO the wordsTestDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wordsTestDTO, or with status 400 (Bad Request) if the wordsTest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/words-tests")
    @Timed
    public ResponseEntity<WordsTestDTO> createWordsTest(
        @Valid @RequestBody WordsTestDTO wordsTestDTO,
        @RequestParam(name = "tags", required = false) List<String> tagsToUseWordsFrom
    ) throws URISyntaxException {
        log.debug("REST request to save WordsTest : {}", wordsTestDTO);
        if (wordsTestDTO.getId() != null) {
            throw new BadRequestAlertException("A new wordsTest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        wordsTestDTO.setCreatorId(getCurrentUserId());
        wordsTestDTO.setCode(generateTestCode());
        if (tagsToUseWordsFrom != null) {
            setWordsHavingTags(wordsTestDTO, tagsToUseWordsFrom);
        }
        WordsTestDTO result = wordsTestService.save(wordsTestDTO);
        return ResponseEntity.created(new URI("/api/words-tests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private void setWordsHavingTags(WordsTestDTO wordsTestDTO, List<String> tagsToUseWordsFrom) {
        List<Tag> tags = tagService.findByTagNames(tagsToUseWordsFrom);
        List<WordDTO> words = wordService.findByTags(tags);
        wordsTestDTO.setWords(new HashSet<>(words));
    }

    private Long getCurrentUserId() {
        User user = userService.getUserWithAuthorities().orElseThrow(() ->
            new SessionAuthenticationException("User not logged in"));
        return user.getId();
    }

    private String generateTestCode() {
        while (true) {
            String code = RandomUtil.generateTestCode();
            if (!wordsTestService.testAlreadyExists(code)) {
                return code;
            }
        }
    }

    /**
     * PUT  /words-tests : Updates an existing wordsTest.
     *
     * @param wordsTestDTO the wordsTestDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wordsTestDTO,
     * or with status 400 (Bad Request) if the wordsTestDTO is not valid,
     * or with status 500 (Internal Server Error) if the wordsTestDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/words-tests")
    @Timed
    public ResponseEntity<WordsTestDTO> updateWordsTest(
        @Valid @RequestBody WordsTestDTO wordsTestDTO,
        @RequestParam(name = "tags", required = false) List<String> tagsToUseWordsFrom
    ) throws URISyntaxException {
        log.debug("REST request to update WordsTest : {}", wordsTestDTO);
        if (wordsTestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (tagsToUseWordsFrom != null) {
            setWordsHavingTags(wordsTestDTO, tagsToUseWordsFrom);
        }
        WordsTestDTO result = wordsTestService.save(wordsTestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wordsTestDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /words-tests : get all the wordsTests.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of wordsTests in body
     */
    @GetMapping("/words-tests")
    @Timed
    public ResponseEntity<List<WordsTestDTO>> getAllWordsTests(WordsTestCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WordsTests by criteria: {}", criteria);
        Page<WordsTestDTO> page = wordsTestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/words-tests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /words-tests/:id : get the "id" wordsTest.
     *
     * @param id the id of the wordsTestDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wordsTestDTO, or with status 404 (Not Found)
     */
    @GetMapping("/words-tests/{id}")
    @Timed
    public ResponseEntity<WordsTestDTO> getWordsTest(@PathVariable Long id) {
        log.debug("REST request to get WordsTest : {}", id);
        Optional<WordsTestDTO> wordsTestDTO = wordsTestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wordsTestDTO);
    }

    /**
     * DELETE  /words-tests/:id : delete the "id" wordsTest.
     *
     * @param id the id of the wordsTestDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/words-tests/{id}")
    @Timed
    public ResponseEntity<Void> deleteWordsTest(@PathVariable Long id) {
        log.debug("REST request to delete WordsTest : {}", id);
        wordsTestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
