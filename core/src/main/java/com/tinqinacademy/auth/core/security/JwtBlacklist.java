package com.tinqinacademy.auth.core.security;

import com.tinqinacademy.auth.persistence.repository.BlackListedTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtBlacklist {
    private final BlackListedTokenRepository blackListedTokenRepository;


    //@Scheduled every 5 minutes
    @Scheduled(cron = "0 */5 * * * *")
    public void cleanBlacklist() {
        log.info("Start cleanBlacklist() blacklisted tokens");
        //loop all blacklisted tokens and remove expired ones
        blackListedTokenRepository.findAll().forEach(blacklistedToken -> {
            if (blacklistedToken.getBlacklistedOn().plusMinutes(5).isBefore(LocalDateTime.now())) {
                blackListedTokenRepository.delete(blacklistedToken);
            }
        });
        log.info("End cleanBlacklist() blacklisted tokens");
    }
}
