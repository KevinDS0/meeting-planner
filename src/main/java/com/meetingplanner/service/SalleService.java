package com.meetingplanner.service;

import com.meetingplanner.config.ApplicationProperties;
import com.meetingplanner.domain.EquipementSalle;
import com.meetingplanner.domain.Salle;
import com.meetingplanner.domain.enumeration.TypeReunion;
import com.meetingplanner.repository.SalleRepository;
import com.meetingplanner.service.dto.SalleDTO;
import com.meetingplanner.service.mapper.SalleMapper;

import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Salle}.
 */
@Service
@Transactional
public class SalleService {

    private final Logger log = LoggerFactory.getLogger(SalleService.class);

    private final SalleRepository salleRepository;

    private final SalleMapper salleMapper;

    private final ApplicationProperties applicationProperties;

    public SalleService(SalleRepository salleRepository, SalleMapper salleMapper, ApplicationProperties applicationProperties) {
        this.salleRepository = salleRepository;
        this.salleMapper = salleMapper;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Save a salle.
     *
     * @param salleDTO the entity to save.
     * @return the persisted entity.
     */
    public SalleDTO save(SalleDTO salleDTO) {
        log.debug("Request to save Salle : {}", salleDTO);
        Salle salle = salleMapper.toEntity(salleDTO);
        salle = salleRepository.save(salle);
        return salleMapper.toDto(salle);
    }

    /**
     * Partially update a salle.
     *
     * @param salleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SalleDTO> partialUpdate(SalleDTO salleDTO) {
        log.debug("Request to partially update Salle : {}", salleDTO);

        return salleRepository
            .findById(salleDTO.getId())
            .map(
                existingSalle -> {
                    salleMapper.partialUpdate(existingSalle, salleDTO);

                    return existingSalle;
                }
            )
            .map(salleRepository::save)
            .map(salleMapper::toDto);
    }

    /**
     * Get all the salles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SalleDTO> findAll() {
        log.debug("Request to get all Salles");
        return salleRepository.findAll().stream().map(salleMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one salle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SalleDTO> findOne(Long id) {
        log.debug("Request to get Salle : {}", id);
        return salleRepository.findById(id).map(salleMapper::toDto);
    }

    /**
     * Delete the salle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Salle : {}", id);
        salleRepository.deleteById(id);
    }

    public Set<Salle> getSalleCapaciteAdaptee(Integer nbParticipants) {
        return salleRepository.findAll().stream()
            .filter(salle -> Math.ceil(salle.getCapacite() * Math.floorDiv(applicationProperties.getPourcentageCapacite(),100)) >= nbParticipants)
            .collect(Collectors.toSet());
    }

    public Boolean verifierEquipementsSalle(Salle salle, TypeReunion typeReunion) {
        return salle.getEquipementSalles().stream()
            .map(EquipementSalle::getType)
            .collect(Collectors.toSet()).containsAll(TypeReunion.getEquipementParTypeReunion(typeReunion));
    }
}
