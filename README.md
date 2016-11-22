# Scheduling API
 
[![Build Status](http://jenkins.activeeon.com/buildStatus/icon?job=scheduling-api)](http://jenkins.activeeon.com/job/scheduling-api)
 
At this time, the Scheduling API offers a GraphQL endpoint for getting information about a ProActive Scheduler instance.
  
**Please note the API is still experimental and may be subject to changes.**

## Building and deploying

You can build a WAR file as follows:

```
$ gradle clean build war
```

The last command produces a WAR file in 

```
build/libs/scheduling-api-X.Y.Z-SNAPSHOT.war
```

This Web Application Archive can be deployed in the embedded Jetty container run by an instance of [ProActive Server](https://github.com/ow2-proactive/scheduling) (or your own application server).

The standard produce is to copy or create a symlink to the WAR file in `$PROACTIVE_HOME/dist/war`.

## Usage

Once deployed and running in a ProActive Scheduler instance, a graphical in-browser interface called [GraphiQL](https://github.com/graphql/graphiql) is accessible at:

http://localhost:8080/scheduling-api/v2/

## What is missing for now
 
- Complete Scheduler schema (i.e. returning Task progress, scripts content, etc.)
  See `JobDataFetcherTest` and `TaskDataFetcherTest`.
- Schema definition and fetchers for Resource Manager
- Field arguments refinement based on use cases
  (i.e. Studio, Scheduler and RM portals)
- Sorting per field (by adding an `orderBy` argument)
  See https://github.com/graphql/graphql-relay-js/issues/20#issuecomment-220494222
- Forwarding of all requests received on `scheduling-api/v1` to `/rest`
- Mutations (i.e. for submitting workflows, locking nodes, etc.)
