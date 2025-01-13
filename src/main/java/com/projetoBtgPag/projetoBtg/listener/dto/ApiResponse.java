package com.projetoBtgPag.projetoBtg.listener.dto;

import java.util.List;
import java.util.Map;

// quando declaramos como tipo T é o mesmo que any no typescript
public record ApiResponse<T>(Map<String, Object> summary,
                             List<T> data,
                             PaginationResponse pagination) {
}
