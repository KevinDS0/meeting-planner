package com.meetingplanner.service.mapper;

import com.meetingplanner.domain.*;
import com.meetingplanner.service.dto.SalleDTO;
import org.mapstruct.*;

import java.util.Set;

/**
 * Mapper for the entity {@link Salle} and its DTO {@link SalleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SalleMapper extends EntityMapper<SalleDTO, Salle> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SalleDTO toDtoId(Salle salle);

    Set<SalleDTO> toDtoSet(Set<Salle> salles);
}
