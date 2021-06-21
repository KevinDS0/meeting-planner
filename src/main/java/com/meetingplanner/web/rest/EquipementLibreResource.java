package com.meetingplanner.web.rest;

import com.meetingplanner.repository.EquipementLibreRepository;
import com.meetingplanner.service.EquipementLibreService;
import com.meetingplanner.service.dto.EquipementLibreDTO;
import com.meetingplanner.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.meetingplanner.domain.EquipementLibre}.
 */
@RestController
@RequestMapping("/api")
public class EquipementLibreResource {

    private final Logger log = LoggerFactory.getLogger(EquipementLibreResource.class);

    private static final String ENTITY_NAME = "equipementLibre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EquipementLibreService equipementLibreService;

    private final EquipementLibreRepository equipementLibreRepository;

    public EquipementLibreResource(EquipementLibreService equipementLibreService, EquipementLibreRepository equipementLibreRepository) {
        this.equipementLibreService = equipementLibreService;
        this.equipementLibreRepository = equipementLibreRepository;
    }

    /**
     * {@code POST  /equipement-libres} : Create a new equipementLibre.
     *
     * @param equipementLibreDTO the equipementLibreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new equipementLibreDTO, or with status {@code 400 (Bad Request)} if the equipementLibre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/equipement-libres")
    public ResponseEntity<EquipementLibreDTO> createEquipementLibre(@RequestBody EquipementLibreDTO equipementLibreDTO)
        throws URISyntaxException {
        log.debug("REST request to save EquipementLibre : {}", equipementLibreDTO);
        if (equipementLibreDTO.getId() != null) {
            throw new BadRequestAlertException("A new equipementLibre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EquipementLibreDTO result = equipementLibreService.save(equipementLibreDTO);
        return ResponseEntity
            .created(new URI("/api/equipement-libres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /equipement-libres/:id} : Updates an existing equipementLibre.
     *
     * @param id the id of the equipementLibreDTO to save.
     * @param equipementLibreDTO the equipementLibreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipementLibreDTO,
     * or with status {@code 400 (Bad Request)} if the equipementLibreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the equipementLibreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/equipement-libres/{id}")
    public ResponseEntity<EquipementLibreDTO> updateEquipementLibre(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EquipementLibreDTO equipementLibreDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EquipementLibre : {}, {}", id, equipementLibreDTO);
        if (equipementLibreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, equipementLibreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!equipementLibreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EquipementLibreDTO result = equipementLibreService.save(equipementLibreDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, equipementLibreDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /equipement-libres/:id} : Partial updates given fields of an existing equipementLibre, field will ignore if it is null
     *
     * @param id the id of the equipementLibreDTO to save.
     * @param equipementLibreDTO the equipementLibreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipementLibreDTO,
     * or with status {@code 400 (Bad Request)} if the equipementLibreDTO is not valid,
     * or with status {@code 404 (Not Found)} if the equipementLibreDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the equipementLibreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/equipement-libres/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<EquipementLibreDTO> partialUpdateEquipementLibre(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EquipementLibreDTO equipementLibreDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EquipementLibre partially : {}, {}", id, equipementLibreDTO);
        if (equipementLibreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, equipementLibreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!equipementLibreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EquipementLibreDTO> result = equipementLibreService.partialUpdate(equipementLibreDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, equipementLibreDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /equipement-libres} : get all the equipementLibres.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of equipementLibres in body.
     */
    @GetMapping("/equipement-libres")
    public List<EquipementLibreDTO> getAllEquipementLibres() {
        log.debug("REST request to get all EquipementLibres");
        return equipementLibreService.findAll();
    }

    /**
     * {@code GET  /equipement-libres/:id} : get the "id" equipementLibre.
     *
     * @param id the id of the equipementLibreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the equipementLibreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/equipement-libres/{id}")
    public ResponseEntity<EquipementLibreDTO> getEquipementLibre(@PathVariable Long id) {
        log.debug("REST request to get EquipementLibre : {}", id);
        Optional<EquipementLibreDTO> equipementLibreDTO = equipementLibreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(equipementLibreDTO);
    }

    /**
     * {@code DELETE  /equipement-libres/:id} : delete the "id" equipementLibre.
     *
     * @param id the id of the equipementLibreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/equipement-libres/{id}")
    public ResponseEntity<Void> deleteEquipementLibre(@PathVariable Long id) {
        log.debug("REST request to delete EquipementLibre : {}", id);
        equipementLibreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
