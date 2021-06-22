package com.meetingplanner.domain.enumeration;

import com.meetingplanner.domain.EquipementLibre;

import java.util.HashSet;
import java.util.Set;

/**
 * The TypeReunion enumeration.
 */
public enum TypeReunion {
    VC,
    SPEC,
    RS,
    RC;

    public static Set<TypeEquipement> getEquipementParTypeReunion(TypeReunion typeReunion) {
        Set<TypeEquipement> equipements = new HashSet<>();
        switch (typeReunion) {
            case VC:
                equipements.add(TypeEquipement.ECRAN);
                equipements.add(TypeEquipement.PIEUVRE);
                equipements.add(TypeEquipement.WEBCAM);
                break;
            case SPEC:
                equipements.add(TypeEquipement.TABLEAU);
                break;
            case RC:
                equipements.add(TypeEquipement.TABLEAU);
                equipements.add(TypeEquipement.ECRAN);
                equipements.add(TypeEquipement.PIEUVRE);
                break;
            default:
        }
        return equipements;
    }

}
