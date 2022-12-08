package com.example.studentmanagementfa22.utility.mapper;

import com.example.studentmanagementfa22.dto.TeacherDTO;
import com.example.studentmanagementfa22.entity.Teacher;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface TeacherMapper extends IGenericMapper<Teacher, TeacherDTO> {

}
