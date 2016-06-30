package sample

import com.metreta.spark.orientdb.connector.rdd.OrientDocument
import org.apache.spark.SparkContext
import org.apache.spark.graphx._


object GraphSample extends App {

  // creates a SparkContext for connecting to OrientDB
  val sc = new SparkContext(conf)
  sc.setLogLevel("WARN")

  // retrieves an instance of the graph database
  val peopleGraph : Graph[OrientDocument, OrientDocument] = sc.orientGraph()

  // reads entries as graphX vertices/edges
  val people: VertexRDD[OrientDocument] = peopleGraph.vertices
  val relationships: EdgeRDD[OrientDocument] = peopleGraph.edges
  println(s"The graph contains ${people.count()} vertices and ${relationships.count()} edges.\n")

  // reads data as triplets
  println("People that has friends: ")
  peopleGraph
    .triplets
    .foreach(triplet => {
      val srcPerson: OrientDocument = triplet.srcAttr
      val dstPerson: OrientDocument = triplet.dstAttr
      println(s"Person: ${srcPerson.getString("surname")}, ${srcPerson.getString("name")} [${triplet.srcId}]. Friend: ${dstPerson.getString("surname")}, ${dstPerson.getString("name")} [${triplet.dstId}]")
    })

  // operates the triangle count
  val triangles = peopleGraph.triangleCount()

  // prints how many triangles every vertex participate in
  triangles
    .vertices
    .foreach {
      case (vertexId, trianglesNumber) => println(s"Person [${vertexId}] participates in ${trianglesNumber} triangles.")
    }

  // prints the total number of triangles
  println("Total number of triangles in the graph: " + triangles.vertices
    .map {
      case (vertexId, trianglesNumber) => trianglesNumber.toLong
    }
    .reduce(_ + _ / 3)
  )


  // creates a sample GraphX graph and saves it to OrientDB
  val gr: Graph[Person, String] = createSampleGraph(sc)
  gr.saveGraphToOrient()
}
