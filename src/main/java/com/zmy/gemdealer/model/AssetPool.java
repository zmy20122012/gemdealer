package com.zmy.gemdealer.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AssetPool {
    private List<List<Asset>> assets;

    public AssetPool(List<Asset> lowAsset, List<Asset> mediumAsset, List<Asset> highAsset) {
        shuffle(lowAsset);
        shuffle(mediumAsset);
        shuffle(highAsset);
        this.assets = new ArrayList<>(3);
        this.assets.add(lowAsset);
        this.assets.add(mediumAsset);
        this.assets.add(highAsset);
    }

    public <T> void shuffle(List<T> data) {
        for (int i = 0; i < data.size(); i++) {
            int swapIdx = (int) (Math.random() * data.size());
            T tmp = data.get(i);
            data.set(i, data.get(swapIdx));
            data.set(swapIdx, tmp);
        }
    }

    public Asset book(int level, int assetIdx) {
        List<Asset> subAsset = this.assets.get(level);
        if (subAsset.size() <= assetIdx) {
            throw new RuntimeException("in AssetPool book method: can not book the Asset level[" + level + "] index[" + assetIdx + "]. size:" + subAsset.size());
        }
        return subAsset.remove(assetIdx);
    }

    public int[] exchange(Player currentPlayer, int level, int assetIdx) {
        Asset asset = getAsset(level, assetIdx);
        Requirement requirement = asset.getRequirement();
        int[] exchangeRequirement = currentPlayer.exchange(requirement);
        currentPlayer.addAsset(asset);
        return exchangeRequirement;
    }

    private Asset getAsset(int level, int assetIdx) {
        return this.assets.get(level).get(assetIdx);
    }
}
