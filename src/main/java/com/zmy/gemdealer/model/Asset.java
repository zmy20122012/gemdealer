package com.zmy.gemdealer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Asset {
    private int goal;
    private GemType value;
    private Requirement requirement;
    private String backgroundURL;


    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return Asset.builder()
                    .goal(this.goal)
                    .value(this.value)
                    .requirement((Requirement) this.requirement.clone())
                    .backgroundURL(this.backgroundURL)
                    .build();
        }
    }
}
