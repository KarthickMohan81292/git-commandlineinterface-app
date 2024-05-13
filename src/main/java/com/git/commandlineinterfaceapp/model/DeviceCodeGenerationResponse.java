package com.git.commandlineinterfaceapp.model;

/**
 * Entity to maintain Device Code Generation API Response.
 * @param device_code
 * @param user_code
 * @param verification_uri
 * @param expires_in
 * @param interval
 */
public record DeviceCodeGenerationResponse(String device_code, String user_code, String verification_uri,
                                           Integer expires_in, Integer interval) {
}
