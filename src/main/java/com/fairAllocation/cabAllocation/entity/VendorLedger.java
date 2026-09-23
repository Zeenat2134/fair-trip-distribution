package com.fairAllocation.cabAllocation.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="vendor_id")
    private Vendor vendor;

    private String zoneType;
    private String tripCategory;

    @Column(precision=19, scale=4)
    private BigDecimal owedBalance;

    private Integer actualAllocatedCount;

}
