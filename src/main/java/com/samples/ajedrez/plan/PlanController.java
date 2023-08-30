package com.samples.ajedrez.plan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.isagroup.PricingService;
import io.github.isagroup.models.Plan;

@RequestMapping("/api/plans")
@RestController
public class PlanController {

    private final PricingService pricingService;

    @Autowired
    public PlanController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @GetMapping("/{plan}")
    public io.github.isagroup.models.Plan getPlanByName(@PathVariable String plan) {

        return pricingService.getPlanFromName(plan);
    }

    @PostMapping("/{plan}")
    public void addPlan(@PathVariable String planName, @RequestBody Plan plan) {

        pricingService.addPlanToConfiguration(planName, plan);
    }

    @DeleteMapping("/{plan}")
    public void deletePlan(@PathVariable String plan) {
        pricingService.removePlanFromConfiguration(plan);
    }

    @PutMapping("/{plan}/pricing")
    public void updatePlanPrice(@PathVariable String plan, @RequestBody Double price) {

        pricingService.setPlanPrice(plan, price);
    }

    @PostMapping("/{plan}/features/{feature}")
    public void updatePlanFeatureValue(@PathVariable String plan, String feature, @RequestBody Object value) {

        pricingService.setPlanFeatureValue(plan, feature, value);
    }

}
