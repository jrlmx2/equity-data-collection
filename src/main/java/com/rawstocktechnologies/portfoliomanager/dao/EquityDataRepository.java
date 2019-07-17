package com.rawstocktechnologies.portfoliomanager.dao;

import com.rawstocktechnologies.portfoliomanager.model.DataIdentifier;
import com.rawstocktechnologies.portfoliomanager.model.EquityData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquityDataRepository extends CrudRepository<EquityData, DataIdentifier> {
}
