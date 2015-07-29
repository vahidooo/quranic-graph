# quranic-graph

## Requirements
Java 1.7
Neo4j 2.1
Gradle 2.2 (optional)

## Installation
1. Download neo4j-2.2+ from [Neo4j download page] (http://neo4j.com/download/)
2. Extract it to project root directory(after that you must have *'quranic-graph/neo4j'* directory) and rename it to *'neo4j'*
3. Compile and deploy plugin via `"./gradlew deploy"` command.(Instead of using ./gradlew,I recommend that install gradle)
4. Run Neo4j by `"./neo4j/bin/neo4j restart"`

## Upgrade database
After running the Neo4j server,to upgrade database with new DataFillers you must make a GET request to *'/quran/data/upgrade'*

## Drop database
`"./gradlew dropDatabase"` command will drop database by remove *'neo4j/data/graph.db'* directory.



## Queries


## How to develop
### DataFillers
### Quran API
### WebServices
