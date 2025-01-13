package com.projetoBtgPag.projetoBtg.listener.dto;

import java.util.List;

// quando declaramos como tipo T é o mesmo que any no typescript
public record ApiResponse<T>(List<T> data, PaginationResponse pagination) {
}
