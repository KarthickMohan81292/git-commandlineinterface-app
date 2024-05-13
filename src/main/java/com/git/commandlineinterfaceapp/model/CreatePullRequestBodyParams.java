package com.git.commandlineinterfaceapp.model;

/**
 * Entity to maintain CreatePullRequest Body Params.
 * @param title
 * @param head
 * @param head_repo
 * @param base
 * @param body
 * @param maintainer_can_modify
 * @param draft
 */
public record CreatePullRequestBodyParams(String title, String head, String head_repo, String base,
                                          String body, Boolean maintainer_can_modify, Boolean draft) {
}
