val nasa = sc.textFile("/user/kainos/nasa-data/*")

// Zadanie 1
val regex = """.*?(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3}).*""" 
val output1 = nasa.map(_.split('\t')(0)).filter(_.matches(regex))
val percentage = (nasa.count().toFloat / output.count().toFloat) * 100

// Zadanie 2
val output2 = nasa.map({line => val splitted = line.split('\t'); (splitted(0), splitted(3))}).filter(_._2 == "GET").map(x => (x._1, 1)).reduceByKey(_ + _)
val test2 = nasa.map(line => line.split('\t')(0)).filter(_ == "unicompt214.unicomp.net").count()

// Zadanie 3 
import java.util.Date
import java.text.SimpleDateFormat

val numberRegex = """^[0-9]*$"""
val output3 = nasa.map(_.split('\t')).filter(_(2).matches(numberRegex)).sortBy(_(2).toInt).map(x => x.updated(2, new SimpleDateFormat("dd/MM/YYYY:hh:ss:mm").format(x(2).toInt))).map(_.mkString('\t'))
output3.saveAsTextFile("/user/kainos/test")

// Zadanie 4 
/*
CREATE TABLE nasa_data (
host STRING,
logname STRING,
time STRING,
method STRING,
url STRING,
response STRING,
bytes STRING,
referer STRING,
useragent STRING
)
LOCATION '/user/kainos/test'
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t';

ALTER TABLE nasa_data SET LOCATION '/user/kainos/test';
*/

