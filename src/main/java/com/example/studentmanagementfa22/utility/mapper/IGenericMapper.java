package com.example.studentmanagementfa22.utility.mapper;


public interface IGenericMapper<S, DTO>{
    DTO mapToDTO(S source);
    S mapToEntity(DTO dto);
}
