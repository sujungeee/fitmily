package com.d208.fitmily.domain.family.controller;

import com.d208.fitmily.domain.family.dto.CreateFamilyRequest;
import com.d208.fitmily.domain.family.dto.CreateFamilyResponse;
import com.d208.fitmily.domain.family.service.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/family")
@RequiredArgsConstructor
public class FamilyController {

    private final FamilyService familyService;

    @PostMapping
    public ResponseEntity<CreateFamilyResponse> createFamily(@RequestBody CreateFamilyRequest request) {
        int familyId = familyService.createFamily(request.getFamilyName());
        return ResponseEntity.ok(new CreateFamilyResponse(familyId));
    }
}
