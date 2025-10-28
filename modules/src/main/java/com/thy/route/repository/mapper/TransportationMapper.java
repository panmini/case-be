package com.thy.route.repository.mapper;

import com.thy.repository.mapper.BaseMapper;
import com.thy.route.data.dto.TransportationDTO;
import com.thy.route.data.entity.Transportation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransportationMapper extends BaseMapper<Transportation, TransportationDTO> {
}
