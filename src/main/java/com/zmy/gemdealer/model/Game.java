package com.zmy.gemdealer.model;

import com.zmy.gemdealer.config.InvestorsAndAssets;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Game {
    private final String name;
    private final List<Player> players;
    private int round;
    private int firstPlayerIdx;
    private int turnOfPlayerIdx;
    private OptionalOperation currentOptionalOperation;
    private volatile GemPool gems;
    private volatile InvestorPool investors;
    private volatile AssetPool assets;

    public Game(String name) {
        this.name = name;
        this.players = new ArrayList<>(4);
    }

    public boolean addPlayer(Player player) {
        if (this.players.size() < 4) {
            synchronized (this.players) {
                if (this.players.size() < 4) {
                    players.add(player);
                    return true;
                }
            }
        }
        return false;
    }

    public void start(InvestorsAndAssets investorsAndAssetsConfig) {
        initializeGame(players.size(), investorsAndAssetsConfig);
        this.round = 1;
        this.turnOfPlayerIdx = this.firstPlayerIdx = 0;
        this.currentOptionalOperation = OptionalOperation.PICK_OR_EXCHANGE_OR_BOOK;
    }

    public void initializeGame(int playerNumbers, InvestorsAndAssets investorsAndAssetsConfig) {
        this.gems = new GemPool(playerNumbers);
        this.investors = new InvestorPool(playerNumbers, investorsAndAssetsConfig.getInvestors());
        this.assets = new AssetPool(investorsAndAssetsConfig.getLowAsset(), investorsAndAssetsConfig.getMediumAsset(), investorsAndAssetsConfig.getHighAsset());
    }

    public int pick(String name, int diamond, int blue, int green, int red, int black) {
        Player currentPlayer = getTurnOfPlayer(name);

        this.gems.reduce(diamond, blue, green, red, black);
        currentPlayer.addGem(diamond, blue, green, red, black);

        int needSendBackCount = currentPlayer.needSendBack();
        if (needSendBackCount > 0) {
            this.currentOptionalOperation = OptionalOperation.SEND_BACK;
            return needSendBackCount;
        }
        turnToNextPlayer();
        return 0;
    }

    public int sendBackGems(String name, int gold, int diamond, int blue, int green, int red, int black) {
        Player currentPlayer = getTurnOfPlayer(name);

        currentPlayer.reduceGem(gold, diamond, blue, green, red, black);
        this.gems.add(gold, diamond, blue, green, red, black);

        int needSendBackCount = currentPlayer.needSendBack();
        if (needSendBackCount > 0) {
            this.currentOptionalOperation = OptionalOperation.SEND_BACK;
            return needSendBackCount;
        }
        turnToNextPlayer();
        return 0;
    }

    public int bookAsset(String name, int level, int assetIdx) {
        Player currentPlayer = getTurnOfPlayer(name);

        if (currentPlayer.canNotBook()) {
            throw new RuntimeException("currentPlayer can not book asset. over book asset limit ");
        }
        if (this.gems.getGolds() == 0) {
            throw new RuntimeException("currentPlayer can not book asset. there is no gold.");
        }
        // todo rollback when exception
        Asset book = this.assets.book(level, assetIdx);
        this.gems.reduceGold();
        currentPlayer.addBookedAsset(book);

        int needSendBackCount = currentPlayer.needSendBack();
        if (needSendBackCount > 0) {
            this.currentOptionalOperation = OptionalOperation.SEND_BACK;
            return needSendBackCount;
        }
        turnToNextPlayer();
        return 0;
    }

    public List<Integer> exchangeBookedAsset(String name, int bookedAssetIdx) {
        Player currentPlayer = getTurnOfPlayer(name);

        int[] playerCost = currentPlayer.exchangeBookedAsset(bookedAssetIdx);
        this.gems.add(playerCost[0], playerCost[1], playerCost[2], playerCost[3], playerCost[4], playerCost[5]);

        List<Integer> canBeInvitedInvestors = this.investors.getCanBeInvitedInvestors(currentPlayer);

        if (canBeInvitedInvestors.size() > 1) {
            this.currentOptionalOperation = OptionalOperation.CHOOSE_INVESTOR;
            return canBeInvitedInvestors;
        }
        currentPlayer.addInvestor(this.investors.getInvestors().remove((int) canBeInvitedInvestors.get(0)));
        turnToNextPlayer();
        return canBeInvitedInvestors;
    }

    public List<Integer> exchangePublicAsset(String name, int level, int assetIdx) {
        Player currentPlayer = getTurnOfPlayer(name);

        int[] playerCost = this.assets.exchange(currentPlayer, level, assetIdx);
        this.gems.add(playerCost[0], playerCost[1], playerCost[2], playerCost[3], playerCost[4], playerCost[5]);

        List<Integer> canBeInvitedInvestors = this.investors.getCanBeInvitedInvestors(currentPlayer);

        if (canBeInvitedInvestors.size() > 1) {
            this.currentOptionalOperation = OptionalOperation.CHOOSE_INVESTOR;
            return canBeInvitedInvestors;
        }
        currentPlayer.addInvestor(this.investors.getInvestors().remove((int) canBeInvitedInvestors.get(0)));
        turnToNextPlayer();
        return canBeInvitedInvestors;
    }

    public boolean chooseInvestor(String name, int chosenIndex) {
        Player currentPlayer = getTurnOfPlayer(name);
        List<Integer> canBeInvitedInvestors = this.investors.getCanBeInvitedInvestors(currentPlayer);
        if (!canBeInvitedInvestors.contains(chosenIndex)) {
            return false;
        }
        currentPlayer.addInvestor(this.investors.getInvestors().remove(chosenIndex));
        turnToNextPlayer();
        return true;
    }

    private Player getTurnOfPlayer(String name) {
        Player turnOfPlayer = this.players.get(this.turnOfPlayerIdx);
        if (!name.equals(turnOfPlayer.getName())) {
            throw new RuntimeException("未到用户『" + name + " 』操作的回合");
        }
        return turnOfPlayer;
    }

    private void turnToNextPlayer() {
        int next = this.turnOfPlayerIdx + 1;
        if (next > this.players.size()) {
            this.round++;
        }
        this.turnOfPlayerIdx = next % this.players.size();
        this.currentOptionalOperation = OptionalOperation.PICK_OR_EXCHANGE_OR_BOOK;
    }

    public static enum OptionalOperation {
        PICK_OR_EXCHANGE_OR_BOOK, SEND_BACK, CHOOSE_INVESTOR;
    }

}