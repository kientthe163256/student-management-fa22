package com.example.studentmanagementfa22.utility;

public interface IGenericMapper<S, DTO>{
    DTO toDTO(S source);
    S toEntity(DTO dto);
}
