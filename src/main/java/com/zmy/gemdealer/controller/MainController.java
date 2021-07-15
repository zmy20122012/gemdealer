package com.zmy.gemdealer.controller;

import com.zmy.gemdealer.model.Game;
import com.zmy.gemdealer.services.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class MainController {

    @Autowired
    private Map<String, Game> gameMap;

    @Autowired
    private GameService gameService;


    @GetMapping("/game_list_page")
    public ModelAndView games(String name) {
        return new ModelAndView("game_list.html");
    }

    @GetMapping("/games")
    public Map<String, Game> getGameList() {
        return gameMap;
    }

    @GetMapping("/game/create")
    public Map<String, Game> createGame(String name) {
        gameMap.put(name, new Game(name));
        return gameMap;
    }

    @GetMapping("/game/join")
    public void joinGame(@Header("simpSessionId")String playerSessionId, String playerName, String gameId) {
        log.info("player {} joined game {}", playerName, gameId);
        gameService.joinGame(playerSessionId, playerName, gameId);
    }

    @GetMapping("/game/start")
    public void startGame(String gameId) {
        gameService.startGame(gameId);
    }

    @GetMapping("/game")
    public Game getGameInfo(String gameId) {
        return gameService.getGameInfo(gameId);
    }

    @GetMapping("/pick")
    public int pickGem(String gameId, String name, Integer diamond, Integer blue, Integer green, Integer red, Integer black) {
        return gameService.pick(gameId, name, diamond, blue, green, red, black);
    }

    @GetMapping("/send_back_gems")
    public int sendBackGems(String gameId, String name, Integer gold, Integer diamond, Integer blue, Integer green, Integer red, Integer black) {
        return gameService.sendBackGems(gameId, name, gold, diamond, blue, green, red, black);
    }

    @GetMapping("/book")
    public int book(String gameId, String name, int level, int assetIdx) {
        return gameService.bookAsset(gameId, name, level, assetIdx);
    }

    @GetMapping("/exchange")
    public List<Integer> exchange(String gameId, String name, Integer level, Integer assetIdx, Integer bookedAssetIdx) {
        if (bookedAssetIdx != null) {
            return gameService.exchangeBookedAsset(gameId, name, bookedAssetIdx);
        }
        return gameService.exchangePublicAsset(gameId, name, level, assetIdx);
    }

    @GetMapping("/choose_investor")
    public boolean chooseInvestor(String gameId, String name, int chosenIndex){
        return gameService.chooseInvestor(gameId, name, chosenIndex);
    }


}
