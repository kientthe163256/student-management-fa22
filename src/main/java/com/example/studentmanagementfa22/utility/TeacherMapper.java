package com.example.studentmanagementfa22.utility;

import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Teacher;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public abstract class TeacherMapper implements IGenericMapper<Teacher, TeacherDTO> {

    @Override
    public abstract TeacherDTO mapToDTO(Teacher source);

}
