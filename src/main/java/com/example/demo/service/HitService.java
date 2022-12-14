package com.example.demo.service;

import com.example.demo.dto.HitDto;
import com.example.demo.dto.request.HitRequest;
import com.example.demo.entity.Hit;
import com.example.demo.entity.User;
import com.example.demo.mapper.HitMapper;
import com.example.demo.repository.HitRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.exception.HitNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HitService {
    private final HitRepository hitRepository;
    private final UserRepository userRepository;
    private final HitChecker hitChecker;

    public List<HitDto> getAllByUserId(Long userId){
        List<Hit> allByUserId = hitRepository.findAllByUserId(userId);
        return allByUserId.stream().map(HitMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    public HitDto findById(Long hitId) {
        Optional<Hit> hitDto = hitRepository.findById(hitId);
        if (hitDto.isPresent()){
            Hit hit = hitDto.get();
            return HitMapper.INSTANCE.toDto(hit);
        }
        throw new HitNotFoundException("Точка не найдена");
    }

    public HitDto save(HitRequest hitRequest, long id, long executionTime, LocalDateTime time) {
        Optional<User> byId = userRepository.findById(id);
        boolean result = hitChecker.check(hitRequest.getX(),
                hitRequest.getY(),
                hitRequest.getR());
        HitDto hitDto = new HitDto(hitRequest.getX(),
                hitRequest.getY(),
                hitRequest.getR(),
                result,
                executionTime,
                time);
        Hit hit = HitMapper.INSTANCE.toEntity(hitDto);
        hit.setUser(byId.get());
        hit = hitRepository.save(hit);
        return HitMapper.INSTANCE.toDto(hit);
    }
}
