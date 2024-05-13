package com.git.commandlineinterfaceapp.model;

/**
 * Entity to maintain Merge Pull Request API Response.
 * @param sha
 * @param merged
 * @param message
 */
public record MergePullRequestResponse(String sha, Boolean merged, String message) {
}
