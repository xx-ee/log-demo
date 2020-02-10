package com.log.utils
import java.sql.DriverManager
import java.sql.Date
import java.sql.PreparedStatement
object MysqlUtil {
  def upsertTongji2(t: (Long, Int, Int, Int, Int, Int)){
    //1.注册数据库驱动
    Class.forName("com.mysql.cj.jdbc.Driver")
    //2.获取数据库连接
    val conn = DriverManager.getConnection("jdbc:mysql://192.168.154.21:3306/fluxdb","root","123456789")
//    val conn = DriverManager.getConnection("jdbc:mysql://192.168.154.3:3306/test_db","root","123456")
    //3.获取传输器
    val ps = conn.prepareStatement("select * from tongji2 where reportTime = ?")
    ps.setDate(1, new Date(t._1))
    //4.执行语句
    val rs = ps.executeQuery()
    //5.处理结果
    var psx:PreparedStatement = null;
    if(rs.next()){
      //--有数据，修改
      psx = conn.prepareStatement("update tongji2 set pv=pv+?,uv=uv+?,vv=vv+?,newip=newip+?,newcust=newcust+? where reportTime=?")
      psx.setInt(1, t._2);
      psx.setInt(2, t._3);
      psx.setInt(3, t._4);
      psx.setInt(4, t._5);
      psx.setInt(5, t._6);
      psx.setDate(6, new Date(t._1))
      psx.executeUpdate()
    }else{
      //--没数据，新增
      psx =  conn.prepareStatement("insert into tongji2(reportTime,pv,uv,vv,newip,newcust) values (?,?,?,?,?,?)")
      psx.setDate(1, new Date(t._1))
      psx.setInt(2, t._2);
      psx.setInt(3, t._3);
      psx.setInt(4, t._4);
      psx.setInt(5, t._5);
      psx.setInt(6, t._6);
      psx.executeUpdate()
    }
    //6.关闭资源
    if(psx!=null)psx.close();
    if(rs!=null)rs.close();
    if(ps!=null)ps.close();
    if(conn!=null)conn.close()
  }
}
