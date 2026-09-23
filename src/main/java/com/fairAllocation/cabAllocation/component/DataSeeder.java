package com.fairAllocation.cabAllocation.component;

import com.fairAllocation.cabAllocation.entity.Trip;
import com.fairAllocation.cabAllocation.entity.Vendor;
import com.fairAllocation.cabAllocation.entity.VendorLedger;
import com.fairAllocation.cabAllocation.entity.VendorZoneConfig;
import com.fairAllocation.cabAllocation.repository.TripRepository;
import com.fairAllocation.cabAllocation.repository.VendorLedgerRepository;
import com.fairAllocation.cabAllocation.repository.VendorRepository;
import com.fairAllocation.cabAllocation.repository.VendorZoneConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final VendorRepository vendorRepository;
    private final VendorZoneConfigRepository vendorZoneConfigRepository;
    private final VendorLedgerRepository vendorLedgerRepository;
    private final TripRepository tripRepository;

    @Override
    public void run(String... args)throws Exception {

        if(vendorRepository.count()==0){

            Vendor v1 = new Vendor(null,"Vendor 1 (V1)",10,10,null);
            Vendor v2= new Vendor(null,"Vendor 2 (V2)",10,10,null);
            Vendor v3= new Vendor(null,"Vendor 3 (V3)",10,10,null);

            vendorRepository.save(v1);
            vendorRepository.save(v2);
            vendorRepository.save(v3);

            String zone="0-15 km";
            String category="NORMAL";

            vendorZoneConfigRepository.save(new VendorZoneConfig(null,v1,zone,category,50.0));
            vendorZoneConfigRepository.save(new VendorZoneConfig(null,v2,zone,category,30.0));
            vendorZoneConfigRepository.save(new VendorZoneConfig(null,v3,zone,category,20.0));

            vendorLedgerRepository.save(new VendorLedger(null,v1,zone,category,BigDecimal.ZERO,0));
            vendorLedgerRepository.save(new VendorLedger(null,v2,zone,category,BigDecimal.ZERO,0));
            vendorLedgerRepository.save(new VendorLedger(null,v3,zone,category,BigDecimal.ZERO,0));

            for(int i=1;i<=10;i++){
                Trip trip=new Trip(null,zone,category,"PENDING",null);
                tripRepository.save(trip);
            }

            System.out.println("DATABASE seeded with 3 Vendors. Contracts, Ledgers and 10 Trips");
        }
    }
}
