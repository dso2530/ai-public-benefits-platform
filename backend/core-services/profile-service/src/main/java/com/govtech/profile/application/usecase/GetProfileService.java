package com.govtech.profile.application.usecase;

import com.govtech.profile.api.dto.ProfileContractDto;
import com.govtech.profile.api.dto.ProfileDto;
import com.govtech.profile.domain.exception.CitizenNotFoundException;
import com.govtech.profile.domain.repository.CitizenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProfileService implements GetProfileUseCase {

  private final CitizenRepository citizenRepository;

  @Override
  public ProfileDto getProfile(String subject) {
    var citizen = citizenRepository.findBySubject(subject).orElseThrow(() -> new CitizenNotFoundException(subject));
    return ProfileDto.from(citizen);
  }

  @Override
  public ProfileContractDto getProfileContract(String subject) {
    var citizen = citizenRepository.findBySubject(subject).orElseThrow(() -> new CitizenNotFoundException(subject));
    return ProfileContractDto.from(citizen);

  }
}
