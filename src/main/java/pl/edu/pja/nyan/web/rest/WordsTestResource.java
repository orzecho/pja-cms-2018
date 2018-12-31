package pl.edu.pja.nyan.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.edu.pja.nyan.service.WordsTestService;
import pl.edu.pja.nyan.web.rest.errors.BadRequestAlertException;
import pl.edu.pja.nyan.web.rest.util.HeaderUtil;
import pl.edu.pja.nyan.web.rest.util.PaginationUtil;
import pl.edu.pja.nyan.service.dto.WordsTestDTO;
import pl.edu.pja.nyan.service.dto.WordsTestCriteria;
import pl.edu.pja.nyan.service.WordsTestQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

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

    public WordsTestResource(WordsTestService wordsTestService, WordsTestQueryService wordsTestQueryService) {
        this.wordsTestService = wordsTestService;
        this.wordsTestQueryService = wordsTestQueryService;
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
    public ResponseEntity<WordsTestDTO> createWordsTest(@Valid @RequestBody WordsTestDTO wordsTestDTO) throws URISyntaxException {
        log.debug("REST request to save WordsTest : {}", wordsTestDTO);
        if (wordsTestDTO.getId() != null) {
            throw new BadRequestAlertException("A new wordsTest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WordsTestDTO result = wordsTestService.save(wordsTestDTO);
        return ResponseEntity.created(new URI("/api/words-tests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
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
    public ResponseEntity<WordsTestDTO> updateWordsTest(@Valid @RequestBody WordsTestDTO wordsTestDTO) throws URISyntaxException {
        log.debug("REST request to update WordsTest : {}", wordsTestDTO);
        if (wordsTestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
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
