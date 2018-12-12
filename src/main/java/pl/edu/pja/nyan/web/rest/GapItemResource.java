package pl.edu.pja.nyan.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.edu.pja.nyan.service.GapItemService;
import pl.edu.pja.nyan.web.rest.errors.BadRequestAlertException;
import pl.edu.pja.nyan.web.rest.util.HeaderUtil;
import pl.edu.pja.nyan.web.rest.util.PaginationUtil;
import pl.edu.pja.nyan.service.dto.GapItemDTO;
import pl.edu.pja.nyan.service.dto.GapItemCriteria;
import pl.edu.pja.nyan.service.GapItemQueryService;
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
 * REST controller for managing GapItem.
 */
@RestController
@RequestMapping("/api")
public class GapItemResource {

    private final Logger log = LoggerFactory.getLogger(GapItemResource.class);

    private static final String ENTITY_NAME = "gapItem";

    private final GapItemService gapItemService;

    private final GapItemQueryService gapItemQueryService;

    public GapItemResource(GapItemService gapItemService, GapItemQueryService gapItemQueryService) {
        this.gapItemService = gapItemService;
        this.gapItemQueryService = gapItemQueryService;
    }

    /**
     * POST  /gap-items : Create a new gapItem.
     *
     * @param gapItemDTO the gapItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gapItemDTO, or with status 400 (Bad Request) if the gapItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/gap-items")
    @Timed
    public ResponseEntity<GapItemDTO> createGapItem(@Valid @RequestBody GapItemDTO gapItemDTO) throws URISyntaxException {
        log.debug("REST request to save GapItem : {}", gapItemDTO);
        if (gapItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new gapItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GapItemDTO result = gapItemService.save(gapItemDTO);
        return ResponseEntity.created(new URI("/api/gap-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gap-items : Updates an existing gapItem.
     *
     * @param gapItemDTO the gapItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gapItemDTO,
     * or with status 400 (Bad Request) if the gapItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the gapItemDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/gap-items")
    @Timed
    public ResponseEntity<GapItemDTO> updateGapItem(@Valid @RequestBody GapItemDTO gapItemDTO) throws URISyntaxException {
        log.debug("REST request to update GapItem : {}", gapItemDTO);
        if (gapItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GapItemDTO result = gapItemService.save(gapItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gapItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gap-items : get all the gapItems.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of gapItems in body
     */
    @GetMapping("/gap-items")
    @Timed
    public ResponseEntity<List<GapItemDTO>> getAllGapItems(GapItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GapItems by criteria: {}", criteria);
        Page<GapItemDTO> page = gapItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gap-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /gap-items/:id : get the "id" gapItem.
     *
     * @param id the id of the gapItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gapItemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/gap-items/{id}")
    @Timed
    public ResponseEntity<GapItemDTO> getGapItem(@PathVariable Long id) {
        log.debug("REST request to get GapItem : {}", id);
        Optional<GapItemDTO> gapItemDTO = gapItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gapItemDTO);
    }

    /**
     * DELETE  /gap-items/:id : delete the "id" gapItem.
     *
     * @param id the id of the gapItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/gap-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteGapItem(@PathVariable Long id) {
        log.debug("REST request to delete GapItem : {}", id);
        gapItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
