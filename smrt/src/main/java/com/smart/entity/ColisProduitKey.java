package com.smart.entity;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColisProduitKey implements Serializable {
    private Long colisId;
    private Long produitId;
}
