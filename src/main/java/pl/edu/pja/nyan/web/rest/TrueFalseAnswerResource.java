package pl.edu.pja.nyan.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.edu.pja.nyan.service.TrueFalseAnswerService;
import pl.edu.pja.nyan.web.rest.errors.BadRequestAlertException;
import pl.edu.pja.nyan.web.rest.util.HeaderUtil;
import pl.edu.pja.nyan.web.rest.util.PaginationUtil;
import pl.edu.pja.nyan.service.dto.TrueFalseAnswerDTO;
import pl.edu.pja.nyan.service.dto.TrueFalseAnswerCriteria;
import pl.edu.pja.nyan.service.TrueFalseAnswerQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TrueFalseAnswer.
 */
@RestController
@RequestMapping("/api")
public class TrueFalseAnswerResource {

    private final Logger log = LoggerFactory.getLogger(TrueFalseAnswerResource.class);

    private static final String ENTITY_NAME = "trueFalseAnswer";

    private final TrueFalseAnswerService trueFalseAnswerService;

    private final TrueFalseAnswerQueryService trueFalseAnswerQueryService;

    public TrueFalseAnswerResource(TrueFalseAnswerService trueFalseAnswerService, TrueFalseAnswerQueryService trueFalseAnswerQueryService) {
        this.trueFalseAnswerService = trueFalseAnswerService;
        this.trueFalseAnswerQueryService = trueFalseAnswerQueryService;
    }

    /**
     * POST  /true-false-answers : Create a new trueFalseAnswer.
     *
     * @param trueFalseAnswerDTO the trueFalseAnswerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trueFalseAnswerDTO, or with status 400 (Bad Request) if the trueFalseAnswer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/true-false-answers")
    @Timed
    public ResponseEntity<TrueFalseAnswerDTO> createTrueFalseAnswer(@RequestBody TrueFalseAnswerDTO trueFalseAnswerDTO) throws URISyntaxException {
        log.debug("REST request to save TrueFalseAnswer : {}", trueFalseAnswerDTO);
        if (trueFalseAnswerDTO.getId() != null) {
            throw new BadRequestAlertException("A new trueFalseAnswer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrueFalseAnswerDTO result = trueFalseAnswerService.save(trueFalseAnswerDTO);
        return ResponseEntity.created(new URI("/api/true-false-answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /true-false-answers : Updates an existing trueFalseAnswer.
     *
     * @param trueFalseAnswerDTO the trueFalseAnswerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trueFalseAnswerDTO,
     * or with status 400 (Bad Request) if the trueFalseAnswerDTO is not valid,
     * or with status 500 (Internal Server Error) if the trueFalseAnswerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/true-false-answers")
    @Timed
    public ResponseEntity<TrueFalseAnswerDTO> updateTrueFalseAnswer(@RequestBody TrueFalseAnswerDTO trueFalseAnswerDTO) throws URISyntaxException {
        log.debug("REST request to update TrueFalseAnswer : {}", trueFalseAnswerDTO);
        if (trueFalseAnswerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TrueFalseAnswerDTO result = trueFalseAnswerService.save(trueFalseAnswerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trueFalseAnswerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /true-false-answers : get all the trueFalseAnswers.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of trueFalseAnswers in body
     */
    @GetMapping("/true-false-answers")
    @Timed
    public ResponseEntity<List<TrueFalseAnswerDTO>> getAllTrueFalseAnswers(TrueFalseAnswerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TrueFalseAnswers by criteria: {}", criteria);
        Page<TrueFalseAnswerDTO> page = trueFalseAnswerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/true-false-answers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /true-false-answers/:id : get the "id" trueFalseAnswer.
     *
     * @param id the id of the trueFalseAnswerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trueFalseAnswerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/true-false-answers/{id}")
    @Timed
    public ResponseEntity<TrueFalseAnswerDTO> getTrueFalseAnswer(@PathVariable Long id) {
        log.debug("REST request to get TrueFalseAnswer : {}", id);
        Optional<TrueFalseAnswerDTO> trueFalseAnswerDTO = trueFalseAnswerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trueFalseAnswerDTO);
    }

    /**
     * DELETE  /true-false-answers/:id : delete the "id" trueFalseAnswer.
     *
     * @param id the id of the trueFalseAnswerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/true-false-answers/{id}")
    @Timed
    public ResponseEntity<Void> deleteTrueFalseAnswer(@PathVariable Long id) {
        log.debug("REST request to delete TrueFalseAnswer : {}", id);
        trueFalseAnswerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
