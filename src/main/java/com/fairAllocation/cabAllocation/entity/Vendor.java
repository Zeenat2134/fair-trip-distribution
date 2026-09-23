package com.fairAllocation.cabAllocation.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vendor {

    @Id@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;

    private Integer totalCapacity;
    private Integer currentActiveCabs;
    private LocalDateTime coolOffUntil;
}
