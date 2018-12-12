package pl.edu.pja.nyan.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.edu.pja.nyan.service.FillingGapsTestItemService;
import pl.edu.pja.nyan.web.rest.errors.BadRequestAlertException;
import pl.edu.pja.nyan.web.rest.util.HeaderUtil;
import pl.edu.pja.nyan.web.rest.util.PaginationUtil;
import pl.edu.pja.nyan.service.dto.FillingGapsTestItemDTO;
import pl.edu.pja.nyan.service.dto.FillingGapsTestItemCriteria;
import pl.edu.pja.nyan.service.FillingGapsTestItemQueryService;
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
 * REST controller for managing FillingGapsTestItem.
 */
@RestController
@RequestMapping("/api")
public class FillingGapsTestItemResource {

    private final Logger log = LoggerFactory.getLogger(FillingGapsTestItemResource.class);

    private static final String ENTITY_NAME = "fillingGapsTestItem";

    private final FillingGapsTestItemService fillingGapsTestItemService;

    private final FillingGapsTestItemQueryService fillingGapsTestItemQueryService;

    public FillingGapsTestItemResource(FillingGapsTestItemService fillingGapsTestItemService, FillingGapsTestItemQueryService fillingGapsTestItemQueryService) {
        this.fillingGapsTestItemService = fillingGapsTestItemService;
        this.fillingGapsTestItemQueryService = fillingGapsTestItemQueryService;
    }

    /**
     * POST  /filling-gaps-test-items : Create a new fillingGapsTestItem.
     *
     * @param fillingGapsTestItemDTO the fillingGapsTestItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fillingGapsTestItemDTO, or with status 400 (Bad Request) if the fillingGapsTestItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/filling-gaps-test-items")
    @Timed
    public ResponseEntity<FillingGapsTestItemDTO> createFillingGapsTestItem(@Valid @RequestBody FillingGapsTestItemDTO fillingGapsTestItemDTO) throws URISyntaxException {
        log.debug("REST request to save FillingGapsTestItem : {}", fillingGapsTestItemDTO);
        if (fillingGapsTestItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new fillingGapsTestItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FillingGapsTestItemDTO result = fillingGapsTestItemService.save(fillingGapsTestItemDTO);
        return ResponseEntity.created(new URI("/api/filling-gaps-test-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /filling-gaps-test-items : Updates an existing fillingGapsTestItem.
     *
     * @param fillingGapsTestItemDTO the fillingGapsTestItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fillingGapsTestItemDTO,
     * or with status 400 (Bad Request) if the fillingGapsTestItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the fillingGapsTestItemDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/filling-gaps-test-items")
    @Timed
    public ResponseEntity<FillingGapsTestItemDTO> updateFillingGapsTestItem(@Valid @RequestBody FillingGapsTestItemDTO fillingGapsTestItemDTO) throws URISyntaxException {
        log.debug("REST request to update FillingGapsTestItem : {}", fillingGapsTestItemDTO);
        if (fillingGapsTestItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FillingGapsTestItemDTO result = fillingGapsTestItemService.save(fillingGapsTestItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fillingGapsTestItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /filling-gaps-test-items : get all the fillingGapsTestItems.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of fillingGapsTestItems in body
     */
    @GetMapping("/filling-gaps-test-items")
    @Timed
    public ResponseEntity<List<FillingGapsTestItemDTO>> getAllFillingGapsTestItems(FillingGapsTestItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FillingGapsTestItems by criteria: {}", criteria);
        Page<FillingGapsTestItemDTO> page = fillingGapsTestItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/filling-gaps-test-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /filling-gaps-test-items/:id : get the "id" fillingGapsTestItem.
     *
     * @param id the id of the fillingGapsTestItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fillingGapsTestItemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/filling-gaps-test-items/{id}")
    @Timed
    public ResponseEntity<FillingGapsTestItemDTO> getFillingGapsTestItem(@PathVariable Long id) {
        log.debug("REST request to get FillingGapsTestItem : {}", id);
        Optional<FillingGapsTestItemDTO> fillingGapsTestItemDTO = fillingGapsTestItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fillingGapsTestItemDTO);
    }

    /**
     * DELETE  /filling-gaps-test-items/:id : delete the "id" fillingGapsTestItem.
     *
     * @param id the id of the fillingGapsTestItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/filling-gaps-test-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteFillingGapsTestItem(@PathVariable Long id) {
        log.debug("REST request to delete FillingGapsTestItem : {}", id);
        fillingGapsTestItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
