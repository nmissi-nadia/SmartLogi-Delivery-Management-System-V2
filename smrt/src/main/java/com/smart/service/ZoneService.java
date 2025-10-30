package com.smart.service;

import com.smart.entity.Zone;
import com.smart.repository.ZoneRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ZoneService {
    private final ZoneRepository zoneRepository;

    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public List<Zone> getAllZones() { return zoneRepository.findAll(); }
    public Zone createZone(Zone zone) { return zoneRepository.save(zone); }

    public Zone getZoneById(Long id) {

    }
}
