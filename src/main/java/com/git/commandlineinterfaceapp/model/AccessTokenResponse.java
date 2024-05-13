package com.git.commandlineinterfaceapp.model;

/**
 * Entity class to maintain Access Token API Response.
 * @param access_token
 * @param expires_in
 * @param refresh_token
 * @param refresh_token_expires_in
 * @param token_type
 * @param scope
 * @param error
 * @param error_description
 * @param error_uri
 */
public record AccessTokenResponse(String access_token, String expires_in, String refresh_token,
                                  String refresh_token_expires_in, String token_type, String scope,
                                  String error, String error_description, String error_uri) {
}
