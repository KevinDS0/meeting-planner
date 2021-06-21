package com.meetingplanner.web.rest;

import com.meetingplanner.repository.EquipementSalleRepository;
import com.meetingplanner.service.EquipementSalleService;
import com.meetingplanner.service.dto.EquipementSalleDTO;
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
 * REST controller for managing {@link com.meetingplanner.domain.EquipementSalle}.
 */
@RestController
@RequestMapping("/api")
public class EquipementSalleResource {

    private final Logger log = LoggerFactory.getLogger(EquipementSalleResource.class);

    private static final String ENTITY_NAME = "equipementSalle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EquipementSalleService equipementSalleService;

    private final EquipementSalleRepository equipementSalleRepository;

    public EquipementSalleResource(EquipementSalleService equipementSalleService, EquipementSalleRepository equipementSalleRepository) {
        this.equipementSalleService = equipementSalleService;
        this.equipementSalleRepository = equipementSalleRepository;
    }

    /**
     * {@code POST  /equipement-salles} : Create a new equipementSalle.
     *
     * @param equipementSalleDTO the equipementSalleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new equipementSalleDTO, or with status {@code 400 (Bad Request)} if the equipementSalle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/equipement-salles")
    public ResponseEntity<EquipementSalleDTO> createEquipementSalle(@RequestBody EquipementSalleDTO equipementSalleDTO)
        throws URISyntaxException {
        log.debug("REST request to save EquipementSalle : {}", equipementSalleDTO);
        if (equipementSalleDTO.getId() != null) {
            throw new BadRequestAlertException("A new equipementSalle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EquipementSalleDTO result = equipementSalleService.save(equipementSalleDTO);
        return ResponseEntity
            .created(new URI("/api/equipement-salles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /equipement-salles/:id} : Updates an existing equipementSalle.
     *
     * @param id the id of the equipementSalleDTO to save.
     * @param equipementSalleDTO the equipementSalleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipementSalleDTO,
     * or with status {@code 400 (Bad Request)} if the equipementSalleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the equipementSalleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/equipement-salles/{id}")
    public ResponseEntity<EquipementSalleDTO> updateEquipementSalle(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EquipementSalleDTO equipementSalleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EquipementSalle : {}, {}", id, equipementSalleDTO);
        if (equipementSalleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, equipementSalleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!equipementSalleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EquipementSalleDTO result = equipementSalleService.save(equipementSalleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, equipementSalleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /equipement-salles/:id} : Partial updates given fields of an existing equipementSalle, field will ignore if it is null
     *
     * @param id the id of the equipementSalleDTO to save.
     * @param equipementSalleDTO the equipementSalleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipementSalleDTO,
     * or with status {@code 400 (Bad Request)} if the equipementSalleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the equipementSalleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the equipementSalleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/equipement-salles/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<EquipementSalleDTO> partialUpdateEquipementSalle(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EquipementSalleDTO equipementSalleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EquipementSalle partially : {}, {}", id, equipementSalleDTO);
        if (equipementSalleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, equipementSalleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!equipementSalleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EquipementSalleDTO> result = equipementSalleService.partialUpdate(equipementSalleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, equipementSalleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /equipement-salles} : get all the equipementSalles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of equipementSalles in body.
     */
    @GetMapping("/equipement-salles")
    public List<EquipementSalleDTO> getAllEquipementSalles() {
        log.debug("REST request to get all EquipementSalles");
        return equipementSalleService.findAll();
    }

    /**
     * {@code GET  /equipement-salles/:id} : get the "id" equipementSalle.
     *
     * @param id the id of the equipementSalleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the equipementSalleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/equipement-salles/{id}")
    public ResponseEntity<EquipementSalleDTO> getEquipementSalle(@PathVariable Long id) {
        log.debug("REST request to get EquipementSalle : {}", id);
        Optional<EquipementSalleDTO> equipementSalleDTO = equipementSalleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(equipementSalleDTO);
    }

    /**
     * {@code DELETE  /equipement-salles/:id} : delete the "id" equipementSalle.
     *
     * @param id the id of the equipementSalleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/equipement-salles/{id}")
    public ResponseEntity<Void> deleteEquipementSalle(@PathVariable Long id) {
        log.debug("REST request to delete EquipementSalle : {}", id);
        equipementSalleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
