package com.rawstocktechnologies.portfoliomanager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rawstocktechnologies.portfoliomanager.model.strategy.Strategy;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.TreeSet;

//@Entity
@Data
//@Table(name="positions")
public class Position {
    @Id
    private String id;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name="opened_at")
    private LocalDateTime openedAt;

    @Column(name="exited_at")
    private LocalDateTime exitedAt;

    @Column(name="position_type")
    private String positionType;

    @Column(name="equity_id")
    private String equity_id;

    @Column(name="equity_type")
    private String equity_type;

    @Column(name="entry_strategy")
    private Strategy entryStrategy;

    @Column(name="exit_strategy")
    private Strategy exitStrategy;

    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL)
    @JsonBackReference
    @OrderBy("executedAt DESC")
    private SortedSet<PostionLot> lots = new TreeSet<>();

    @ManyToOne
    @JsonManagedReference
    private Strategy strategy;
}
