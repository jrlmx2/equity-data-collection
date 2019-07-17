package com.rawstocktechnologies.portfoliomanager.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="equity_data")
@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class EquityData {

    @EmbeddedId
    @Column
    private DataIdentifier equity;

    @Column
    private String url;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private String chart;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private String stats;

    @Column
    @UpdateTimestamp
    private LocalDateTime updated;
}
