package org.jacksonlaboratory.model;

import io.micronaut.serde.annotation.Serdeable;
import org.monarchinitiative.phenol.annotations.formats.GeneIdentifier;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Serdeable
@Entity
@Table(name = "gene")
public class Gene {

    @Id
    private int id;

    private String symbol;
    public Gene(){}

    public Gene(GeneIdentifier geneIdentifier){
        this.id = Integer.parseInt(geneIdentifier.id().getId());
        this.symbol = geneIdentifier.symbol();
    }
}
