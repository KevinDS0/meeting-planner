package com.meetingplanner.service.mapper;

import com.meetingplanner.domain.*;
import com.meetingplanner.service.dto.EquipementLibreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EquipementLibre} and its DTO {@link EquipementLibreDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EquipementLibreMapper extends EntityMapper<EquipementLibreDTO, EquipementLibre> {}
