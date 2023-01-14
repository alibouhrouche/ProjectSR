package com.app.services;

import com.app.entities.Sport;
import com.app.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SportService {
    private final SportRepository sportRepository;

    public Sport save(Sport sport) {
        return sportRepository.save(sport);
    }
}
