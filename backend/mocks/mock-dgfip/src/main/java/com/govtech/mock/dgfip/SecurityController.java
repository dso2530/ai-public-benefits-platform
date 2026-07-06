package com.govtech.mock.dgfip;

import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
public class SecurityController {

  @PostMapping("/token")
  public Map<String, Object> token() {

    return Map.of(
        "access_token", "mock-access-token",
        "token_type", "Bearer",
        "expires_in", 3600);
  }
}
