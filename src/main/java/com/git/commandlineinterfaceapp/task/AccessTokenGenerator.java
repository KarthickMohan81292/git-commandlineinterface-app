package com.git.commandlineinterfaceapp.task;

import java.util.Objects;

import com.git.commandlineinterfaceapp.client.AccessTokenGenerationClient;
import com.git.commandlineinterfaceapp.client.UserAuthorizationInitiationClient;
import com.git.commandlineinterfaceapp.model.AccessTokenResponse;
import com.git.commandlineinterfaceapp.model.DeviceCodeGenerationResponse;

import org.jline.reader.LineReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.security.sasl.AuthenticationException;

/**
 * Generates Access token to hit GitHub Rest APIs.
 */
@Configuration
public class AccessTokenGenerator {

    @Autowired
    private UserAuthorizationInitiationClient userAuthorizationInitiationClient;

    @Autowired
    private AccessTokenGenerationClient accessTokenGenerationClient;

    @Value("${commandlineinterfaceapp.clientid}")
    private String clientId;

    @Autowired
    @Lazy
    private LineReader lineReader;

    public String accessToken = "";

    /**
     * Generates Access token by sharing authorization code to user and
     * allowing user to authenticate into git account using the code.
     */
    public void generateAccessToken() throws AuthenticationException {
        DeviceCodeGenerationResponse deviceCodeGenerationResponse =
                userAuthorizationInitiationClient.getUserDeviceCode(clientId);
        System.out.println("Please login to your Github account in browser and navigate to URL: " +
                deviceCodeGenerationResponse.verification_uri() +
                " and enter code " + deviceCodeGenerationResponse.user_code() + " to authorize the app");
        String authorizationComplete = "";
        while (authorizationComplete.isEmpty()) {
            authorizationComplete = this.lineReader.readLine("After successful authorization, press Y. Press N to cancel: ");
            if (Objects.equals(authorizationComplete, "Y")) {
                AccessTokenResponse accessTokenResponse = accessTokenGenerationClient.generateAccessToken(
                        clientId, deviceCodeGenerationResponse.device_code(),
                        "urn:ietf:params:oauth:grant-type:device_code");
                if (accessTokenResponse.error() != null) {
                    if (Objects.equals(accessTokenResponse.error(), "authorization_pending")) {
                        System.out.println("Authorization not complete.\n");
                        authorizationComplete = "";
                    } else {
                        throw new AuthenticationException(
                                "Authorization failed: " + accessTokenResponse.error_description());
                    }
                } else if (accessTokenResponse.access_token() != null) {
                    this.accessToken = accessTokenResponse.access_token();
                }
            }
        }
    }
}
