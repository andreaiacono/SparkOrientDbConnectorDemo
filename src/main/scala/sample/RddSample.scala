package sample

import com.metreta.spark.orientdb.connector.rdd.OrientDocument
import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext

object RddSample extends App {

  // creates a SparkContext for connecting to OrientDB
  val sc = new SparkContext(conf)
  sc.setLogLevel("WARN")

  // reads all entries of class Person from Orient as an RDD
  var peopleRdd: RDD[OrientDocument] = sc.orientQuery("Person")

  // creates a new person and saves it as a new vertex in OrientDB
  peopleRdd
    .filter(person => person.getString("name") == "John")
    .map(person => new Person("Foo", "Bar"))
    .saveToOrient("Person")

  // logs the newly inserted person
  println("existing people and new person:")
  sc.orientQuery("Person").foreach(p => println(s"Person: ${p.getString("surname")}, ${p.getString("name")}"))

  // manipulates it as a normal RDD and updates existing data into OrientDB
  peopleRdd
    .filter(person => !person.getString("surname").startsWith("New"))
    .map(person => new Person(person.getString("name"), "New " + person.getString("surname")))
    .upsertToOrient("Person")

  // logs the updated people
  println("Updated people:")
  sc.orientQuery("Person").foreach(p => println(s"Person: ${p.getString("surname")}, ${p.getString("name")}"))
}
