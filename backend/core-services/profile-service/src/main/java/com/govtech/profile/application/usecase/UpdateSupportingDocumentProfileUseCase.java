package com.govtech.profile.application.usecase;

import com.govtech.events.common.DocumentBaseEvent;
import com.govtech.profile.application.dto.UpdateProfileCommand;

public interface UpdateSupportingDocumentProfileUseCase {

    void updateSupportingDocumentProfile(
            String subject,
            UpdateProfileCommand command,
            DocumentBaseEvent metadata);
}