package LOLIBRARY.util;

/*
DBUtil：数据库工具类，这个类的作用是初始化驱动，
并且提供一个getConnection用于获取连接。 在后续的所有DAO中，当需要获取连接的时候，都采用这种方式进行。
数据库连接的参数，如数据库名称，账号密码，编码方式等都设计在属性上，便于统一修改，降低维护成本。
 */

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;

public class DBUtil {
    static String ip = "127.0.0.1";
    static int port = 3306;
    static String database = "lolibrary";
    static String encoding = "utf-8";
    static String loginName = "root";
    static String password = "as951753258";

    static{
        try{
            Class.forName("com.mysql.jdbc.driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public static Connection getConnection()throws SQLException{
        String url = String.format("jdbc:mysql://%s:%d/%s?characterEncoding=%s",ip,port,encoding,database);
        return DriverManager.getConnection(url,loginName,password);
    }

    public static void main(String[] args)throws SQLException{
        System.out.println(getConnection());
    }
}
