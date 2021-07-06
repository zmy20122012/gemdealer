package com.zmy.gemdealer.config;

import com.zmy.gemdealer.model.Asset;
import com.zmy.gemdealer.model.Investor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@ConfigurationProperties(prefix = "default")
@PropertySource(value = "classpath:investors_assets.yml", factory = YamlPropertySourceFactory.class)
@Data
@SuperBuilder
@NoArgsConstructor
public class InvestorsAndAssets {
    private List<Investor> investors;
    private Map<String, List<Asset>> assets;

    public List<Asset> getLowAsset() {
        return assets.get("low");
    }

    public List<Asset> getMediumAsset() {
        return assets.get("medium");
    }

    public List<Asset> getHighAsset() {
        return assets.get("high");
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            List<Investor> investors = this.investors.stream().map(investor -> (Investor)investor.clone()).collect(Collectors.toList());
            Map<String, List<Asset>> assets = new HashMap<>(3);
            assets.put("low", getLowAsset().stream().map(asset -> (Asset)asset.clone()).collect(Collectors.toList()));
            assets.put("medium", getMediumAsset().stream().map(asset -> (Asset)asset.clone()).collect(Collectors.toList()));
            assets.put("high", getHighAsset().stream().map(asset -> (Asset)asset.clone()).collect(Collectors.toList()));
            return InvestorsAndAssets.builder()
                    .investors(investors)
                    .assets(assets)
                    .build();
        }
    }
}
