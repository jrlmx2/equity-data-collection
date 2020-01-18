package com.rawstocktechnologies.portfoliomanager.model.strategy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rawstocktechnologies.portfoliomanager.model.Position;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

//@Entity
@Data
//@Table(name="strategies")
/*@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})*/
public class Strategy {

    @Id
    @Column
    private String id;

    @Column
    private String name;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private String details;

    @Column
    private Boolean margin = false;

    @Column(name="created_at")
    private LocalDateTime created;

    @Column(name="updated_at")
    private LocalDateTime updated;

    @JsonBackReference
    @OneToMany(mappedBy = "strategy", orphanRemoval = true)
    private Set<Position> positions = new HashSet<>();

    @JsonBackReference
    @OneToMany(mappedBy = "strategy", orphanRemoval = true)
    private Set<StrategyAudit> auditTrail = new HashSet<>();
}

