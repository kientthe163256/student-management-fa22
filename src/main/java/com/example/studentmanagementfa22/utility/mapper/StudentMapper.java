package com.example.studentmanagementfa22.utility.mapper;

import com.example.studentmanagementfa22.dto.StudentDTO;
import com.example.studentmanagementfa22.entity.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper extends IGenericMapper<Student, StudentDTO>{
}
