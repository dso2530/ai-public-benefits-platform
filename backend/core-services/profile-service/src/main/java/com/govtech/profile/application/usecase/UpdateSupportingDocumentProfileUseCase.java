package com.govtech.profile.application.usecase;

import com.govtech.platform.messaging.event.EventContext;
import com.govtech.profile.application.dto.DocumentCommand;
import com.govtech.profile.application.dto.UpdateProfileCommand;

public interface UpdateSupportingDocumentProfileUseCase {

    void updateSupportingDocumentProfile(
            String subject,
            UpdateProfileCommand updateProfileCommand,
            DocumentCommand documentCommand,
            EventContext eventContext);
}