package com.zmy.gemdealer.services;

import com.zmy.gemdealer.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PlayerService {

    @Autowired
    private Map<String, Player> playerMap;

    public boolean playerExisted(String name) {
        return playerMap.get(name) != null;
    }

    public Player getOrCreatePlayerByName(String name, String sessionId) {
        if (playerExisted(name)) {
            return playerMap.get(name);
        }
        Player player = new Player(name, sessionId);
        playerMap.put(name, player);
        return player;
    }

}
