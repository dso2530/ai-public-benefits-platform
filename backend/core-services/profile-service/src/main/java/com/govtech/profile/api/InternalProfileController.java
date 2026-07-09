package com.govtech.profile.api;

import com.govtech.profile.api.dto.ProfileContractDto;
import com.govtech.profile.application.usecase.GetProfileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/profiles")
@RequiredArgsConstructor
public class InternalProfileController {

    private final GetProfileUseCase getProfileUseCase;

    @GetMapping("/{subject}")
    public ProfileContractDto getProfile(@PathVariable String subject) {
        return getProfileUseCase.getProfileContract(subject);
    }
}