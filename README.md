# DD2480-Assignment2
#### A project by [4 KTH students](#developers-and-their-main-contributions)

## Git Commit Message Template
### `type(domain): Description #issuenumber`
### Types
- `build`: Changes that affect the build system or external dependencies (example scopes: broccoli, npm).
- `docs`: Documentation only changes.
- `feat`: A new feature. Can of course also contain tests for the feature.
- `fix`: Bug fixes. Can of course also contain tests for the feature.
- `refactor`: A code change that neither fixes a bug nor adds a feature.
- `revert`: Reverts a previous commit.
- `style`: Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc).
- `test`: Adding missing tests or correcting existing tests.

### Example of domains
- `ngrok`
- `API`
- `Event`
- `WebHook`

##
## Gradle tutorial
To build and test the Gradle project, you can use the following commands:

- `gradle build`: This command will build the project and create the executable files.
- `gradle test`: This command will run all the tests defined in the project.
- `gradle check`: This command will run the tests as well as check for any code style violations and other potential issues.
- `gradle clean`: This command will delete all the build files generated by previous builds.

## Run on KTH-machine tutorial
- `./gradlew -PmainClass=ContinuousIntegrationServer run`: This command will run the java class named "ContinuousIntegrationServer"
- `./ngrok http 8028`: Forward localhost:8028 to ngrok public IP

## Team assessment



Based on the checklist criterias in p. 52 in the [Essence standard](https://www.omg.org/spec/Essence/1.2/PDF) v1.2, the group was evaluated to be in the state of “Collaborating”. Since finishing the first assignment and continuing on the second, the group has developed an honest and open communication with each other, where everyone has gotten to know every group member well. The team has throughout the assignment been focused on achieving the goal set up from the first briefing of the task and felt like a cohesive unit. 

To reach the next state “Performing”, the group agreed on the facts that they fulfill some of the requirements but fails to fully reach some of the criterias. Since the project got stalled in the beginning phase, setting up the server, the group identified the problems but reached out to TAs in order to fully address the problems, which resulted in failing the criteria of addressing problems without outside help. This initial problem of the project resulted in some time wasted which was identified but also took some time to eliminate. 


## Notification 

When a pull request is made the status of every commit is shown on the github page. The status is `SUCCESS` if the build compiles and tests correctly, `PENDING` for ongoing builds, and `FAILURE` for failed tests and build.

## Build History
The build history can be accessed by this URL and for each status notification, one can also acccess the build output of their commit. 
https://69c7-2001-6b0-1-1df0-2be-43ff-feb8-c810.eu.ngrok.io/builds

## Contributions

Adam - Code architecture, Json handling, build history.
Porsev - Commit status, REST API calls, JGIT.
David - Tests, REST API, server setup.
Pontus - Tests, notifications, build history.


