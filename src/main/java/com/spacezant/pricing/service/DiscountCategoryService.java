package com.spacezant.pricing.service;

import com.spacezant.pricing.dto.discounts.DiscountCategoryRequestDTO;
import com.spacezant.pricing.dto.discounts.DiscountCategoryResponseDTO;
import com.spacezant.pricing.entity.DiscountCategory;
import com.spacezant.pricing.repository.DiscountCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountCategoryService {

    private final DiscountCategoryRepository repo;

    // ✅ FIXED: Convert DTO → Entity before saving
    public DiscountCategoryResponseDTO create(DiscountCategoryRequestDTO request) {

        DiscountCategory entity = new DiscountCategory();
        entity.setCategoryName(request.getCategoryType());
        entity.setDescription(request.getDescription());
        entity.setActive(request.getActive());
        entity.setCategoryType(request.getCategoryType());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        DiscountCategory saved = repo.save(entity);

        // Convert Entity → Response DTO
        return mapToResponse(saved);
    }

    // ✅ GET ALL
    public List<DiscountCategoryResponseDTO> getAll() {

        return repo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ✅ COMMON MAPPER METHOD (clean & reusable)
    private DiscountCategoryResponseDTO mapToResponse(DiscountCategory c) {
        DiscountCategoryResponseDTO dto = new DiscountCategoryResponseDTO();
        dto.setCategoryId(c.getCategoryId());
        dto.setCategoryName(c.getCategoryName());
        dto.setDescription(c.getDescription());
        dto.setActive(c.getActive());
        dto.setCategoryName(c.getCategoryType());
        return dto;
    }
}