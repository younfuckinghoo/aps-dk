package com.aps.yinghai;

import oracle.jdbc.driver.OracleDriver;

import java.sql.*;
import java.util.Properties;

public class IGTOSOracle {

    public static void main(String[] args) {
        Connection connect = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            //第一步：注册驱动
            //第一种方式：类加载(常用)
            //Class.forName("oracle.jdbc.OracleDriver");
            //第二种方式：利用Driver对象
            Driver driver = new OracleDriver();
            DriverManager.deregisterDriver(driver);


            //第二步：获取连接
            //第一种方式：利用DriverManager（常用）
            //connect = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "你的oracle数据库用户名", "用户名密码");
            //第二种方式：直接使用Driver
            Properties pro = new Properties();
            pro.put("user", "IGTOS_NEW");
            pro.put("password", "QGDK_ignew9");
            connect = driver.connect("jdbc:oracle:thin:@//10.236.2.72:1521/ORCL", pro);

            //测试connect正确与否
            System.out.println(connect);


            //第三步：获取执行sql语句对象
            //第一种方式:statement
            //statement = connect.createStatement();
            //第二种方式：PreStatement
            PreparedStatement preState = connect.prepareStatement("select  1 from dual");


            //第四步：执行sql语句
            //第一种方式：
            //resultSet = statement.executeQuery("select  * from tb1_dept");
            //第二种方式：
            //1是指sql语句中第一个？，2是指第一个？的values值
//            preState.setInt(1, 2);
            //执行查询语句
            resultSet = preState.executeQuery();
            //执行的任何查询语句，如果有结果集，则返回true，没有的话返回false，注意如果是插入一条数据的话，虽然是没有结果集，返回false，但是却能成功的插入一条数据
//            boolean execute = preState.execute();
//            System.out.println(execute);

            //第五步：处理结果集
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                System.out.println(id);
//                String name = resultSet.getString("name");
//                String city = resultSet.getString("city");
//                //打印输出结果集
//                System.out.println( id + " " + name + " " + city );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //第六步：最后无论如何要关闭资源
            try {
                if (resultSet!=null) resultSet.close();
                if (statement!=null) statement.close();
                if (connect!=null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
