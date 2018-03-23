import cakesolutions.kafka.KafkaProducer.Conf
import cakesolutions.kafka.{KafkaProducer, KafkaProducerRecord}
import net.liftweb.json.prettyRender
import org.apache.kafka.common.serialization.StringSerializer

import java.sql.DriverManager
import java.sql.Connection

import net.liftweb.json.JsonDSL._

object Zadanie1 extends App {
  case class Customer(customer_id: String, customer_fname: String, customer_lname: String, customer_email: String, customer_password: String, customer_street: String, customer_city: String, customer_state: String, customer_zipcode: String)

  val driver = "com.mysql.jdbc.Driver"
  val url = "jdbc:mysql://quickstart.cloudera:3306/retail_db"
  val username = "retail_dba"
  val password = "cloudera"

  var conn: Connection = null

  try {
    conn = DriverManager.getConnection(
      url, username, password
    )

    val queryString = "select * from customers"
    val resultSet = conn.createStatement()
      .executeQuery(queryString)

    val producer = KafkaProducer(
      Conf(new StringSerializer(), new StringSerializer(), bootstrapServers = "localhost:9092")
    )

    while (resultSet.next) {
      val customer_id = resultSet.getString("customer_id")
      val customer_fname = resultSet.getString("customer_fname")
      val customer_lname = resultSet.getString("customer_lname")
      val customer_email = resultSet.getString("customer_email")
      val customer_password = resultSet.getString("customer_password")
      val customer_street = resultSet.getString("customer_street")
      val customer_city = resultSet.getString("customer_city")
      val customer_state = resultSet.getString("customer_state")
      val customer_zipcode = resultSet.getString("customer_zipcode")
      val customer = Customer(customer_id, customer_fname, customer_lname, customer_email, customer_password, customer_street, customer_city, customer_state, customer_zipcode)

      val json =
        "customer" ->
          ("customer_id" -> customer.customer_id) ~
            ("customer_fname" -> customer.customer_fname) ~
            ("customer_lname" -> customer.customer_lname) ~
            ("customer_email" -> customer.customer_email) ~
            ("customer_password" -> customer.customer_password) ~
            ("customer_street" -> customer.customer_street)
            ("customer_city" -> customer.customer_city) ~
            ("customer_state" -> customer.customer_state) ~
            ("customer_zipcode" -> customer.customer_zipcode)

      producer.send(KafkaProducerRecord("customers", Some("customer"), prettyRender(json)))
    }

    producer.close()
  }
}