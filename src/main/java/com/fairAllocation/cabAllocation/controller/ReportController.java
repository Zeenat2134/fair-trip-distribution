package com.fairAllocation.cabAllocation.controller;

import com.fairAllocation.cabAllocation.dto.VendorReportDTO;
import com.fairAllocation.cabAllocation.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/shares")
    public ResponseEntity<List<VendorReportDTO>> getShareReport(
            @RequestParam String zoneType,
            @RequestParam String tripCategory)
    {
        List<VendorReportDTO> report= reportService.generateShareReport(zoneType, tripCategory);
        return ResponseEntity.ok(report);
    }
}
