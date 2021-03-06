package com.meetingplanner.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.meetingplanner.domain.enumeration.Creneau;
import com.meetingplanner.domain.enumeration.TypeReunion;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Reunion.
 */
@Entity
@Table(name = "reunion")
public class Reunion implements Serializable {

    private static final long serialVersionUID = -8883500677335551616L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeReunion type;

    @Enumerated(EnumType.STRING)
    @Column(name = "creneau")
    private Creneau creneau;

    @OneToMany(mappedBy = "reunion")
    @JsonIgnoreProperties(value = { "reunion" }, allowSetters = true)
    private Set<EquipementLibre> equipementLibres = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "equipementSalles", "reunions" }, allowSetters = true)
    private Salle salle;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reunion id(Long id) {
        this.id = id;
        return this;
    }

    public TypeReunion getType() {
        return this.type;
    }

    public Reunion type(TypeReunion type) {
        this.type = type;
        return this;
    }

    public void setType(TypeReunion type) {
        this.type = type;
    }

    public Creneau getCreneau() {
        return this.creneau;
    }

    public Reunion creneau(Creneau creneau) {
        this.creneau = creneau;
        return this;
    }

    public void setCreneau(Creneau creneau) {
        this.creneau = creneau;
    }

    public Set<EquipementLibre> getEquipementLibres() {
        return this.equipementLibres;
    }

    public Reunion equipementLibres(Set<EquipementLibre> equipementLibres) {
        this.setEquipementLibres(equipementLibres);
        return this;
    }

    public Reunion addEquipementLibre(EquipementLibre equipementLibre) {
        this.equipementLibres.add(equipementLibre);
        equipementLibre.setReunion(this);
        return this;
    }

    public Reunion removeEquipementLibre(EquipementLibre equipementLibre) {
        this.equipementLibres.remove(equipementLibre);
        equipementLibre.setReunion(null);
        return this;
    }

    public void setEquipementLibres(Set<EquipementLibre> equipementLibres) {
        if (this.equipementLibres != null) {
            this.equipementLibres.forEach(i -> i.setReunion(null));
        }
        if (equipementLibres != null) {
            equipementLibres.forEach(i -> i.setReunion(this));
        }
        this.equipementLibres = equipementLibres;
    }

    public Salle getSalle() {
        return this.salle;
    }

    public Reunion salle(Salle salle) {
        this.setSalle(salle);
        return this;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reunion)) {
            return false;
        }
        return id != null && id.equals(((Reunion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reunion{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", creneau='" + getCreneau() + "'" +
            "}";
    }
}
