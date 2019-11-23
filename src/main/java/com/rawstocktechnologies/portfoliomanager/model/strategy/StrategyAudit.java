package com.rawstocktechnologies.portfoliomanager.model.strategy;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="stategies_audit")
public class StrategyAudit {

    @Id
    private String id;

    @ManyToOne
    @JsonManagedReference
    private Strategy strategy;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="name_before")
    private String nameBefore;

    @Column(name="name_after")
    private String nameAfter;

    @Column(name="strategy_before")
    private String strategyBefore;

    @Column(name="name_after")
    private String strategyAfter;

    @Column(name="margin_before")
    private Boolean marginBefore;

    @Column(name="margin_after")
    private Boolean marginAfter;
}

