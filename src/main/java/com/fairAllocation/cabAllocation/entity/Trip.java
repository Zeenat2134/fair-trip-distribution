package com.fairAllocation.cabAllocation.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String zoneType; // to determine short dist or long dist zone
    private String tripCategory;
    private  String status ; // pending , allocated  or rejected

    @ManyToOne
    @JoinColumn(name="assigned_vendor_id")
    private Vendor assignedVendor;
}
