package com.example.studentmanagementfa22.entity;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;

public class TeacherSpecification {
    public static Specification<Teacher> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> {
            Join<Account, Teacher> teacherAccount = root.join("account");
            return criteriaBuilder.equal(teacherAccount.get("firstName"), firstName);
        };
    }
}
