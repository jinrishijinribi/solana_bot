package com.rich.sol_bot.admin.service;

import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class MFATotpService {
    private final DefaultSecretGenerator secretGenerator = new DefaultSecretGenerator();
    private final DefaultCodeVerifier verifier = new DefaultCodeVerifier(
            new DefaultCodeGenerator(HashingAlgorithm.SHA1),
            new SystemTimeProvider()
    );

    public String newSecret() {
        return secretGenerator.generate();
    }

    public boolean validateCode0(String secret, String code) {
        if (StringUtils.isBlank(code)) {
            return false;
        }
        return verifier.isValidCode(secret, code);
    }

}
