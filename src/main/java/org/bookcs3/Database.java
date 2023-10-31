package org.bookcs3;

/**
 * @Author MisakiMikoto
 * @Date 2023/10/31
 */
// 数据库类
import java.sql.*;
import java.util.*;

public class Database {
    // 定义数据库的连接信息
    private static final String URL = "jdbc:mysql://localhost:3306/addressbook"; // 数据库的URL，addressbook是数据库名
    private static final String USER = "root"; // 数据库的用户名
    private static final String PASSWORD = "123456"; // 数据库的密码

    // 定义数据库的连接对象
    private Connection conn;

    // 定义数据库类的构造方法，初始化连接对象
    public Database() {
        try {
            Class.forName("com.mysql.jdbc.Driver"); // 加载MySQL驱动类
            conn = DriverManager.getConnection(URL, USER, PASSWORD); // 获取数据库连接对象
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(); // 打印异常信息
        }
    }

    // 定义数据库类的增加联系人的方法，参数为姓名、电话和邮箱，返回值为布尔类型，表示操作是否成功
    public boolean addContact(String name, String phone, String email) {
        try {
            String sql = "INSERT INTO contacts (name, phone, email) VALUES (?, ?, ?)"; // 定义插入语句，使用占位符代替具体的值
            PreparedStatement ps = conn.prepareStatement(sql); // 创建预编译语句对象
            ps.setString(1, name); // 设置第一个占位符的值为姓名
            ps.setString(2, phone); // 设置第二个占位符的值为电话
            ps.setString(3, email); // 设置第三个占位符的值为邮箱
            int rows = ps.executeUpdate(); // 执行插入语句，并返回影响的行数
            ps.close(); // 关闭预编译语句对象
            return rows > 0; // 如果影响的行数大于0，表示插入成功，返回true；否则返回false
        } catch (SQLException e) {
            e.printStackTrace(); // 打印异常信息
            return false; // 发生异常时，返回false
        }
    }

    // 定义数据库类的修改联系人信息的方法，参数为姓名、电话和邮箱，返回值为布尔类型，表示操作是否成功
    public boolean modifyContact(String name, String phone, String email) {
        try {
            String sql = "UPDATE contacts SET phone = ?, email = ? WHERE name = ?"; // 定义更新语句，使用占位符代替具体的值
            PreparedStatement ps = conn.prepareStatement(sql); // 创建预编译语句对象
            ps.setString(1, phone); // 设置第一个占位符的值为电话
            ps.setString(2, email); // 设置第二个占位符的值为邮箱
            ps.setString(3, name); // 设置第三个占位符的值为姓名
            int rows = ps.executeUpdate(); // 执行更新语句，并返回影响的行数
            ps.close(); // 关闭预编译语句对象
            return rows > 0; // 如果影响的行数大于0，表示更新成功，返回true；否则返回false
        } catch (SQLException e) {
            e.printStackTrace(); // 打印异常信息
            return false; // 发生异常时，返回false
        }
    }

    // 定义数据库类的删除联系人的方法，参数为姓名，返回值为布尔类型，表示操作是否成功
    public boolean deleteContact(String name) {
        try {
            String sql = "DELETE FROM contacts WHERE name = ?"; // 定义删除语句，使用占位符代替具体的值
            PreparedStatement ps = conn.prepareStatement(sql); // 创建预编译语句对象
            ps.setString(1, name); // 设置第一个占位符的值为姓名
            int rows = ps.executeUpdate(); // 执行删除语句，并返回影响的行数
            ps.close(); // 关闭预编译语句对象
            return rows > 0; // 如果影响的行数大于0，表示删除成功，返回true；否则返回false
        } catch (SQLException e) {
            e.printStackTrace(); // 打印异常信息
            return false; // 发生异常时，返回false
        }
    }

    // 定义数据库类的查询所有联系人的方法，无参数，返回值为一个列表，每个元素是一个字符串数组，表示一个联系人的信息
    public List<String[]> queryAllContacts() {
        List<String[]> list = new ArrayList<>(); // 创建一个空的列表，用于存储查询结果
        try {
            String sql = "SELECT * FROM contacts"; // 定义查询语句，选择所有的字段和记录
            Statement st = conn.createStatement(); // 创建语句对象
            ResultSet rs = st.executeQuery(sql); // 执行查询语句，并返回结果集对象
            while (rs.next()) { // 遍历结果集对象，每次取出一条记录
                String[] contact = new String[3]; // 创建一个字符串数组，用于存储一条记录的信息
                contact[0] = rs.getString("name"); // 从结果集对象中获取姓名字段的值，并存入字符串数组的第一个元素
                contact[1] = rs.getString("phone"); // 从结果集对象中获取电话字段的值，并存入字符串数组的第二个元素
                contact[2] = rs.getString("email"); // 从结果集对象中获取邮箱字段的值，并存入字符串数组的第三个元素
                list.add(contact); // 将字符串数组添加到列表中
            }
            rs.close(); // 关闭结果集对象
            st.close(); // 关闭语句对象
        } catch (SQLException e) {
            e.printStackTrace(); // 打印异常信息
        }
        return list; // 返回列表
    }
}
