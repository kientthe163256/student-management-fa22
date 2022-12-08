package com.example.studentmanagementfa22.utility.mapper;

import com.example.studentmanagementfa22.dto.SubjectDTO;
import com.example.studentmanagementfa22.entity.Subject;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubjectMapper extends IGenericMapper<Subject, SubjectDTO>{
}
