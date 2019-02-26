package pl.edu.pja.nyan.web.rest;

import com.codahale.metrics.annotation.Timed;

import pl.edu.pja.nyan.domain.Word;
import pl.edu.pja.nyan.service.ProposedWordService;
import pl.edu.pja.nyan.service.dto.WordDTO;
import pl.edu.pja.nyan.web.rest.errors.BadRequestAlertException;
import pl.edu.pja.nyan.web.rest.util.HeaderUtil;
import pl.edu.pja.nyan.web.rest.util.PaginationUtil;
import pl.edu.pja.nyan.service.dto.ProposedWordDTO;
import pl.edu.pja.nyan.service.dto.ProposedWordCriteria;
import pl.edu.pja.nyan.service.ProposedWordQueryService;
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
 * REST controller for managing ProposedWord.
 */
@RestController
@RequestMapping("/api")
public class ProposedWordResource {

    private final Logger log = LoggerFactory.getLogger(ProposedWordResource.class);

    private static final String ENTITY_NAME = "proposedWord";

    private final ProposedWordService proposedWordService;

    private final ProposedWordQueryService proposedWordQueryService;

    public ProposedWordResource(ProposedWordService proposedWordService, ProposedWordQueryService proposedWordQueryService) {
        this.proposedWordService = proposedWordService;
        this.proposedWordQueryService = proposedWordQueryService;
    }

    /**
     * POST  /proposed-words : Create a new proposedWord.
     *
     * @param proposedWordDTO the proposedWordDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new proposedWordDTO, or with status 400 (Bad Request) if the proposedWord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/proposed-words")
    @Timed
    public ResponseEntity<ProposedWordDTO> createProposedWord(@Valid @RequestBody ProposedWordDTO proposedWordDTO) throws URISyntaxException {
        log.debug("REST request to save ProposedWord : {}", proposedWordDTO);
        if (proposedWordDTO.getId() != null) {
            throw new BadRequestAlertException("A new proposedWord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProposedWordDTO result = proposedWordService.save(proposedWordDTO);
        return ResponseEntity.created(new URI("/api/proposed-words/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /proposed-words : Updates an existing proposedWord.
     *
     * @param proposedWordDTO the proposedWordDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated proposedWordDTO,
     * or with status 400 (Bad Request) if the proposedWordDTO is not valid,
     * or with status 500 (Internal Server Error) if the proposedWordDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/proposed-words")
    @Timed
    public ResponseEntity<ProposedWordDTO> updateProposedWord(@Valid @RequestBody ProposedWordDTO proposedWordDTO) throws URISyntaxException {
        log.debug("REST request to update ProposedWord : {}", proposedWordDTO);
        if (proposedWordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProposedWordDTO result = proposedWordService.save(proposedWordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, proposedWordDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /proposed-words : get all the proposedWords.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of proposedWords in body
     */
    @GetMapping("/proposed-words")
    @Timed
    public ResponseEntity<List<ProposedWordDTO>> getAllProposedWords(ProposedWordCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProposedWords by criteria: {}", criteria);
        Page<ProposedWordDTO> page = proposedWordQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/proposed-words");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/proposed-words/accept/{id}")
    @Timed
    public ResponseEntity<WordDTO> acceptWord(@PathVariable Long id) {
        WordDTO wordDTO = this.proposedWordService.acceptWord(id);
        this.proposedWordService.delete(id);
        return new ResponseEntity<>(wordDTO, HeaderUtil.createAlert("Zaakceptowano!", id.toString()),
            HttpStatus.OK);
    }

    /**
     * GET  /proposed-words/:id : get the "id" proposedWord.
     *
     * @param id the id of the proposedWordDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the proposedWordDTO, or with status 404 (Not Found)
     */
    @GetMapping("/proposed-words/{id}")
    @Timed
    public ResponseEntity<ProposedWordDTO> getProposedWord(@PathVariable Long id) {
        log.debug("REST request to get ProposedWord : {}", id);
        Optional<ProposedWordDTO> proposedWordDTO = proposedWordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(proposedWordDTO);
    }

    /**
     * DELETE  /proposed-words/:id : delete the "id" proposedWord.
     *
     * @param id the id of the proposedWordDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/proposed-words/{id}")
    @Timed
    public ResponseEntity<Void> deleteProposedWord(@PathVariable Long id) {
        log.debug("REST request to delete ProposedWord : {}", id);
        proposedWordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
