package com.govtech.document.application.usecase;

import org.springframework.stereotype.Service;

import com.govtech.document.application.command.RegisterDocumentCommand;
import com.govtech.document.application.service.DocumentRegistrationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateExternalDocumentUseCase {

        private final DocumentRegistrationService registrationService;

        public void execute(
                        RegisterDocumentCommand registerDocumentCommand) {

                registrationService.register(

                                registerDocumentCommand)

                ;

        }

}