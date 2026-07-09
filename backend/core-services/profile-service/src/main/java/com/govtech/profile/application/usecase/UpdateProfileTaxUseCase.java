package com.govtech.profile.application.usecase;

import com.govtech.profile.application.dto.UpdateProfileCommand;

public interface UpdateProfileTaxUseCase {

    void update(String subject, UpdateProfileCommand command);

}