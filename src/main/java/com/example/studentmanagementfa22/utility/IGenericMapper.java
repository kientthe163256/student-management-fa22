package com.example.studentmanagementfa22.utility;


public interface IGenericMapper<S, DTO>{
    DTO mapToDTO(S source);
    S mapToEntity(DTO dto);
}
