package com.git.commandlineinterfaceapp.commands;

import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.git.commandlineinterfaceapp.client.PullRequestActionsClient;
import com.git.commandlineinterfaceapp.task.AccessTokenGenerator;
import com.git.commandlineinterfaceapp.model.CreatePullRequestBodyParams;
import com.git.commandlineinterfaceapp.model.MergePullRequestBodyParams;
import com.git.commandlineinterfaceapp.model.MergePullRequestResponse;
import com.git.commandlineinterfaceapp.model.PullRequestResponse;
import com.git.commandlineinterfaceapp.util.ApplicationConstants;

import org.jline.reader.LineReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import javax.security.sasl.AuthenticationException;

/**
 * Execute shell commands to perform git operations
 * specific to Pull Request.
 */
@ShellComponent
public class PullRequestCommands {

    @Autowired
    @Lazy
    private LineReader lineReader;

    @Autowired
    private AccessTokenGenerator accessTokenGenerator;

    private PullRequestActionsClient pullRequestActionsClient = null;

    /**
     * Build REST API client to perform pull request operations.
     */
    public void buildPullRequestActionsClient() throws AuthenticationException {
        // Generate access token only when it is not generated previously
        if (accessTokenGenerator.accessToken.isEmpty()) {
            accessTokenGenerator.generateAccessToken();
        }
        WebClient webClient = WebClient.builder()
                .baseUrl(ApplicationConstants.gitReposEndPointBaseURL)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("Accept", ApplicationConstants.gitHubAcceptHeaderFormat);
                    httpHeaders.set("Authorization", "Bearer " + accessTokenGenerator.accessToken);
                    httpHeaders.set(ApplicationConstants.gitHubApiVersionHeaderName,
                            ApplicationConstants.gitHubApiVersion);
                }).build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(
                WebClientAdapter.create(webClient)).build();
        this.pullRequestActionsClient = factory.createClient(PullRequestActionsClient.class);
    }

    /**
     * Exposes command to creates pull request, collecting necessary
     * details from user and makes GitHub API call to create pull request.
     * @return String - response
     * @throws JsonProcessingException - when unable to convert class to
     *      json string.
     * @throws AuthenticationException - When authentication fails.
     */
    @ShellMethod(key = "create-pr", value="Create new pull request")
    public String createPullRequestCommand() throws JsonProcessingException, AuthenticationException {
        // Build client to perform pull request operations, if not built already
        if (pullRequestActionsClient == null) {
            buildPullRequestActionsClient();
        }
        Boolean getUserInput = true;
        String response = "";
        while (getUserInput) {
            // Getting necessary details from user to create pull request from command line.
            System.out.println("Enter the details necessary to create a pull request(PR)."
                    + "Enter blank value for non-applicable fields");
            String repoName = this.lineReader.readLine("Enter name of the repo(mandatory - without .git extension): ");
            String owner = this.lineReader.readLine("Enter owner of the repo(mandatory): ");
            String title = this.lineReader.readLine("Enter title of the PR(mandatory): ");
            String headBranch = this.lineReader.readLine("Enter name of source branch whose changes need to be merged"
                    + " as part of PR(mandatory): ");
            String baseBranch = this.lineReader.readLine("Enter name of destination branch where changes need to be"
                    + " merged as part of PR(mandatory): ");
            String headRepo = this.lineReader.readLine("Enter name of repo where changes are made and need to be merged"
                    + " to cross-repository: ");
            String body = this.lineReader.readLine("Enter detailed comments of PR: ");
            String canMaintainerModify = this.lineReader.readLine("Can maintainer modify - Enter Y/N: ");
            String draft = this.lineReader.readLine("Is this a draft PR - Enter Y/N: ");
            if (owner.isBlank() || repoName.isBlank() || headBranch.isEmpty() || baseBranch.isBlank()) {
                System.out.println("Not all mandatory fields are entered. Try again");
            } else {
                // Setting default value of boolean values to API call as false
                Boolean canMaintainerModifyPR = false;
                if (Objects.equals(canMaintainerModify, "Y")) {
                    canMaintainerModifyPR = true;
                }
                Boolean isDraftPR = false;
                if (Objects.equals(draft, "Y")) {
                    isDraftPR = true;
                }
                // Constructing the body json to be passed to API
                CreatePullRequestBodyParams createPullRequestBodyParams = new CreatePullRequestBodyParams(
                        title, headBranch, headRepo, baseBranch, body, canMaintainerModifyPR,
                        isDraftPR);
                String bodyJSON = new ObjectMapper().writeValueAsString(createPullRequestBodyParams);
                System.out.println("New Pull Request created with parameters " + bodyJSON);
                try {
                    // Performing API call to create pull request
                    PullRequestResponse pullRequest = pullRequestActionsClient.createPullRequest(
                            owner, repoName, bodyJSON);
                    // Any failure would be thrown as an exception. On successful response, this
                    // line gets executed.
                    response = "Pull request created : PR number - " + pullRequest.number() + " with "
                            + pullRequest.additions() + " files added "
                            + "and " + pullRequest.changed_files() + " files changed, with "
                            + pullRequest.commits() + " commits.\nPR Link: " + pullRequest.html_url();
                }
                catch (Exception exception) {
                    // API failure is thrown as exception
                    response = "Error occurred: " + exception.getMessage()
                        + ". Resolve issue and Retry with valid parameters";
                }
                getUserInput = false;
            }
        }
        return response;
    }

    /**
     * Exposes command to merge a pull request, collecting necessary
     * details from user and makes GitHub API call to merge pull request.
     * @return String - response
     * @throws JsonProcessingException - when unable to convert class to
     *      json string.
     * @throws AuthenticationException - When authentication fails.
     */
    @ShellMethod(key = "merge-pr", value="Merge pull request")
    public String mergePullRequestCommand() throws JsonProcessingException, AuthenticationException {
        // Build client to perform pull request operations, if not built already
        if (pullRequestActionsClient == null) {
            buildPullRequestActionsClient();
        }
        Boolean getUserInput = true;
        String response = "";
        while (getUserInput) {
            // Getting necessary details from user to create pull request from command line.
            System.out.println("Enter the details necessary to merge a pull request(PR)."
                    + " Enter blank value for non-applicable fields");
            String repoName = this.lineReader.readLine("Enter name of the repo(mandatory - without .git extension): ");
            String owner = this.lineReader.readLine("Enter owner of the repo(mandatory): ");
            String pullNumber = this.lineReader.readLine("Enter pull number to be merged(mandatory): ");
            String title = this.lineReader.readLine("Enter title of the commit: ");
            String message = this.lineReader.readLine("Enter message of the commit: ");
            String mergeMethod = this.lineReader.readLine("Enter merge method(can be one of merge/squash/rebase - default merge): ");
            if (owner.isBlank() || repoName.isBlank() || pullNumber.isEmpty()) {
                System.out.println("Not all mandatory fields are entered. Try again");
            } else if (mergeMethod.isBlank() || mergeMethod.contains("merge") || mergeMethod.contains("squash")
                || mergeMethod.contains("rebase")
            ) {
                if (mergeMethod.isBlank()) {
                    mergeMethod = "merge";
                }
                Integer pullNumberValue = Integer.parseInt(pullNumber);
                MergePullRequestBodyParams mergePullRequestBodyParams = new MergePullRequestBodyParams(
                        title, message, mergeMethod);
                String bodyJSON = new ObjectMapper().writeValueAsString(mergePullRequestBodyParams);
                System.out.println("Merge Pull Request initiated with parameters " + bodyJSON);
                try {
                    // Performing API call to merge pull request
                    MergePullRequestResponse mergePullRequestResponse = pullRequestActionsClient.mergePullRequest(
                            owner, repoName, pullNumberValue, bodyJSON);
                    // Any failure would be thrown as an exception. On successful response, this
                    // line gets executed.
                    response = "Merge status:  " + mergePullRequestResponse.merged() + ". Message: "
                            + mergePullRequestResponse.message();
                }
                catch (Exception exception) {
                    // API failure is thrown as exception
                    response = "Error occurred: " + exception.getMessage()
                            + ". Retry with valid parameters";
                }
                getUserInput = false;
            } else {
                System.out.println("Enter a valid value for merge method. Try again");
            }
        }
        return response;
    }
}
