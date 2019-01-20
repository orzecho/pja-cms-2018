package pl.edu.pja.nyan.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.edu.pja.nyan.service.ExamResultService;
import pl.edu.pja.nyan.web.rest.errors.BadRequestAlertException;
import pl.edu.pja.nyan.web.rest.util.HeaderUtil;
import pl.edu.pja.nyan.web.rest.util.PaginationUtil;
import pl.edu.pja.nyan.service.dto.ExamResultDTO;
import pl.edu.pja.nyan.service.dto.ExamResultCriteria;
import pl.edu.pja.nyan.service.ExamResultQueryService;
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
 * REST controller for managing ExamResult.
 */
@RestController
@RequestMapping("/api")
public class ExamResultResource {

    private final Logger log = LoggerFactory.getLogger(ExamResultResource.class);

    private static final String ENTITY_NAME = "examResult";

    private final ExamResultService examResultService;

    private final ExamResultQueryService examResultQueryService;

    public ExamResultResource(ExamResultService examResultService, ExamResultQueryService examResultQueryService) {
        this.examResultService = examResultService;
        this.examResultQueryService = examResultQueryService;
    }

    /**
     * POST  /exam-results : Create a new examResult.
     *
     * @param examResultDTO the examResultDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new examResultDTO, or with status 400 (Bad Request) if the examResult has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/exam-results")
    @Timed
    public ResponseEntity<ExamResultDTO> createExamResult(@Valid @RequestBody ExamResultDTO examResultDTO) throws URISyntaxException {
        log.debug("REST request to save ExamResult : {}", examResultDTO);
        if (examResultDTO.getId() != null) {
            throw new BadRequestAlertException("A new examResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExamResultDTO result = examResultService.save(examResultDTO);
        return ResponseEntity.created(new URI("/api/exam-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /exam-results : Updates an existing examResult.
     *
     * @param examResultDTO the examResultDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated examResultDTO,
     * or with status 400 (Bad Request) if the examResultDTO is not valid,
     * or with status 500 (Internal Server Error) if the examResultDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/exam-results")
    @Timed
    public ResponseEntity<ExamResultDTO> updateExamResult(@Valid @RequestBody ExamResultDTO examResultDTO) throws URISyntaxException {
        log.debug("REST request to update ExamResult : {}", examResultDTO);
        if (examResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExamResultDTO result = examResultService.save(examResultDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, examResultDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /exam-results : get all the examResults.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of examResults in body
     */
    @GetMapping("/exam-results")
    @Timed
    public ResponseEntity<List<ExamResultDTO>> getAllExamResults(ExamResultCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ExamResults by criteria: {}", criteria);
        Page<ExamResultDTO> page = examResultQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/exam-results");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /exam-results/:id : get the "id" examResult.
     *
     * @param id the id of the examResultDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the examResultDTO, or with status 404 (Not Found)
     */
    @GetMapping("/exam-results/{id}")
    @Timed
    public ResponseEntity<ExamResultDTO> getExamResult(@PathVariable Long id) {
        log.debug("REST request to get ExamResult : {}", id);
        Optional<ExamResultDTO> examResultDTO = examResultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(examResultDTO);
    }

    /**
     * DELETE  /exam-results/:id : delete the "id" examResult.
     *
     * @param id the id of the examResultDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/exam-results/{id}")
    @Timed
    public ResponseEntity<Void> deleteExamResult(@PathVariable Long id) {
        log.debug("REST request to delete ExamResult : {}", id);
        examResultService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
