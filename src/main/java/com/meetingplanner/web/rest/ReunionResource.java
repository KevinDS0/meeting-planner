package com.meetingplanner.web.rest;

import com.meetingplanner.repository.ReunionRepository;
import com.meetingplanner.service.ReunionService;
import com.meetingplanner.service.dto.ReservationDTO;
import com.meetingplanner.service.dto.ReunionDTO;
import com.meetingplanner.service.dto.SalleDTO;
import com.meetingplanner.web.rest.errors.BadRequestAlertException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.meetingplanner.domain.Reunion}.
 */
@RestController
@RequestMapping("/api")
public class ReunionResource {

    private final Logger log = LoggerFactory.getLogger(ReunionResource.class);

    private static final String ENTITY_NAME = "reunion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReunionService reunionService;

    private final ReunionRepository reunionRepository;

    public ReunionResource(ReunionService reunionService, ReunionRepository reunionRepository) {
        this.reunionService = reunionService;
        this.reunionRepository = reunionRepository;
    }

    /**
     * {@code POST  /reunions} : Create a new reunion.
     *
     * @param reunionDTO the reunionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reunionDTO, or with status {@code 400 (Bad Request)} if the reunion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reunions")
    public ResponseEntity<ReunionDTO> createReunion(@RequestBody ReunionDTO reunionDTO) throws URISyntaxException {
        log.debug("REST request to save Reunion : {}", reunionDTO);
        if (reunionDTO.getId() != null) {
            throw new BadRequestAlertException("A new reunion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReunionDTO result = reunionService.save(reunionDTO);
        return ResponseEntity
            .created(new URI("/api/reunions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reunions/:id} : Updates an existing reunion.
     *
     * @param id         the id of the reunionDTO to save.
     * @param reunionDTO the reunionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reunionDTO,
     * or with status {@code 400 (Bad Request)} if the reunionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reunionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reunions/{id}")
    public ResponseEntity<ReunionDTO> updateReunion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReunionDTO reunionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Reunion : {}, {}", id, reunionDTO);
        if (reunionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reunionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reunionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReunionDTO result = reunionService.save(reunionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reunionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reunions/:id} : Partial updates given fields of an existing reunion, field will ignore if it is null
     *
     * @param id         the id of the reunionDTO to save.
     * @param reunionDTO the reunionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reunionDTO,
     * or with status {@code 400 (Bad Request)} if the reunionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reunionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reunionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reunions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ReunionDTO> partialUpdateReunion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReunionDTO reunionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Reunion partially : {}, {}", id, reunionDTO);
        if (reunionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reunionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reunionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReunionDTO> result = reunionService.partialUpdate(reunionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reunionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reunions} : get all the reunions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reunions in body.
     */
    @GetMapping("/reunions")
    public List<ReunionDTO> getAllReunions() {
        log.debug("REST request to get all Reunions");
        return reunionService.findAll();
    }

    /**
     * {@code GET  /reunions/:id} : get the "id" reunion.
     *
     * @param id the id of the reunionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reunionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reunions/{id}")
    public ResponseEntity<ReunionDTO> getReunion(@PathVariable Long id) {
        log.debug("REST request to get Reunion : {}", id);
        Optional<ReunionDTO> reunionDTO = reunionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reunionDTO);
    }

    /**
     * {@code DELETE  /reunions/:id} : delete the "id" reunion.
     *
     * @param id the id of the reunionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reunions/{id}")
    public ResponseEntity<Void> deleteReunion(@PathVariable Long id) {
        log.debug("REST request to delete Reunion : {}", id);
        reunionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @ApiOperation(value = "Réserve une salle pour une réunion et renvoie la salle sélectionnée")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok, retour de la salle réservée"), @ApiResponse(code = 500, message = "Internal server error"), @ApiResponse(code = 404, message = "Not found")})
    @PostMapping("/reunion/reserver")
    public ResponseEntity<SalleDTO> reserverSalleReunion(@RequestBody ReservationDTO reservation) {
        log.debug("REST request pour la réservation d'une salle pour une réunion : {} {} {}", reservation.getTypeReunion(), reservation.getNbParticipants(), reservation.getCreneau());
        try {
            SalleDTO result = reunionService.getSalleAdapteeDisponible(reservation);
            return ResponseEntity.ok(result);
        } catch (NoSuchElementException e) {
            log.debug("Aucune salle disponible pour cette réunion");
            return ResponseEntity.noContent().build();
        }
    }
}
