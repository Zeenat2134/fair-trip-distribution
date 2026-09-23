package com.fairAllocation.cabAllocation.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class VendorReportDTO {

    private String vendorName;
    private String zoneType;
    private String tripCategory;
    private Double promisedPercentage;
    private Double actualPercentage;
    private BigDecimal currentShortfall;

}
