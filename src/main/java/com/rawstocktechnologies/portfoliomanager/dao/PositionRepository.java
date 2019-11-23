package com.rawstocktechnologies.portfoliomanager.dao;

import com.rawstocktechnologies.portfoliomanager.model.Position;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends CrudRepository<Position, String> {
}
