package com.samples.ajedrez.plan;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer>{

	Optional<Plan> findPlanByType(PlanType type);
}
