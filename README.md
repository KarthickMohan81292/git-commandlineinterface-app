# git-commandlineinterface-app
CLI tool to peform Git operations interacting with Git APIs.

# Pre-requisites

GitHub Setup
- GitHub account to perform Git Operations
- Create a github App in that account following https://docs.github.com/en/apps/creating-github-apps/registering-a-github-app/registering-a-github-app providing URL of the Github repository (where we are planning to perform Git Operations) as home URL. Also enable device flow for the app.
- Provide permissions for the app necessary to perform the necessary git operations - accessing repos, creating or merging PRs, etc. Make sure user who is going to run this app in local environment has similar permissions to this app.
- Once App is created, note down the clientId created.
- Install the github app created
- Update permissions of Repository for github app to access it.

To Run Spring boot App
- Java17 SDK, JRE
- Any IDE with gradle support

# Steps to run
- Set up environment variable CLIENTID with value noted down while creating github App
- Run the application in IDE and command line interface would be initiated
- Enter "create-pr" to create pull request. Enter the necessary details for creating pull request and finally save.
- Enter "merge-pr" to merge a pull request. Enter the necessary details for creating pull request and finally save.
- This app could be converted into a native app using gradle nativeBuild and executable file could be directly run in local machine.
