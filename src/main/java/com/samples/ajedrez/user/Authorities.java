package com.samples.ajedrez.user;

import com.samples.ajedrez.model.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "authorities")
@NoArgsConstructor
public class Authorities extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "username")
    User user;

    @Size(min = 3, max = 50)
    String authority;

}
