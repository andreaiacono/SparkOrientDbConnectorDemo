import com.metreta.spark.orientdb.connector.{ClassRDDFunctions, GraphFunctions, SparkContextFunctions}
import org.apache.spark.graphx.{Edge, _}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


package object sample {

  implicit def toSparkContextFunctions(sc: SparkContext): SparkContextFunctions = new SparkContextFunctions(sc)
  implicit def toRDDFunctions[T](rdd: RDD[T]): ClassRDDFunctions[T] = new ClassRDDFunctions(rdd)
  implicit def toGraphxFunctions[V, E](graph: Graph[V, E]): GraphFunctions[V, E] = new GraphFunctions(graph)

  val OrientDBNodesProperty = "spark.orientdb.connection.nodes"
  val OriendtDBProtocolProperty = "spark.orientdb.protocol"
  val OriendtDBDBNameProperty = "spark.orientdb.dbname"
  val OriendtDBPortProperty = "spark.orientdb.port"
  val OriendtDBUserProperty = "spark.orientdb.user"
  val OriendtDBPasswordProperty = "spark.orientdb.password"
  val OriendtDBClusterModeProperty = "spark.orientdb.clustermode" //remote-colocated

  val conf = new SparkConf()
    .setMaster("local[*]")
    .setAppName("demo")
    .set(OrientDBNodesProperty, "127.0.0.1")
    .set(OriendtDBProtocolProperty,  "remote")
    .set(OriendtDBDBNameProperty, "test2")
    .set(OriendtDBPortProperty,  "2424")
    .set(OriendtDBUserProperty,"admin")
    .set(OriendtDBPasswordProperty, "admin")
    .set(OriendtDBClusterModeProperty, "remote")

  def connectionUri() = conf.get(OriendtDBProtocolProperty) + ":/" + conf.get(OriendtDBDBNameProperty)
  def dbUser() = conf.get(OriendtDBUserProperty)
  def dbPassword() = conf.get(OriendtDBPasswordProperty)


  def createSampleGraph(sparkContext: SparkContext): Graph[Person, String] = {

    val people: RDD[(VertexId, Person)] =
      sparkContext.parallelize(
        Array(
          (1L, new Person("Alice", "Anderson")),
          (2L, new Person("Bob", "Brown")),
          (3L, new Person("Carol", "Clark"))
        )
      )

    val edges: RDD[Edge[String]] =
      sparkContext.parallelize(
        Array(
          Edge(1L, 2L, "Friendship"),
          Edge(1L, 3L, "Friendship"),
          Edge(2L, 1L, "Friendship"),
          Edge(3L, 1L, "Friendship"),
          Edge(3L, 2L, "Friendship")
        )
      )
    Graph(people, edges)
  }

}
