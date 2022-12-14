package com.example.demo.mapper;

import com.example.demo.dto.HitDto;
import com.example.demo.entity.Hit;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class})
public interface HitMapper {
    HitMapper INSTANCE = Mappers.getMapper(HitMapper.class);

    Hit toEntity(HitDto hitDto);

    HitDto toDto(Hit hit);
}
