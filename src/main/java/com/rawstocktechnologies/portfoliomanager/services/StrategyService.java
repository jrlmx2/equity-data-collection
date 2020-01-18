package com.rawstocktechnologies.portfoliomanager.services;


import com.rawstocktechnologies.portfoliomanager.model.Position;
import com.rawstocktechnologies.portfoliomanager.model.strategy.Strategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StrategyService {

    private void runStrategy(String name) {

        Strategy strategy = null;// = strategies.findByName(name);
        if(strategy == null){
            throw new IllegalStateException("No strategy by the name "+name+" exists");
        }

        // 0. decode parameters
        //StartegyDetails stratgey = strategy.getDetails();

        // 1. Manage current positions
        for(Position currentPosition : strategy.getPositions()){
        }

        // 2. filter for new ideas

        // 3. size positions based on ideas and strategy limit

        // 4. open new positions
    }
}
