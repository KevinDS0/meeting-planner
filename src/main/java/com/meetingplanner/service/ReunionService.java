package com.meetingplanner.service;

import com.meetingplanner.domain.Reunion;
import com.meetingplanner.domain.Salle;
import com.meetingplanner.repository.ReunionRepository;
import com.meetingplanner.service.dto.ReservationDTO;
import com.meetingplanner.service.dto.ReunionDTO;
import com.meetingplanner.service.dto.SalleDTO;
import com.meetingplanner.service.mapper.ReunionMapper;
import com.meetingplanner.service.mapper.SalleMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Reunion}.
 */
@Service
@Transactional
public class ReunionService {

    public static final String AUCUNE_SALLE_DISPONIBLE = "Aucune salle disponible";
    private final Logger log = LoggerFactory.getLogger(ReunionService.class);

    private final ReunionRepository reunionRepository;

    private final ReunionMapper reunionMapper;

    private final SalleService salleService;

    private final SalleMapper salleMapper;

    public ReunionService(ReunionRepository reunionRepository, ReunionMapper reunionMapper, SalleService salleService, SalleMapper salleMapper) {
        this.reunionRepository = reunionRepository;
        this.reunionMapper = reunionMapper;
        this.salleService = salleService;
        this.salleMapper = salleMapper;
    }

    /**
     * Save a reunion.
     *
     * @param reunionDTO the entity to save.
     * @return the persisted entity.
     */
    public ReunionDTO save(ReunionDTO reunionDTO) {
        log.debug("Request to save Reunion : {}", reunionDTO);
        Reunion reunion = reunionMapper.toEntity(reunionDTO);
        reunion = reunionRepository.save(reunion);
        return reunionMapper.toDto(reunion);
    }

    /**
     * Partially update a reunion.
     *
     * @param reunionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReunionDTO> partialUpdate(ReunionDTO reunionDTO) {
        log.debug("Request to partially update Reunion : {}", reunionDTO);

        return reunionRepository
            .findById(reunionDTO.getId())
            .map(
                existingReunion -> {
                    reunionMapper.partialUpdate(existingReunion, reunionDTO);

                    return existingReunion;
                }
            )
            .map(reunionRepository::save)
            .map(reunionMapper::toDto);
    }

    /**
     * Get all the reunions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReunionDTO> findAll() {
        log.debug("Request to get all Reunions");
        return reunionRepository.findAll().stream().map(reunionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one reunion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReunionDTO> findOne(Long id) {
        log.debug("Request to get Reunion : {}", id);
        return reunionRepository.findById(id).map(reunionMapper::toDto);
    }

    /**
     * Delete the reunion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Reunion : {}", id);
        reunionRepository.deleteById(id);
    }

    public SalleDTO getSalleAdapteeDisponible(ReservationDTO reservation) {
        // TODO : Ajouter le jour de la semaine de la r??union pour pouvoir r??server des salles sur les autres jours de la semaine
        Set<Long> identifiantsSallesNonDisponibles = reunionRepository.findAllReunionsByCreneauIn(Set.of(reservation.getCreneau(), Objects.requireNonNull(reservation.getCreneau().getPrecedentCreneau(reservation.getCreneau()))))
            .stream()
            .map(Reunion::getSalle)
            .map(Salle::getId)
            .collect(Collectors.toSet());

        Set<Salle> sallesCapaciteAdaptee = salleService.getSalleCapaciteAdaptee(reservation.getNbParticipants());

        Set<Salle> sallesAdapteeAvecEquipementNecessaire = sallesCapaciteAdaptee.stream().filter(salle -> {
            return salleService.verifierEquipementsSalle(salle, reservation.getTypeReunion());
        }).collect(Collectors.toSet());


        if (CollectionUtils.isNotEmpty(sallesAdapteeAvecEquipementNecessaire)) {
            return salleMapper.toDto(sallesAdapteeAvecEquipementNecessaire.stream()
                .filter(salle -> !identifiantsSallesNonDisponibles.contains(salle.getId()))
                .min(Comparator.comparing(Salle::getCapacite)).orElseThrow(() -> new NoSuchElementException(AUCUNE_SALLE_DISPONIBLE)));
        } else {
            throw new NoSuchElementException(AUCUNE_SALLE_DISPONIBLE);
        }
    }
}
