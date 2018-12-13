package pl.edu.pja.nyan.web.rest;

import com.codahale.metrics.annotation.Timed;

import pl.edu.pja.nyan.security.AuthoritiesConstants;
import pl.edu.pja.nyan.service.WordService;
import pl.edu.pja.nyan.web.rest.errors.BadRequestAlertException;
import pl.edu.pja.nyan.web.rest.util.HeaderUtil;
import pl.edu.pja.nyan.web.rest.util.PaginationUtil;
import pl.edu.pja.nyan.service.dto.WordDTO;
import pl.edu.pja.nyan.service.dto.WordCriteria;
import pl.edu.pja.nyan.service.WordQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Word.
 */
@RestController
@RequestMapping("/api")
public class WordResource {

    private final Logger log = LoggerFactory.getLogger(WordResource.class);

    private static final String ENTITY_NAME = "word";

    private final WordService wordService;

    private final WordQueryService wordQueryService;

    public WordResource(WordService wordService, WordQueryService wordQueryService) {
        this.wordService = wordService;
        this.wordQueryService = wordQueryService;
    }

    /**
     * POST  /words : Create a new word.
     *
     * @param wordDTO the wordDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wordDTO, or with status 400 (Bad Request) if the word has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/words")
    @Timed
    @Secured({ AuthoritiesConstants.ADMIN, AuthoritiesConstants.TEACHER})
    public ResponseEntity<WordDTO> createWord(@Valid @RequestBody WordDTO wordDTO) throws URISyntaxException {
        log.debug("REST request to save Word : {}", wordDTO);
        if (wordDTO.getId() != null) {
            throw new BadRequestAlertException("A new word cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WordDTO result = wordService.save(wordDTO);
        return ResponseEntity.created(new URI("/api/words/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /words : Updates an existing word.
     *
     * @param wordDTO the wordDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wordDTO,
     * or with status 400 (Bad Request) if the wordDTO is not valid,
     * or with status 500 (Internal Server Error) if the wordDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/words")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.TEACHER})
    public ResponseEntity<WordDTO> updateWord(@Valid @RequestBody WordDTO wordDTO) throws URISyntaxException {
        log.debug("REST request to update Word : {}", wordDTO);
        if (wordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WordDTO result = wordService.save(wordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wordDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /words : get all the words.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of words in body
     */
    @GetMapping("/words")
    @Timed
    public ResponseEntity<List<WordDTO>> getAllWords(WordCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Words by criteria: {}", criteria);
        Page<WordDTO> page = wordQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/words");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /words/:id : get the "id" word.
     *
     * @param id the id of the wordDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wordDTO, or with status 404 (Not Found)
     */
    @GetMapping("/words/{id}")
    @Timed
    public ResponseEntity<WordDTO> getWord(@PathVariable Long id) {
        log.debug("REST request to get Word : {}", id);
        Optional<WordDTO> wordDTO = wordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wordDTO);
    }

    /**
     * DELETE  /words/:id : delete the "id" word.
     *
     * @param id the id of the wordDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/words/{id}")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.TEACHER})
    public ResponseEntity<Void> deleteWord(@PathVariable Long id) {
        log.debug("REST request to delete Word : {}", id);
        wordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
