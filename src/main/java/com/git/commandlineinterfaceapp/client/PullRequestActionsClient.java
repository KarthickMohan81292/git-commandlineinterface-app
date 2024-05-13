package com.git.commandlineinterfaceapp.client;

import com.git.commandlineinterfaceapp.model.MergePullRequestResponse;
import com.git.commandlineinterfaceapp.model.PullRequestResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

/**
 * REST API Client to perform git operations specific
 * to pull request, ex:- adding, merging, etc.
 */
public interface PullRequestActionsClient {

    /**
     * Makes REST API call to create a new pull request
     * @param owner - Owner of repo
     * @param repo - Name of repo
     * @param body - String containing multiple parameters to be
     *             passed to API in json format.
     * @return PullRequestResponse object
     */
    @PostExchange("/{owner}/{repo}/pulls")
    PullRequestResponse createPullRequest(@PathVariable(value = "owner") String owner,
            @PathVariable(value = "repo") String repo,
            @RequestBody String body);

    /**
     * Makes REST API call to merge a pull request
     * @param owner - Owner of repo
     * @param repo - Name of repo
     * @param pullNumber - Pull number
     * @param body - String containing multiple parameters to be
     *             passed to API in json format.
     * @return MergePullRequestResponse object
     */
    @PutExchange("/{owner}/{repo}/pulls/{pull_number}/merge")
    MergePullRequestResponse mergePullRequest(@PathVariable(value = "owner") String owner,
                                              @PathVariable(value = "repo") String repo,
                                              @PathVariable(value = "pull_number") Integer pullNumber,
                                              @RequestBody String body);
}
