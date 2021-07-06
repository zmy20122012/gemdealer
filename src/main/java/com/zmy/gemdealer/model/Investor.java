package com.zmy.gemdealer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Investor implements Serializable {
    private int goal;
    private String name;
    private String gender;
    private Requirement requirement;

    public boolean canBeInvited(Player player) {
        if (requirement.getRequirementType() != Requirement.RequirementType.ONLY_ASSET) {
            throw new RuntimeException("RequirementType must be ONLY_ASSET, plz check your config");
        }
        return player.getDiamondAssets().size() >= requirement.getDiamonds()
                && player.getBlueAssets().size() >= requirement.getBlueGems()
                && player.getGreenAssets().size() >= requirement.getGreenGems()
                && player.getRedAssets().size() >= requirement.getRedGems()
                && player.getBlackAssets().size() >= requirement.getBlackGems();
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return Investor.builder()
                    .goal(this.goal)
                    .name(this.name)
                    .gender(this.gender)
                    .requirement((Requirement) this.requirement.clone())
                    .build();
        }
    }
}
