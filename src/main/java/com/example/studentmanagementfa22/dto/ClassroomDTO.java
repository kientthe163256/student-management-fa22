package com.example.studentmanagementfa22.dto;

import com.example.studentmanagementfa22.entity.ClassType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassroomDTO {
    private Integer id;

    private String classroomName;

    private Integer currentNoStudent;

    private Integer noStudent;

    @Enumerated(value = EnumType.STRING)
    private ClassType classType;

    private String teacherName;

    private Integer teacherId;

    private String subjectName;

    private Integer subjectId;
}
