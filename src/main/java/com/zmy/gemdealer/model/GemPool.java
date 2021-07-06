package com.zmy.gemdealer.model;

import lombok.Data;

@Data
public class GemPool {
    private volatile int golds;
    private volatile int diamonds;
    private volatile int blueGems;
    private volatile int greenGems;
    private volatile int redGems;
    private volatile int blackGems;

    public GemPool(int playerNums) {
        this.golds = 5;
        switch (playerNums) {
            case 2:
                this.diamonds = this.blueGems = this.greenGems = this.redGems = this.blackGems = 4;
                break;
            case 3:
                this.diamonds = this.blueGems = this.greenGems = this.redGems = this.blackGems = 5;
                break;
            case 4:
                this.diamonds = this.blueGems = this.greenGems = this.redGems = this.blackGems = 7;
                break;
            default:
                throw new RuntimeException("invalid numbers of players");
        }
    }

    public void reduce(int diamond, int blueGem, int greenGem, int redGem, int blackGem) {
        if (!reduceRule(diamond, blueGem, greenGem, redGem, blackGem)) {
            throw new RuntimeException("不符合拿宝石规则");
        }
        int remainderDiamonds = diamonds - diamond;
        int remainderBlueGems = blueGems - blueGem;
        int remainderGreenGems = greenGems - greenGem;
        int remainderRedGems = redGems - redGem;
        int remainderBlackGems = blackGems - blackGem;

        if (remainderDiamonds < 0 || remainderBlueGems < 0 || remainderGreenGems < 0 || remainderRedGems < 0 || remainderBlackGems < 0) {
            throw new RuntimeException("宝石剩余数量不够本次操作");
        }

        this.diamonds = remainderDiamonds;
        this.blueGems = remainderBlueGems;
        this.greenGems = remainderGreenGems;
        this.redGems = remainderRedGems;
        this.blackGems = remainderBlackGems;
    }

    // todo 可优化
    private boolean reduceRule(int diamond, int blue, int green, int red, int black) {
        int sum = diamond + blue + green + red + black;
        if (sum > 3 || sum < 0) {
            return false;
        } else if (sum == 3) {
            return (2 & diamond & blue & green & red & black) == 0;
        }
        return true;
    }

    public void add(int gold, int diamond, int blue, int green, int red, int black) {
        this.golds += gold;
        this.diamonds += diamond;
        this.blackGems += blue;
        this.greenGems += green;
        this.redGems += red;
        this.blackGems += black;
    }

    public void reduceGold(){
        this.golds -= 1;
    }

}
