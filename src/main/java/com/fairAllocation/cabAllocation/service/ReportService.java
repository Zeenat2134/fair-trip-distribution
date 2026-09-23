package com.fairAllocation.cabAllocation.service;

import com.fairAllocation.cabAllocation.dto.VendorReportDTO;
import com.fairAllocation.cabAllocation.entity.VendorLedger;
import com.fairAllocation.cabAllocation.entity.VendorZoneConfig;
import com.fairAllocation.cabAllocation.repository.VendorLedgerRepository;
import com.fairAllocation.cabAllocation.repository.VendorZoneConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final VendorLedgerRepository vendorLedgerRepository;
    private final VendorZoneConfigRepository vendorZoneConfigRepository;

    public List<VendorReportDTO> generateShareReport(String zoneType, String tripCategory){

        List<VendorLedger> ledgers = vendorLedgerRepository.findAll();
        List<VendorZoneConfig> configs = vendorZoneConfigRepository.findByZoneTypeAndTripCategory(zoneType, tripCategory);

        int totalTripsAllocated=ledgers.stream()
                .filter(l-> l.getZoneType().equals(zoneType) &&
                        l.getTripCategory().equals(tripCategory))
                .mapToInt(VendorLedger::getActualAllocatedCount).sum();

        List<VendorReportDTO> reports = new ArrayList<>();

        for(VendorZoneConfig config: configs){

            VendorLedger ledger= ledgers.stream()
                    .filter(l -> l.getVendor().getId().equals(config.getVendor().getId()) &&
                             l.getZoneType().equals(zoneType)
                            && l.getTripCategory().equals(tripCategory))
                    .findFirst().orElse(null);

            if(ledger!=null) {
                VendorReportDTO dto = new VendorReportDTO();
                dto.setVendorName(config.getVendor().getName());
                dto.setZoneType(zoneType);
                dto.setTripCategory(tripCategory);
                dto.setPromisedPercentage(config.getPromisedPercentage());

                double actualPct = 0.0;
                if (totalTripsAllocated > 0) {
                    actualPct = ((double) ledger.getActualAllocatedCount() / totalTripsAllocated) * 100;

                }

                dto.setActualPercentage(Math.round(actualPct * 100.0) / 100.0);

                dto.setCurrentShortfall(ledger.getOwedBalance());

                reports.add(dto);
            }
        }
        return reports;
    }
}
