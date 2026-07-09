package com.govtech.profile.application.usecase;

import com.govtech.profile.application.dto.UpdateProfileCommand;

public interface UpdateProfileTaxUseCase {

    void updateProfileTaxUseCase(String subject, UpdateProfileCommand command);

}