package com.zmy.gemdealer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private String name;
    private String sessionId;

    private int golds;
    private int diamonds;
    private int blueGems;
    private int greenGems;
    private int redGems;
    private int blackGems;

    private List<Asset> diamondAssets;
    private List<Asset> blueAssets;
    private List<Asset> greenAssets;
    private List<Asset> redAssets;
    private List<Asset> blackAssets;
    private List<Investor> investors;

    private List<Asset> bookedAssets;

    private int score;

    public Player(String name, String sessionId) {
        this.name = name;
        this.sessionId = sessionId;
        this.diamondAssets = new LinkedList<>();
        this.blueAssets = new LinkedList<>();
        this.greenAssets = new LinkedList<>();
        this.redAssets = new LinkedList<>();
        this.blackAssets = new LinkedList<>();
        this.investors = new LinkedList<>();
        this.bookedAssets = new LinkedList<>();
    }

    public void addGem(int diamond, int blue, int green, int red, int black) {
        this.diamonds += diamond;
        this.blueGems += blue;
        this.greenGems += green;
        this.redGems += red;
        this.blackGems += black;
    }

    public int needSendBack() {
        return this.golds + this.diamonds + this.blueGems + this.greenGems + this.redGems + this.blackGems - 10;
    }

    public void reduceGem(int gold, int diamond, int blue, int green, int red, int black) {
        this.golds -= gold;
        this.diamonds -= diamond;
        this.blueGems -= blue;
        this.greenGems -= green;
        this.redGems -= red;
        this.blackGems -= black;
    }

    public boolean canNotBook() {
        return this.bookedAssets.size() >= 3;
    }

    public void addBookedAsset(Asset book) {
        if (canNotBook()) {
            throw new RuntimeException("over book asset limit. current booked Asset list size:" + this.bookedAssets.size());
        }
        this.bookedAssets.add(book);
        this.golds += 1;
    }

    public int[] exchange(Requirement requirement) {
        int[] requiredDiamonds = exchangeItem(requirement.getDiamonds(), this.diamonds, this.diamondAssets.size());
        int[] requiredBlueGems = exchangeItem(requirement.getBlueGems(), this.blueGems, this.blueAssets.size());
        int[] requiredGreenGems = exchangeItem(requirement.getGreenGems(), this.greenGems, this.greenAssets.size());
        int[] requiredRedGems = exchangeItem(requirement.getRedGems(), this.redGems, this.redAssets.size());
        int[] requiredBlackGems = exchangeItem(requirement.getBlackGems(), this.blackGems, this.blackAssets.size());
        int requiredGold = requiredDiamonds[1] + requiredBlueGems[1] + requiredGreenGems[1] + requiredRedGems[1] + requiredBlackGems[1];
        if (requiredGold > this.golds) {
            throw new RuntimeException("资源不够兑换");
        }
        reduceGem(requiredGold, requiredDiamonds[0], requiredBlueGems[0], requiredGreenGems[0], requiredRedGems[0], requiredBlackGems[0]);

        return new int[]{requiredGold, requiredDiamonds[0], requiredBlueGems[0], requiredGreenGems[0], requiredRedGems[0], requiredBlackGems[0]};
    }

    private int[] exchangeItem(int target, int holdingGems, int holdingAssetSum) {
        int needGems = target - holdingAssetSum;
        if (needGems < 0) {
            return new int[]{0, 0};
        }
        if (needGems > holdingGems) {
            int needGold = (needGems - holdingGems);
            needGems = holdingGems;
            return new int[]{needGems, needGold};
        }
        return new int[]{needGems, 0};
    }

    public int[] exchangeBookedAsset(int bookedAssetIdx) {
        Asset asset = this.bookedAssets.get(bookedAssetIdx);
        Requirement requirement = asset.getRequirement();
        int[] exchangeRequirement = exchange(requirement);
        addAsset(asset);
        return exchangeRequirement;
    }

    public void addAsset(Asset asset) {
        this.score += asset.getGoal();
        switch (asset.getValue()) {
            case DIAMOND:
                this.getDiamondAssets().add(asset);
                break;
            case BLUE_GEM:
                this.getBlueAssets().add(asset);
                break;
            case GREEN_GEM:
                this.getGreenAssets().add(asset);
                break;
            case RED_GEM:
                this.getRedAssets().add(asset);
                break;
            case BLACK_GEM:
                this.getBlackAssets().add(asset);
                break;
            default:
                throw new RuntimeException("UNKNOW GEM TYPE");
        }
    }

    public void addInvestor(Investor investor) {
        this.investors.add(investor);
        this.score += investor.getGoal();
    }
}
