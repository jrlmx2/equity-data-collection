package com.rawstocktechnologies.portfoliomanager.model.iex;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;

@Data
@Entity
@Table(name="iex_symbols")
@TypeDefs({
        @TypeDef(
                name = "string-array",
                typeClass = StringArrayType.class
        )})
public class IEXSymbolCompany {
    @Id
    private String symbol;
    private String companyName;
    private String exchange;
    private String industry;
    private String website;
    private String description;
    @Column(name="ceo")
    @JsonProperty("CEO")
    private String CEO;
    private String securityName;
    private String issueType;
    private String sector;
    private Integer primarySicCode;
    private Integer employees;

    @Type( type = "string-array" )
    @Column(
            name = "tags",
            columnDefinition = "text[]"
    )
    private String[] tags;
    private String address;
    private String address2;
    private String state;
    private String city;
    private String zip;
    private String country;
    private String phone;
    private Timestamp updatedAt = Timestamp.from(Instant.now());
}
