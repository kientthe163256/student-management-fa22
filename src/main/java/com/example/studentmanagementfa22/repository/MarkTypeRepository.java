package com.example.studentmanagementfa22.repository;

import com.example.studentmanagementfa22.entity.Mark;
import com.example.studentmanagementfa22.entity.MarkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarkTypeRepository extends JpaRepository<MarkType, Integer> {
    Optional<MarkType> findById(int markTypeId);
}
