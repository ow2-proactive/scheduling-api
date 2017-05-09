# Scheduling API

[![Build Status](http://jenkins.activeeon.com/buildStatus/icon?job=scheduling-api)](http://jenkins.activeeon.com/job/scheduling-api)

At this time, the Scheduling API offers a GraphQL endpoint for getting information about a ProActive Scheduler instance.

**Please note the API is still experimental and may be subject to changes.**

## Development

Importing the project into your IDE the first time might display some missing classes issues messages, as some of them are auto generated on the first build. You will need to call the `build` task from the `scheduling-api-graphql-beans-output` submodule for the IDE to resolve the missing classes properly. You can use the following command to do so:

```
gradle clean :scheduling-api-graphql:scheduling-api-graphql-beans:scheduling-api-graphql-beans-output:build
```

The rest of the project can then be compiled from your IDE or from command-line with the following command:
```
gradle clean build
```

## Building and deploying

You can build a WAR file as follows:

```
$ gradle clean build war
```

The last command produces a WAR file in

```
scheduling-api-http/build/libs/scheduling-api-X.Y.Z-SNAPSHOT.war
```

This Web Application Archive can be deployed in the embedded Jetty container run by an instance of [ProActive Server](https://github.com/ow2-proactive/scheduling) (or your own application server).

The standard procedure is to copy or to create a symlink to the WAR file in `$PROACTIVE_HOME/dist/war`.

## Usage

Once deployed and running in a ProActive Scheduler instance, a graphical in-browser interface called [GraphiQL](https://github.com/graphql/graphiql) is accessible at:

[http://localhost:8080/scheduling-api/v1/graphiql](http://localhost:8080/scheduling-api/v1/graphiql)

## What is missing for now

- Tests for the Java client
- Complete Scheduler schema (i.e. returning Task progress, scripts content, etc.)
  See `JobDataFetcherTest` and `TaskDataFetcherTest`.
- Schema definition and fetchers for Resource Manager
- Field arguments refinement based on use cases
  (i.e. Studio, Scheduler and RM portals)
- Sorting per field (by adding an `orderBy` argument)
  See https://github.com/graphql/graphql-relay-js/issues/20#issuecomment-220494222
- Forwarding of all requests received on `scheduling-api/v1` to `/rest`
- Mutations (i.e. for submitting workflows, locking nodes, etc.)
