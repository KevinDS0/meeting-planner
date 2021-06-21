package com.meetingplanner.service.dto;

import com.meetingplanner.domain.enumeration.TypeEquipement;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.meetingplanner.domain.EquipementSalle} entity.
 */
public class EquipementSalleDTO implements Serializable {

    private Long id;

    private TypeEquipement type;

    private SalleDTO salle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeEquipement getType() {
        return type;
    }

    public void setType(TypeEquipement type) {
        this.type = type;
    }

    public SalleDTO getSalle() {
        return salle;
    }

    public void setSalle(SalleDTO salle) {
        this.salle = salle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquipementSalleDTO)) {
            return false;
        }

        EquipementSalleDTO equipementSalleDTO = (EquipementSalleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, equipementSalleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipementSalleDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", salle=" + getSalle() +
            "}";
    }
}
