package com.govtech.citizen.application.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.govtech.citizen.application.dto.ApplicationDto;
import com.govtech.citizen.clients.ApplicationClient;

@Service
@RequiredArgsConstructor
public class ApplicationService {

  private final ApplicationClient client;

  public List<ApplicationDto> findAll() {
    return client.retrieveAlls();
  }

  public ApplicationDto findById(UUID id) {
    return client.findById(id);
  }

  public void submit(UUID id) {
    client.submit(id);
  }

  public byte[] downloadPackage(UUID id) {
    return client.download(id);
  }
}
