package com.example.studentmanagementfa22.utility;

import com.example.studentmanagementfa22.dto.ClassroomDTO;
import com.example.studentmanagementfa22.entity.Classroom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ClassroomMapper implements IGenericMapper<Classroom, ClassroomDTO> {


    @Override
    public abstract ClassroomDTO mapToDTO(Classroom source);

}
