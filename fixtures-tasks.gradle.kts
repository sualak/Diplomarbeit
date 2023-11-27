import org.gradle.api.tasks.Exec
import java.util.Properties


// Load And Configure Fixtures Properties
// --------------------------------------------

// Define a function to load properties from `application.properties`
fun loadProperties(properitesPath: String): Properties
{
    val properties = Properties()
    val propertiesFile = file(properitesPath)
    propertiesFile.reader().use(properties::load)
    return properties
}

// Load fixtures properties
val testProperties = loadProperties("src/test/resources/application.properties")
val mainProperties = loadProperties("src/main/resources/application.properties")

// Configure the fixture properties
val _dbNameMain = mainProperties.getProperty("spring.data.mongodb.database")!!
val _dbNameTest = testProperties.getProperty("spring.data.mongodb.database")!!
val _fixturesPath = testProperties.getProperty("fixtures.path")!!
val _collectionList = listOf("user")
val _collectionFixturesClassMap = _collectionList.associateBy { "${it.capitalize()}Fixture" }
val _collectionScriptList = _collectionList.map { "create-${it}.sh" }



// Generate Fixtures
// --------------------------------------------

// This task is used to generate all fixtures used to test the application.
// ./gradlew generateFixtures --fixturesPath=src/test/resources/fixtures --amount=10
val generateFixtures = tasks.register<GenerateFixturesTask>("generateFixtures") {
    group = "database"
    description = "Generates user and product fixtures"

    amount = project.findProperty("amount") as String? ?: "10"
    fixturesPath = project.findProperty("fixturesPath") as String? ?: _fixturesPath
    collectionFixturesClassMap = _collectionFixturesClassMap
}

open class GenerateFixturesTask : DefaultTask()
{
    @Input var amount = ""
    @Input var fixturesPath = ""
    @Input var collectionFixturesClassMap = mapOf<String, String>()

    @TaskAction
    fun generateAllFixtures() {
        val projectSourceSets = project.extensions.getByName("sourceSets") as org.gradle.api.tasks.SourceSetContainer
        val classpath = projectSourceSets.getByName("test").runtimeClasspath

        collectionFixturesClassMap.forEach { (fixtureClassName, collection) ->
            project.javaexec {
                this.classpath = classpath
                mainClass.set("at.dertanzbogen.api.fixtures.$fixtureClassName")
                args = listOf(amount, "$fixturesPath/$collection.json")
            }
        }
    }
}



// Import Fixtures
// --------------------------------------------

// This task is used to import all fixtures
// ./gradlew importFixtures --dbName=shopping-portal --fixturePath=src/main/resources/fixtures
val importFixtures = tasks.register<ImportFixturesTask>("importFixtures") {
    group = "database"
    description = "Imports fixture data for all collections"

    dbName = project.findProperty("dbName") as String? ?: _dbNameMain
    fixturesPath = project.findProperty("fixturesPath") as String? ?: _fixturesPath
    collectionList = _collectionList
}

open class ImportFixturesTask : DefaultTask()
{
    @Input var dbName: String = ""
    @Input var fixturesPath: String = ""
    @Input var collectionList = listOf<String>()

    @TaskAction
    fun importFixtures() {
        collectionList.forEach { collection ->
            project.exec {
                commandLine("mongoimport", "--db", dbName, "--collection", collection,
                    "--file", "$fixturesPath/$collection.json", "--jsonArray")
            }
        }
    }
}



// Create Collections
// --------------------------------------------

// This task is used to create all collections
// ./gradlew createCollections --dbName=shopping-portal
val createCollections = tasks.register<CreateCollectionsTask>("createCollections") {
    group = "database"
    description = "Applies JSON schema validation for all collections"
    dbName = project.findProperty("dbName") as String? ?: _dbNameMain
    collectionScriptList = _collectionScriptList
}

open class CreateCollectionsTask : DefaultTask()
{
    @Input var dbName: String = ""
    @Input var collectionScriptList = listOf<String>()

    @TaskAction
    fun applySchemas() {
        collectionScriptList.forEach { collectionScript ->
            project.exec {
                commandLine("sh", "./tools/db/$collectionScript", dbName)
            }
        }
    }
}



// Drop Fixtures
// --------------------------------------------

// This task is used to delete all fixtures
// ./gradlew dropFixtures --fixturesPath=src/main/resources/fixtures
val dropFixtures = tasks.register<Delete>("dropFixtures") {
    group = "database"
    description = "Deletes all fixtures"

    val fixturesPath = project.findProperty("fixturesPath") as String? ?: _fixturesPath
    delete(fileTree(fixturesPath) { include("**/*") })
}



// Drop Database
// --------------------------------------------

// This task is used to drop the database
// ./gradlew dropDatabase --dbName=shopping-portal
val dropDatabase = tasks.register<Exec>("dropDatabase") {
    group = "database"
    description = "Drops database"

    val dbName = project.findProperty("dbName") as String? ?: _dbNameMain
    commandLine("mongosh", dbName, "--eval", "db.dropDatabase()")
}



// Seed Main Database
// --------------------------------------------

val seedMainDatabase = tasks.register<Exec>("seedMainDatabase") {
    group = "database"
    description = "Seeds main database with schemas and fixtures"

    val dbName = project.findProperty("dbName") as String? ?: _dbNameMain
    val fixturesPath = project.findProperty("fixturesPath") as String? ?: _fixturesPath
    val amount = project.findProperty("amount") as String? ?: "10"

    commandLine("./tools/db/seed-database.sh", dbName, fixturesPath, amount)
}



// Seed Test Database
// --------------------------------------------

val seedTestDatabase = tasks.register<Exec>("seedTestDatabase") {
    group = "database"
    description = "Seeds test database with schemas and fixtures"

    val dbName = project.findProperty("dbName") as String? ?: _dbNameTest
    val fixturesPath = project.findProperty("fixturesPath") as String? ?: _fixturesPath
    val amount = project.findProperty("amount") as String? ?: "10"

    commandLine("./tools/db/seed-database.sh", dbName, fixturesPath, amount)
}



// sourceSets does not work here because this script is not part of the gradle project
// classpath = sourceSets.test.get().runtimeClasspath

// This is a workaround to access the sourceSets extension:
// project.extensions.getByName("sourceSets") accesses the sourceSets extension
// projectSourceSets.getByName("test") accesses the test source set

