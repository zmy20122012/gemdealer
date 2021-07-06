package com.zmy.gemdealer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Requirement {
    private RequirementType requirementType;
    private int redGems;
    private int greenGems;
    private int blueGems;
    private int blackGems;
    private int diamonds;

    public Requirement(int redGems, int greenGems, int blueGems, int blackGems, int diamonds){
        this.redGems = redGems;
        this.greenGems = greenGems;
        this.blueGems = blueGems;
        this.blackGems = blackGems;
        this.diamonds = diamonds;
        this.requirementType = RequirementType.ANY;
    }

    public static enum RequirementType{
        ONLY_ASSET, ANY
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return Requirement.builder()
                    .requirementType(this.requirementType)
                    .redGems(this.redGems)
                    .greenGems(this.greenGems)
                    .blueGems(this.blueGems)
                    .blackGems(this.blackGems)
                    .diamonds(this.diamonds)
                    .build();
        }
    }
}
