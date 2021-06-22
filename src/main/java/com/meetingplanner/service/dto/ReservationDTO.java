package com.meetingplanner.service.dto;

import com.meetingplanner.domain.enumeration.Creneau;
import com.meetingplanner.domain.enumeration.TypeReunion;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ReservationDTO implements Serializable {

    private static final long serialVersionUID = -8645111172235981676L;

    @NotNull
    private Integer nbParticipants;

    @NotNull
    private Creneau creneau;

    @NotNull
    private TypeReunion typeReunion;

    public Integer getNbParticipants() {
        return nbParticipants;
    }

    public Creneau getCreneau() {
        return creneau;
    }

    public TypeReunion getTypeReunion() {
        return typeReunion;
    }

    public void setNbParticipants(Integer nbParticipants) {
        this.nbParticipants = nbParticipants;
    }

    public void setCreneau(Creneau creneau) {
        this.creneau = creneau;
    }

    public void setTypeReunion(TypeReunion typeReunion) {
        this.typeReunion = typeReunion;
    }
}
