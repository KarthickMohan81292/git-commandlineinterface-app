package com.git.commandlineinterfaceapp.config;

import com.git.commandlineinterfaceapp.client.AccessTokenGenerationClient;
import com.git.commandlineinterfaceapp.client.UserAuthorizationInitiationClient;
import com.git.commandlineinterfaceapp.util.ApplicationConstants;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * Builds Rest API Client objects which are possible
 * to be initialised during startup of application.
 */
@Configuration
public class RestAPIClientBuilder {

    /**
     * Builds web client for API to generate device code
     * for user.
     * @return UserAuthorizationInitiationClient
     */
    @Bean
    UserAuthorizationInitiationClient userDeviceAuthorizationClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl(ApplicationConstants.gitDeviceCodeBaseURL)
                .defaultHeader("Accept",
                        ApplicationConstants.jsonAcceptHeaderFormat)
                .build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(
                WebClientAdapter.create(webClient)).build();
        return factory.createClient(UserAuthorizationInitiationClient.class);
    }

    /**
     * Builds web client for API to generate access token
     * @return AccessTokenGenerationClient
     */
    @Bean
    AccessTokenGenerationClient accessTokenGenerationClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl(ApplicationConstants.gitGenerateAccessTokenBaseURL)
                .defaultHeader("Accept",
                        ApplicationConstants.jsonAcceptHeaderFormat)
                .build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(
                WebClientAdapter.create(webClient)).build();
        return factory.createClient(AccessTokenGenerationClient.class);
    }

}
