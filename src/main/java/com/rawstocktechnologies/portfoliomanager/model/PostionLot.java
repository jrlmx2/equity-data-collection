package com.rawstocktechnologies.portfoliomanager.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="position_lots")
public class PostionLot {
    @Id
    private String id;

    @Column(name="executed_at")
    private LocalDateTime executedAt;

    @Column(name="price")
    private BigDecimal price = new BigDecimal(0);

    @Column(name="exchange_number")
    private Long exchangeAmount;

    @ManyToOne()
    @JsonManagedReference
    private Position position;
}
