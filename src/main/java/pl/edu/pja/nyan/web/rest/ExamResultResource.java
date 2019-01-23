package pl.edu.pja.nyan.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import io.github.jhipster.web.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import pl.edu.pja.nyan.domain.User;
import static pl.edu.pja.nyan.security.AuthoritiesConstants.ADMIN;
import static pl.edu.pja.nyan.security.AuthoritiesConstants.TEACHER;
import static pl.edu.pja.nyan.security.AuthoritiesConstants.USER;
import pl.edu.pja.nyan.service.ExamResultQueryService;
import pl.edu.pja.nyan.service.ExamResultService;
import pl.edu.pja.nyan.service.UserService;
import pl.edu.pja.nyan.service.dto.ExamResultCriteria;
import pl.edu.pja.nyan.service.dto.ExamResultDTO;
import pl.edu.pja.nyan.service.mapper.UserMapper;
import pl.edu.pja.nyan.web.rest.errors.BadRequestAlertException;
import pl.edu.pja.nyan.web.rest.errors.UserNotLoggedInException;
import pl.edu.pja.nyan.web.rest.util.HeaderUtil;
import pl.edu.pja.nyan.web.rest.util.PaginationUtil;

/**
 * REST controller for managing ExamResult.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExamResultResource {

    private final Logger log = LoggerFactory.getLogger(ExamResultResource.class);

    private static final String ENTITY_NAME = "examResult";

    private final ExamResultService examResultService;

    private final ExamResultQueryService examResultQueryService;

    private final UserService userService;

    private final UserMapper userMapper;

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

        User user = userService.getUserWithAuthorities()
            .orElseThrow(UserNotLoggedInException::new);
        if (user.getAuthorities().containsAll(Arrays.asList(ADMIN, TEACHER))) {
            throw new BadRequestAlertException(
                "Only " + USER + " authority can create ExamResult", ENTITY_NAME, "illegalauthority");
        }

        examResultDTO.setStudentId(user.getId());
        examResultDTO.setStudentLogin(user.getLogin());
        examResultDTO.setDate(Instant.now());
        examResultDTO.setResult(examResultService.calculateExamResult(examResultDTO));

        ExamResultDTO result = examResultService.save(examResultDTO);
        return ResponseEntity.created(new URI("/api/exam-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
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
