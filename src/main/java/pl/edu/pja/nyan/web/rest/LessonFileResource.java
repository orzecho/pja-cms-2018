package pl.edu.pja.nyan.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.edu.pja.nyan.service.LessonFileService;
import pl.edu.pja.nyan.service.dto.LessonFileShortDTO;
import pl.edu.pja.nyan.web.rest.errors.BadRequestAlertException;
import pl.edu.pja.nyan.web.rest.util.HeaderUtil;
import pl.edu.pja.nyan.web.rest.util.PaginationUtil;
import pl.edu.pja.nyan.service.dto.LessonFileDTO;
import pl.edu.pja.nyan.service.dto.LessonFileCriteria;
import pl.edu.pja.nyan.service.LessonFileQueryService;
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
 * REST controller for managing LessonFile.
 */
@RestController
@RequestMapping("/api")
public class LessonFileResource {

    private final Logger log = LoggerFactory.getLogger(LessonFileResource.class);

    private static final String ENTITY_NAME = "lessonFile";

    private final LessonFileService lessonFileService;

    private final LessonFileQueryService lessonFileQueryService;

    public LessonFileResource(LessonFileService lessonFileService, LessonFileQueryService lessonFileQueryService) {
        this.lessonFileService = lessonFileService;
        this.lessonFileQueryService = lessonFileQueryService;
    }

    /**
     * POST  /lesson-files : Create a new lessonFile.
     *
     * @param lessonFileDTO the lessonFileDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lessonFileDTO, or with status 400 (Bad Request) if the lessonFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/lesson-files")
    @Timed
    public ResponseEntity<LessonFileDTO> createLessonFile(@Valid @RequestBody LessonFileDTO lessonFileDTO) throws URISyntaxException {
        log.debug("REST request to save LessonFile : {}", lessonFileDTO);
        if (lessonFileDTO.getId() != null) {
            throw new BadRequestAlertException("A new lessonFile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LessonFileDTO result = lessonFileService.save(lessonFileDTO);
        return ResponseEntity.created(new URI("/api/lesson-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lesson-files : Updates an existing lessonFile.
     *
     * @param lessonFileDTO the lessonFileDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lessonFileDTO,
     * or with status 400 (Bad Request) if the lessonFileDTO is not valid,
     * or with status 500 (Internal Server Error) if the lessonFileDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/lesson-files")
    @Timed
    public ResponseEntity<LessonFileDTO> updateLessonFile(@Valid @RequestBody LessonFileDTO lessonFileDTO) throws URISyntaxException {
        log.debug("REST request to update LessonFile : {}", lessonFileDTO);
        if (lessonFileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LessonFileDTO result = lessonFileService.save(lessonFileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, lessonFileDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lesson-files : get all the lessonFiles.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of lessonFiles in body
     */
    @GetMapping("/lesson-files")
    @Timed
    public ResponseEntity<List<LessonFileShortDTO>> getAllLessonFiles(LessonFileCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LessonFiles by criteria: {}", criteria);
        Page<LessonFileShortDTO> page = lessonFileQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lesson-files");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lesson-files/:id : get the "id" lessonFile.
     *
     * @param id the id of the lessonFileDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lessonFileDTO, or with status 404 (Not Found)
     */
    @GetMapping("/lesson-files/{id}")
    @Timed
    public ResponseEntity<LessonFileDTO> getLessonFile(@PathVariable Long id) {
        log.debug("REST request to get LessonFile : {}", id);
        Optional<LessonFileDTO> lessonFileDTO = lessonFileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lessonFileDTO);
    }

    /**
     * DELETE  /lesson-files/:id : delete the "id" lessonFile.
     *
     * @param id the id of the lessonFileDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/lesson-files/{id}")
    @Timed
    public ResponseEntity<Void> deleteLessonFile(@PathVariable Long id) {
        log.debug("REST request to delete LessonFile : {}", id);
        lessonFileService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
