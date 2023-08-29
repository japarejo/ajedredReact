package com.samples.ajedrez.plan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.isagroup.PricingService;
import io.github.isagroup.models.FeatureType;

@RequestMapping("/api/features")
@RestController
public class FeatureController {

    private final PricingService pricingService;

    @Autowired
    public FeatureController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @DeleteMapping("/{feature}")
    public void deleteFeature(@PathVariable String feature) {

        pricingService.removeFeatureFromConfiguration(feature);

    }

    @PutMapping("/{feature}")
    public void updateFeatureExpression(@PathVariable String feature, @RequestBody String expression) {

        pricingService.setFeatureExpression(feature, expression);
    }

    @PutMapping("/{feature}/type")
    public void updateFeatureType(@PathVariable String feature, @RequestBody String type) {

        pricingService.setFeatureType(feature, FeatureType.valueOf(type));
    }

}
