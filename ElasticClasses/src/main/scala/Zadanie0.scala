import java.sql.DriverManager
import java.sql.Connection
import java.util

import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import scalaj.http.{Http, HttpOptions}

object Zadanie0 extends App {
  case class Product(product_id: String, product_category_id: String, product_name: String, product_description: String, product_price: String, product_image: String)

  val driver = "com.mysql.jdbc.Driver"
  val url = "jdbc:mysql://quickstart.cloudera:3306/retail_db"
  val username = "retail_dba"
  val password = "cloudera"

  var conn: Connection = null

  try {
    conn = DriverManager.getConnection(
      url, username, password
    )

    val queryString = "select * from products"
    val resultSet = conn.createStatement()
      .executeQuery(queryString)

    while(resultSet.next) {
      val productId = resultSet.getString("product_id")
      val productCategoryId = resultSet.getString("product_category_id")
      val productName = resultSet.getString("product_name")
      val productDescription = resultSet.getString("product_description")
      val productPrice = resultSet.getString("product_price")
      val productImage = resultSet.getString("product_image")
      val product = Product(productId, productCategoryId, productName, productDescription, productPrice, productImage)

      val json =
        "product" ->
          ("product_id" -> product.product_id) ~
            ("product_category_id" -> product.product_category_id) ~
            ("product_name" -> product.product_name) ~
            ("product_description" -> product.product_description) ~
            ("product_price" -> product.product_price) ~
            ("product_image" -> product.product_image)

      val result = Http("http://127.0.0.1:9200/products/all3").postData(prettyRender(json))
        .option(HttpOptions.readTimeout(10000)).asString

      println(result)
    }
  } catch {
    case ex: Throwable => ex.printStackTrace()
  } finally {
    if (conn != null) {
      conn.close()
    }
  }
}
