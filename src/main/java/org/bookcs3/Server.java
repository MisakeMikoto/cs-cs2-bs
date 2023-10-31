package org.bookcs3;

/**
 * @Author MisakiMikoto
 * @Date 2023/10/31
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    // 定义服务器的端口号
    private static final int PORT = 8888;

    // 定义服务器类的主方法，启动服务器并等待客户端的连接请求
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT); // 创建服务器套接字，绑定到指定的端口号
            System.out.println("服务器启动成功，等待客户端连接...");
            while (true) { // 循环等待客户端的连接请求
                Socket socket = serverSocket.accept(); // 接受客户端的连接请求，并返回与客户端通信的套接字
                System.out.println("客户端" + socket.getInetAddress() + ":" + socket.getPort() + "已连接");
                new Thread(new Handler(socket)).start(); // 创建一个新的线程，传入与客户端通信的套接字，并启动线程
            }
        } catch (IOException e) {
            e.printStackTrace(); // 打印异常信息
        }
    }
}

// 处理器类，实现Runnable接口，用于处理客户端的请求和响应
class Handler implements Runnable {
    // 定义与客户端通信的套接字和输入输出流
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    // 定义数据库对象，用于操作数据库
    private Database db;

    // 定义处理器类的构造方法，初始化套接字、输入输出流和数据库对象
    public Handler(Socket socket) {
        this.socket = socket; // 将传入的套接字赋值给成员变量socket
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 创建输入流，从套接字中获取客户端发来的数据
            out = new PrintWriter(socket.getOutputStream(), true); // 创建输出流，向套接字中发送数据给客户端，自动刷新缓冲区
        } catch (IOException e) {
            e.printStackTrace(); // 打印异常信息
        }
        db = new Database(); // 创建数据库对象
    }

    // 实现Runnable接口的run方法，处理客户端的请求和响应
    public void run() {
        try {
            String request; // 定义一个字符串变量，用于存储客户端发来的请求信息
            while ((request = in.readLine()) != null) { // 循环读取客户端发来的请求信息，直到读到                null或空字符串
                String[] tokens = request.split(" "); // 将请求信息按空格分割成字符串数组
                String command = tokens[0]; // 获取字符串数组的第一个元素，作为请求的命令
                if (command.equals("ADD")) { // 如果请求的命令是添加
                    if (tokens.length == 4) { // 如果字符串数组的长度是4，表示请求的格式正确
                        String name = tokens[1]; // 获取字符串数组的第二个元素，作为联系人的姓名
                        String phone = tokens[2]; // 获取字符串数组的第三个元素，作为联系人的电话
                        String email = tokens[3]; // 获取字符串数组的第四个元素，作为联系人的邮箱
                        boolean result = db.addContact(name, phone, email); // 调用数据库对象的增加联系人的方法，并获取返回值
                        if (result) { // 如果返回值为true，表示添加成功
                            out.println("添加联系人成功！"); // 向客户端发送响应信息
                        } else { // 如果返回值为false，表示添加失败
                            out.println("添加联系人失败！"); // 向客户端发送响应信息
                        }
                    } else { // 如果字符串数组的长度不是4，表示请求的格式错误
                        out.println("请求格式错误！"); // 向客户端发送响应信息
                    }
                } else if (command.equals("MODIFY")) { // 如果请求的命令是修改
                    if (tokens.length == 4) { // 如果字符串数组的长度是4，表示请求的格式正确
                        String name = tokens[1]; // 获取字符串数组的第二个元素，作为联系人的姓名
                        String phone = tokens[2]; // 获取字符串数组的第三个元素，作为联系人的电话
                        String email = tokens[3]; // 获取字符串数组的第四个元素，作为联系人的邮箱
                        boolean result = db.modifyContact(name, phone, email); // 调用数据库对象的修改联系人信息的方法，并获取返回值
                        if (result) { // 如果返回值为true，表示修改成功
                            out.println("修改联系人信息成功！"); // 向客户端发送响应信息
                        } else { // 如果返回值为false，表示修改失败
                            out.println("修改联系人信息失败！"); // 向客户端发送响应信息
                        }
                    } else { // 如果字符串数组的长度不是4，表示请求的格式错误
                        out.println("请求格式错误！"); // 向客户端发送响应信息
                    }
                } else if (command.equals("DELETE")) { // 如果请求的命令是删除
                    if (tokens.length == 2) { // 如果字符串数组的长度是2，表示请求的格式正确
                        String name = tokens[1]; // 获取字符串数组的第二个元素，作为联系人的姓名
                        boolean result = db.deleteContact(name); // 调用数据库对象的删除联系人的方法，并获取返回值
                        if (result) { // 如果返回值为true，表示删除成功
                            out.println("删除联系人成功！"); // 向客户端发送响应信息
                        } else { // 如果返回值为false，表示删除失败
                            out.println("删除联系人失败！"); // 向客户端发送响应信息
                        }
                    } else { // 如果字符串数组的长度不是2，表示请求的格式错误
                        out.println("请求格式错误！"); // 向客户端发送响应信息
                    }
                } else if (command.equals("QUERY")) { // 如果请求的命令是查询
                    List<String[]> list = db.queryAllContacts(); // 调用数据库对象的查询所有联系人的方法，并获取返回值
                    if (list.isEmpty()) { // 如果列表为空，表示没有任何联系人记录
                        out.println("没有任何联系人记录！"); // 向客户端发送响应信息
                    } else { // 如果列表不为空，表示有联系人记录
                        StringBuilder sb = new StringBuilder(); // 创建一个字符串构建器，用于拼接响应信息
                        sb.append("所有联系人记录如下：\n"); // 在字符串构建器中添加标题
                        for (String[] contact : list) { // 遍历列表中的每个元素，每个元素是一个字符串数组，表示一个联系人的信息
                            sb.append(contact[0] + " " + contact[1] + " " + contact[2] + "\n"); // 在字符串构建器中添加每个联系人的信息，姓名、电话和邮箱用空格分隔，每个联系人占一行
                        }
                        out.println(sb.toString()); // 将字符串构建器转换为字符串，并向客户端发送响应信息
                    }
                } else { // 如果请求的命令不是以上任何一种，表示请求的命令无效
                    out.println("请求命令无效！"); // 向客户端发送响应信息
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // 打印异常信息
        } finally {
            try {
                socket.close(); // 关闭与客户端通信的套接字
            } catch (IOException e) {
                e.printStackTrace(); // 打印异常信息
            }
        }
    }
}