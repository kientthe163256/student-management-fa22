package com.example.studentmanagementfa22.utility;

import com.example.studentmanagementfa22.dto.MarkDTO;
import com.example.studentmanagementfa22.entity.Mark;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarkMapper extends  IGenericMapper<Mark, MarkDTO> {
}
