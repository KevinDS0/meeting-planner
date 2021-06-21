package com.meetingplanner.service.dto;

import com.meetingplanner.domain.enumeration.Creneau;
import com.meetingplanner.domain.enumeration.TypeReunion;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.meetingplanner.domain.Reunion} entity.
 */
public class ReunionDTO implements Serializable {

    private Long id;

    private TypeReunion type;

    private Creneau creneau;

    private SalleDTO salle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeReunion getType() {
        return type;
    }

    public void setType(TypeReunion type) {
        this.type = type;
    }

    public Creneau getCreneau() {
        return creneau;
    }

    public void setCreneau(Creneau creneau) {
        this.creneau = creneau;
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
        if (!(o instanceof ReunionDTO)) {
            return false;
        }

        ReunionDTO reunionDTO = (ReunionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reunionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReunionDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", creneau='" + getCreneau() + "'" +
            ", salle=" + getSalle() +
            "}";
    }
}
