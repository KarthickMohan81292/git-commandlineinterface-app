package com.git.commandlineinterfaceapp.client;

import com.git.commandlineinterfaceapp.model.DeviceCodeGenerationResponse;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

/**
 * REST API Client to initiate user device authorization.
 */
public interface UserAuthorizationInitiationClient {

    /**
     * Makes REST API call to get device code of user
     * @param client_id
     * @return DeviceCodeGenerationResponse object
     */
    @PostExchange("/")
    DeviceCodeGenerationResponse getUserDeviceCode(
            @RequestParam String client_id);
}
