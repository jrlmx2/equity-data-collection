package com.rawstocktechnologies.portfoliomanager.model.iex;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="iex_symbols")
public class IEXSymbolCompany {
    @Id
    private String symbol;
    private String companyName;
    private String exchange;
    private String industry;
    private String website;
    private String description;
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
}
