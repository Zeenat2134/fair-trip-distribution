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


            // COMBO 1: 0-15 km | NORMAL
            String zone1="0-15 km";
            String category1="NORMAL";

            vendorZoneConfigRepository.save(new VendorZoneConfig(null,v1,zone1,category1,50.0));
            vendorZoneConfigRepository.save(new VendorZoneConfig(null,v2,zone1,category1,30.0));
            vendorZoneConfigRepository.save(new VendorZoneConfig(null,v3,zone1,category1,20.0));

            vendorLedgerRepository.save(new VendorLedger(null,v1,zone1,category1,BigDecimal.ZERO,0));
            vendorLedgerRepository.save(new VendorLedger(null,v2,zone1,category1,BigDecimal.ZERO,0));
            vendorLedgerRepository.save(new VendorLedger(null,v3,zone1,category1,BigDecimal.ZERO,0));

            for(int i=1;i<=2;i++){
                Trip trip=new Trip(null,zone1,category1,"PENDING",null);
                tripRepository.save(trip);
            }


            // COMBO 2: 15-25 km | NORMAL
            String zone2 = "15-25 km";
            String category2 = "NORMAL";

            vendorZoneConfigRepository.save(new VendorZoneConfig(null, v1, zone2, category2, 40.0));
            vendorZoneConfigRepository.save(new VendorZoneConfig(null, v2, zone2, category2, 40.0));
            vendorZoneConfigRepository.save(new VendorZoneConfig(null, v3, zone2, category2, 20.0));

            vendorLedgerRepository.save(new VendorLedger(null, v1, zone2, category2, BigDecimal.ZERO, 0));
            vendorLedgerRepository.save(new VendorLedger(null, v2, zone2, category2, BigDecimal.ZERO, 0));
            vendorLedgerRepository.save(new VendorLedger(null, v3, zone2, category2, BigDecimal.ZERO, 0));

            for(int i = 1; i <= 2; i++) {
                Trip trip = new Trip(null, zone2, category2, "PENDING", null);
                tripRepository.save(trip);
            }


            //COMBO 3: 0-15 km | ESCORT
            String zone3 = "0-15 km";
            String category3 = "ESCORT";

            vendorZoneConfigRepository.save(new VendorZoneConfig(null, v1, zone3, category3, 70.0));
            vendorZoneConfigRepository.save(new VendorZoneConfig(null, v2, zone3, category3, 30.0));
            vendorZoneConfigRepository.save(new VendorZoneConfig(null, v3, zone3, category3, 0.0));

            vendorLedgerRepository.save(new VendorLedger(null, v1, zone3, category3, BigDecimal.ZERO, 0));
            vendorLedgerRepository.save(new VendorLedger(null, v2, zone3, category3, BigDecimal.ZERO, 0));
            vendorLedgerRepository.save(new VendorLedger(null, v3, zone3, category3, BigDecimal.ZERO, 0));

            for(int i = 1; i <= 2; i++) {
                Trip trip = new Trip(null, zone3, category3, "PENDING", null);
                tripRepository.save(trip);
            }


            // COMBO 4: 15-25 km | ESCORT
            String zone4 = "15-25 km";
            String category4 = "ESCORT";

            vendorZoneConfigRepository.save(new VendorZoneConfig(null, v1, zone4, category4, 0.0));
            vendorZoneConfigRepository.save(new VendorZoneConfig(null, v2, zone4, category4, 50.0));
            vendorZoneConfigRepository.save(new VendorZoneConfig(null, v3, zone4, category4, 50.0));

            vendorLedgerRepository.save(new VendorLedger(null, v1, zone4, category4, BigDecimal.ZERO, 0));
            vendorLedgerRepository.save(new VendorLedger(null, v2, zone4, category4, BigDecimal.ZERO, 0));
            vendorLedgerRepository.save(new VendorLedger(null, v3, zone4, category4, BigDecimal.ZERO, 0));

            for(int i = 1; i <= 2; i++) {
                Trip trip = new Trip(null, zone4, category4, "PENDING", null);
                tripRepository.save(trip);
            }
            System.out.println("DATABASE seeded with 3 Vendors. Contracts, Ledgers and 10 Trips");
        }
    }
}
