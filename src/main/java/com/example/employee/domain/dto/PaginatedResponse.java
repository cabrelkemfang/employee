package com.example.employee.domain.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PaginatedResponse<T>(List<T> response,
                                   long totalElements,
                                   int totalPages,
                                   int currentPage,
                                   int pageSize) {
}
