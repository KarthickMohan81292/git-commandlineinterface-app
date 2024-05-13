package com.git.commandlineinterfaceapp.client;

import com.git.commandlineinterfaceapp.model.AccessTokenResponse;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

/**
 * REST API Client to generate access token for performing
 * Git operations via command line.
 */
public interface AccessTokenGenerationClient {
    /**
     * Makes a REST API call to generate user access token.
     *
     * @param client_id - Client Id
     * @param device_code - Code generated for device trying
     *                    to login
     * @param grant_type - Device grant type
     * @return AccessTokenResponse - Response of API call
     */
    @PostExchange("/")
    AccessTokenResponse generateAccessToken(
            @RequestParam String client_id,
            @RequestParam String device_code,
            @RequestParam String grant_type);
}
