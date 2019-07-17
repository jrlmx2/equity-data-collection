package com.rawstocktechnologies.portfoliomanager.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Data
public class DataIdentifier implements Serializable {
    @Enumerated(EnumType.STRING)
    @Column(name="data_source")
    private DataSource dataSource;
    @Column(name="data_source_version")
    private String dataSourceVersion;
    @Column
    private String symbol;
}
