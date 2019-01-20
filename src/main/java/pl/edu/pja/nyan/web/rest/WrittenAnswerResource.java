package pl.edu.pja.nyan.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.edu.pja.nyan.service.WrittenAnswerService;
import pl.edu.pja.nyan.web.rest.errors.BadRequestAlertException;
import pl.edu.pja.nyan.web.rest.util.HeaderUtil;
import pl.edu.pja.nyan.web.rest.util.PaginationUtil;
import pl.edu.pja.nyan.service.dto.WrittenAnswerDTO;
import pl.edu.pja.nyan.service.dto.WrittenAnswerCriteria;
import pl.edu.pja.nyan.service.WrittenAnswerQueryService;
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
 * REST controller for managing WrittenAnswer.
 */
@RestController
@RequestMapping("/api")
public class WrittenAnswerResource {

    private final Logger log = LoggerFactory.getLogger(WrittenAnswerResource.class);

    private static final String ENTITY_NAME = "writtenAnswer";

    private final WrittenAnswerService writtenAnswerService;

    private final WrittenAnswerQueryService writtenAnswerQueryService;

    public WrittenAnswerResource(WrittenAnswerService writtenAnswerService, WrittenAnswerQueryService writtenAnswerQueryService) {
        this.writtenAnswerService = writtenAnswerService;
        this.writtenAnswerQueryService = writtenAnswerQueryService;
    }

    /**
     * POST  /written-answers : Create a new writtenAnswer.
     *
     * @param writtenAnswerDTO the writtenAnswerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new writtenAnswerDTO, or with status 400 (Bad Request) if the writtenAnswer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/written-answers")
    @Timed
    public ResponseEntity<WrittenAnswerDTO> createWrittenAnswer(@RequestBody WrittenAnswerDTO writtenAnswerDTO) throws URISyntaxException {
        log.debug("REST request to save WrittenAnswer : {}", writtenAnswerDTO);
        if (writtenAnswerDTO.getId() != null) {
            throw new BadRequestAlertException("A new writtenAnswer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WrittenAnswerDTO result = writtenAnswerService.save(writtenAnswerDTO);
        return ResponseEntity.created(new URI("/api/written-answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /written-answers : Updates an existing writtenAnswer.
     *
     * @param writtenAnswerDTO the writtenAnswerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated writtenAnswerDTO,
     * or with status 400 (Bad Request) if the writtenAnswerDTO is not valid,
     * or with status 500 (Internal Server Error) if the writtenAnswerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/written-answers")
    @Timed
    public ResponseEntity<WrittenAnswerDTO> updateWrittenAnswer(@RequestBody WrittenAnswerDTO writtenAnswerDTO) throws URISyntaxException {
        log.debug("REST request to update WrittenAnswer : {}", writtenAnswerDTO);
        if (writtenAnswerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WrittenAnswerDTO result = writtenAnswerService.save(writtenAnswerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, writtenAnswerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /written-answers : get all the writtenAnswers.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of writtenAnswers in body
     */
    @GetMapping("/written-answers")
    @Timed
    public ResponseEntity<List<WrittenAnswerDTO>> getAllWrittenAnswers(WrittenAnswerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WrittenAnswers by criteria: {}", criteria);
        Page<WrittenAnswerDTO> page = writtenAnswerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/written-answers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /written-answers/:id : get the "id" writtenAnswer.
     *
     * @param id the id of the writtenAnswerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the writtenAnswerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/written-answers/{id}")
    @Timed
    public ResponseEntity<WrittenAnswerDTO> getWrittenAnswer(@PathVariable Long id) {
        log.debug("REST request to get WrittenAnswer : {}", id);
        Optional<WrittenAnswerDTO> writtenAnswerDTO = writtenAnswerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(writtenAnswerDTO);
    }

    /**
     * DELETE  /written-answers/:id : delete the "id" writtenAnswer.
     *
     * @param id the id of the writtenAnswerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/written-answers/{id}")
    @Timed
    public ResponseEntity<Void> deleteWrittenAnswer(@PathVariable Long id) {
        log.debug("REST request to delete WrittenAnswer : {}", id);
        writtenAnswerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
