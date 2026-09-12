package com.govtech.profile.application.usecase;

import com.govtech.platform.messaging.event.EventContext;
import com.govtech.profile.application.dto.UpdateProfileCommand;

public interface UpdateProfileTaxUseCase {

    void updateProfileTax(String subject, UpdateProfileCommand command, EventContext eventContext);

}