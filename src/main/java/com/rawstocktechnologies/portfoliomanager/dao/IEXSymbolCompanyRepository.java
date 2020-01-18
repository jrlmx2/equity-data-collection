package com.rawstocktechnologies.portfoliomanager.dao;

import com.rawstocktechnologies.portfoliomanager.model.iex.IEXSymbolCompany;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEXSymbolCompanyRepository extends CrudRepository<IEXSymbolCompany, String> {
    List<IEXSymbolCompany> findBySymbolNotIn(String[] symbols);
}
