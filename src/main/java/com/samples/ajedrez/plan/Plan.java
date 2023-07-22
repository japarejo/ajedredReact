package com.samples.ajedrez.plan;

import com.samples.ajedrez.model.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="plans")
public class Plan extends BaseEntity {

    private double price;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PlanType type;

    private int maxGames;

    private boolean allowGameSpectators;

}
