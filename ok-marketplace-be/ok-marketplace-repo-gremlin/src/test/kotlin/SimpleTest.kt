package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import com.arcadedb.gremlin.ArcadeGraphFactory
import com.arcadedb.query.sql.executor.ResultSet
import com.arcadedb.remote.RemoteDatabase
import com.arcadedb.remote.RemoteServer
import org.apache.tinkerpop.gremlin.driver.AuthProperties
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.structure.VertexProperty
import org.junit.Test
import kotlin.test.Ignore
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as bs

/**
 * Для запуска тестов этого файла требуется запустить локальный экземпляр ArcadeDB
 * Можно использовать файл /deploy/docker-compose-arcadedb.yml
 */
//@Ignore("Тест для экспериментов")
class SimpleTest {
    private val host: String = "localhost"
    private val user: String = "root"
    private val pass: String = "root_root"

    private val dbName: String = "graph"

//     private val dbName: String = "mkpl" // Этот граф должен быть настроен в /home/arcadedb/config/gremlin-server.groovy
    private val aPort: Int = 2480
    private val gPort: Int = 8182

    /**
     * Пример запроса без использования библиотеки Gremlin.
     * Дает доступ к другим графам данных без ограничений.
     */
    @Test
    fun arcadeSimple() {
        val server = RemoteServer(host, aPort, user, pass)
        if (!server.exists(dbName)) {
            server.create(dbName)
        }
        assert(server.databases().contains(dbName))
        RemoteDatabase(host, aPort, dbName, user, pass).use { db ->
            val res: ResultSet = db.command("gremlin", "g.V().elementMap().toList()")
            res.forEachRemaining { block ->
                val vxList = block.getProperty<List<Map<String, String>>>("result")
                vxList.forEach { vx ->
                    println("V:")
                    vx.forEach { p ->
                        println("  ${p.key}=${p.value}")
                    }
                }
            }
        }
    }

    /**
     * Организуем соединение с БД средствами ArcadeDB, выполняем запросы с помощью Gremlin
     * Доступ по умолчанию только к graph.
     */
    @Test
    fun arcadeConnection() {
        ArcadeGraphFactory.withRemote(host, aPort, dbName, user, pass).use { pool ->
            pool.get().use { graph ->
                val userId = graph.traversal()
                    .addV("User")
                    .property(VertexProperty.Cardinality.single, "name", "Evan")
                    .next()
                    .id()
                println("UserID: $userId")
            }
        }
    }

    /**
     * Работа только средствами Gremlin
     */
    @Test
    fun gremlinConnection() {
        val authProp = AuthProperties().apply {
            with(AuthProperties.Property.USERNAME, user)
            with(AuthProperties.Property.PASSWORD, pass)
        }
        val cluster = Cluster.build()
            .addContactPoints(host)
            .port(gPort)
            .authProperties(authProp)
            .create()
        traversal()
            //               Этот граф должен быть указан в /home/arcadedb/config/gremblin-server.groovy
            .withRemote(DriverRemoteConnection.using(cluster, dbName))
            .use { g ->
                val userId = g
                    .addV("User")
                    .property(VertexProperty.Cardinality.single, "name", "Evan")
                    .next()
                    .id()
                println("UserID: $userId")
            }
    }

    /**
     * Демонстрация project
     */
    @Test
    fun projectTest() {
        val cluster = Cluster.build().apply {
            addContactPoints(host)
            port(gPort)
            credentials(user, pass)
        }.create()
        traversal()
            .withRemote(DriverRemoteConnection.using(cluster)).use { g ->
                val x = g.V().hasLabel("Test")
                    .toList()
                println("CONTENT: ${x}")

                val y = g.V().hasLabel("Test") //.`as`("a")
                    .project<Any?>("lock", "ownerId", "z")
                    .by("lock")
                    // Извлекаем ID связанного объекта (связь через грань Owns
                    .by(bs.inE("Owns").outV().id())
                    .by(bs.elementMap<Vertex, Map<Any?, Any?>>())
                    .toList()
                println("CONTENT: $y")
            }
    }

    /**
     * Демонстрация некоторых возможностей Gremlin
     */
    @Test
    fun gremlinExamples() {
        val cluster = Cluster.build().apply {
            addContactPoints(host)
            port(gPort)
            credentials(user, pass)
        }.create()
        traversal().withRemote(DriverRemoteConnection.using(cluster)).use { g ->
            // Создаем узел
            val userId = g
                .addV("User")
                .property("name", "Ivan")
                .next()
                .id()
            println("UserID: $userId")

            // Создаем узел и привязываем его к предыдущему через связь Owns
            val id = g
                .addV("Test")
                .`as`("a")
                .property("lock", "111")
                .addE("Owns")
                .from(bs.V<Vertex>(userId))
                .select<Vertex>("a")
                .next()
                .id()
            println("ID: $id")

            // Это поиск связи по ID
            val owner = g
                .V(userId)
                .outE("Owns")
                .where(bs.inV().id().`is`(id))
                .toList()
            println("OWNER: $owner")

            // Запрос с условием choose
            val n = g
                .V(id)
                .`as`("a")
                .choose(
                    bs.select<Vertex, Vertex>("a")
                        .values<String>("lock")
                        .`is`("111"),
                    bs.select<Vertex, String>("a").drop().inject("success"),
                    bs.constant("lock-failure")
                ).toList()
            println("YYY: $n")
        }
    }
}
