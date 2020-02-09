import com.log.utils.MysqlUtil

object test {
  def main(args: Array[String]): Unit = {
    val a1= "22:20:07.187 INFO  com.logs.controller.LogController - (LogController.java:43) - http://localhost:8080/b|b|B??|UTF-8|1536x864|24-bit|zh-cn|0|1|32.0 r0*|0.9566059343078407|http://localhost:8080/a|Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:72.0) Gecko/20100101 Firefox/72.0|72740539703796005261|7197521626_27_1581258007183|127.0.0.1"
    println(a1.split(" - ")(2))
    println(a1.split(" - ")(2))
    println(a1.split(" - ")(2).matches("^([^\\|]*\\|){15}[^\\|]*$"))
    println(a1.split(" - ")(2).split("\\|").length)

    MysqlUtil.upsertTongji2(1581261073574l,1,1,1,1,1);
  }
}
