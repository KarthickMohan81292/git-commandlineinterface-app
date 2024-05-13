package com.git.commandlineinterfaceapp.model;

/**
 * Entity to maintain Pull Request Response from Git.
 * @param id
 * @param url
 * @param html_url
 * @param commits
 * @param changed_files
 * @param additions
 * @param number
 */
public record PullRequestResponse(Integer id, String url, String html_url, Integer commits,
                                  Integer changed_files, Integer additions, Integer number) {
}
