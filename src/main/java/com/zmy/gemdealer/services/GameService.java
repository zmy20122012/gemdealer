package com.zmy.gemdealer.services;

import com.zmy.gemdealer.config.InvestorsAndAssets;
import com.zmy.gemdealer.model.Game;
import com.zmy.gemdealer.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GameService {
    @Autowired
    private Map<String, Player> playerMap;
    @Autowired
    private Map<String, Game> gameMap;
    @Autowired
    InvestorsAndAssets investorsAndAssets;

    public void joinGame(String playerName, String gameId) {
        Player player = playerMap.get(playerName);
        Game game = gameMap.get(gameId);
        boolean addSuccess = game.addPlayer(player);
        if (!addSuccess) {
            throw new RuntimeException("玩家以达到上限");
        }
    }

    public void startGame(String gameId) {
        InvestorsAndAssets cloned = (InvestorsAndAssets) investorsAndAssets.clone();
        Game game = gameMap.get(gameId);
        game.start(cloned);
    }

    public Game getGameInfo(String gameId) {
        return gameMap.get(gameId);
    }

    public int pick(String gameId, String name, Integer diamond, Integer blue, Integer green, Integer red, Integer black) {
        diamond = diamond == null ? 0 : diamond;
        blue = blue == null ? 0 : blue;
        green = green == null ? 0 : green;
        red = red == null ? 0 : red;
        black = black == null ? 0 : black;

        Game game = gameMap.get(gameId);
        return game.pick(name, diamond, blue, green, red, black);
    }

    public int sendBackGems(String gameId, String name, Integer gold, Integer diamond, Integer blue, Integer green, Integer red, Integer black) {
        gold = gold == null ? 0 : gold;
        diamond = diamond == null ? 0 : diamond;
        blue = blue == null ? 0 : blue;
        green = green == null ? 0 : green;
        red = red == null ? 0 : red;
        black = black == null ? 0 : black;

        Game game = gameMap.get(gameId);
        return game.sendBackGems(name, gold, diamond, blue, green, red, black);
    }

    public int bookAsset(String gameId, String name, int level, int assetIdx) {
        Game game = gameMap.get(gameId);
        return game.bookAsset(name, level, assetIdx);
    }

    public List<Integer> exchangeBookedAsset(String gameId, String name, int bookedAssetIdx) {
        Game game = gameMap.get(gameId);
        return game.exchangeBookedAsset(name, bookedAssetIdx);
    }

    public List<Integer> exchangePublicAsset(String gameId, String name, int level, int assetIdx) {
        Game game = gameMap.get(gameId);
        return game.exchangePublicAsset(name, level, assetIdx);
    }

    public boolean chooseInvestor(String gameId, String name, int chosenIndex) {
        Game game = gameMap.get(gameId);
        return game.chooseInvestor(name,chosenIndex);
    }
}
