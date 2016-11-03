# Scheduling API

## Query examples

```
$ http POST http://localhost:8080/graphql query='{ version }' 
$ http POST http://localhost:8080/graphql query='{ jobs(first: 3) { edges { cursor node { name id }  } } }' 
$ echo '{"query": "{ __schema { types { name }}}", "variables": {}}' | http POST localhost:8080/graphql
```
