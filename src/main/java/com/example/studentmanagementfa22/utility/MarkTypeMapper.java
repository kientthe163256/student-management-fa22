package com.example.studentmanagementfa22.utility;

import com.example.studentmanagementfa22.dto.MarkTypeDTO;
import com.example.studentmanagementfa22.entity.MarkType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarkTypeMapper extends IGenericMapper<MarkType, MarkTypeDTO> {
}
