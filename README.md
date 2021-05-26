# Corda Features Test

This repository demonstrates various features of Corda.

To deploy different CorDapps:
1. Change the configurations in settings.gradle to include desired projects.
2. Change the 'app' property in gradle.properties.
3. Run `./gradlew jar` to generate new jar files
4. Run `./update $app` to copy these files to Node cordapp directories.

## Persistence

The database provided by Corda nodes can store both on-ledger data and off-ledger data. Database
can be accessed via native jdbc connection or JPA.

States can be made queryable by implementing QueryableState interface. Off-ledger data storage needs
to be registered with hibernate.

### Concepts

To use the Corda node built-in database, you need to subclass `MappedSchema`, which allows Corda
knows what you want to do to the database.

Your created schema is required to belong to a schema family, which allows smooth future upgrade. Also required
are various tables and constraints (hence `Schema`).

The way it works is that during the node start-up, Corda will read information defined in your cordapps and update
database (migration) accordingly. Database migration currently (it seems) must be done manually by running 
`java -jar corda.jar --run-migration-scripts --core-schemas --app-schemas --allow-hibernate-to-manage-app-schema` or else
node start-up would fail.

### Queryable State
`QueryableState` is a convenient feature in that it automates the process of creating new entries when new states are
recorded. To implement it, you need to define two methods: `supportedSchema()` which tells Corda which schema this state
will be recored in, and `generateMappedObject` which maps the State object to Entity object based on the given schema.

### Sample Usage