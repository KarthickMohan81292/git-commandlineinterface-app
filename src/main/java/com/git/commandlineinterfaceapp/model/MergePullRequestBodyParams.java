package com.git.commandlineinterfaceapp.model;

/**
 * Entity to maintain Merge PullRequest Body params.
 * @param commit_title
 * @param commit_message
 * @param merge_method
 */
public record MergePullRequestBodyParams(String commit_title, String commit_message,
                                         String merge_method) {
}
