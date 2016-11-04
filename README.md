# Scheduling API

## Query examples

The following examples will have to be transformed into integration tests
### GET

```
$ http http://localhost:8080/v2/graphql?query={version}
$ http http://localhost:8080/v2/graphql?query="{ jobs(first: 3) { edges { cursor node { name }  } } }"
$ http http://localhost:8080/v2/graphql query=="query q1 {version} query q2 {name}" operationName==q1
$ http http://localhost:8080/v2/graphql query=="query q1 {version} query q2 {name}" operationName==q2
$ http http://localhost:8080/v2/graphql query=="query listJobs($count) { jobs(first: $count) { edges { cursor node { name }  } } }" variables=="{count: 3}"
```

### POST
```
$ http POST http://localhost:8080/v2/graphql query='{ version }' 
$ http POST http://localhost:8080/v2/graphql query='{ jobs(first: 3) { edges { cursor node { name id }  } } }' 
$ echo '{"query": "{ __schema { types { name }}}", "variables": {}}' | http POST localhost:8080/v2/graphql
$ http POST http://localhost:8080/v2/graphql query='query($count:Int!) { jobs(first: $count) { edges { cursor node { id name } } } }' variables='{"count": 3}'
```
