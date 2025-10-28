package com.thy.route.repository.mapper;

import com.thy.repository.mapper.BaseMapper;
import com.thy.route.data.dto.LocationDTO;
import com.thy.route.data.entity.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper extends BaseMapper<Location, LocationDTO> {
}
