package com.fairAllocation.cabAllocation.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorZoneConfig {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="vendor_id")
    private Vendor vendor;

    private String zoneType;
    private String tripCategory;
    private Double promisedPercentage;
}
