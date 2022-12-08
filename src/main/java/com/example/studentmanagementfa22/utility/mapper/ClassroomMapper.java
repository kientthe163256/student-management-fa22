package com.example.studentmanagementfa22.utility.mapper;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.Classroom;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ClassroomMapper extends IGenericMapper<Classroom, ClassroomDTO> {
}
