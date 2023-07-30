package com.samples.ajedrez.plan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanService {

	private final PlanRepository planRepository;
	
	@Autowired
	public PlanService(PlanRepository planRepository) {
		this.planRepository = planRepository;
	}
	
	public boolean checkPlanExists(String plan) {
		return true;
	}
	
	public Plan getPlanByPlanType(String plan) {
		PlanType planType = PlanType.valueOf(PlanType.class, plan.toUpperCase());
		
		return this.planRepository.findPlanByType(planType).orElse(null);
	}
}
