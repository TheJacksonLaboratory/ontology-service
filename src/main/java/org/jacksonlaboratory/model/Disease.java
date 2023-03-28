package org.jacksonlaboratory.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.micronaut.serde.annotation.Serdeable;
import org.monarchinitiative.phenol.annotations.io.hpo.HpoaDiseaseData;
import org.monarchinitiative.phenol.ontology.data.TermId;

@Serdeable
@Entity
@Table(name = "disease")
public class Disease {
    @Id
    public TermId id;

    public String name;

    public Disease(){
    }

    public Disease(HpoaDiseaseData diseaseData){
        this.id = diseaseData.id();
        this.name = diseaseData.name();

    }
}
