package com.rawstocktechnologies.portfoliomanager.dao;

import com.rawstocktechnologies.portfoliomanager.model.strategy.Strategy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StrategyRepository extends CrudRepository<Strategy,String> {
    Strategy findByName(String name);
}
