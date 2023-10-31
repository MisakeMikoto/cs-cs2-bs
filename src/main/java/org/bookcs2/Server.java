package org.bookcs2;

/**
 * @Author MisakiMikoto
 * @Date 2023/10/31
 */
// 服务器端代码
import java.io.*;
import java.net.*;
import java.util.*;

// 服务器类，负责接收客户端的请求，并根据请求调用通讯录类的相应方法，然后将结果发送回客户端
public class Server {
    private ServerSocket serverSocket; // 服务器套接字
    private AddressBook addressBook; // 通讯录对象

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port); // 创建服务器套接字，并绑定指定端口
        addressBook = new AddressBook(); // 创建通讯录对象
        System.out.println("服务器启动成功，等待客户端连接...");
    }

    // 启动服务器的方法，循环接收客户端的连接，并为每个连接创建一个线程处理
    public void start() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept(); // 接收客户端的连接
            System.out.println("客户端" + socket.getInetAddress() + "已连接");
            new Thread(new Handler(socket)).start(); // 为该连接创建一个线程处理
        }
    }

    // 处理客户端请求的线程类，实现了Runnable接口
    class Handler implements Runnable {
        private Socket socket; // 客户端套接字

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // 创建输入输出流对象
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                while (true) {
                    // 读取客户端发送的请求类型，根据请求类型调用通讯录类的相应方法，并将结果发送回客户端
                    String requestType = (String) ois.readObject();
                    switch (requestType) {
                        case "add": // 添加联系人请求
                            Contact contact = (Contact) ois.readObject(); // 读取客户端发送的联系人对象
                            boolean addResult = addressBook.addContact(contact); // 调用通讯录类的添加方法
                            oos.writeObject(addResult); // 将添加结果发送回客户端
                            break;
                        case "delete": // 删除联系人请求
                            String name = (String) ois.readObject(); // 读取客户端发送的联系人姓名
                            boolean deleteResult = addressBook.deleteContact(name); // 调用通讯录类的删除方法
                            oos.writeObject(deleteResult); // 将删除结果发送回客户端
                            break;
                        case "update": // 修改联系人请求
                            String oldName = (String) ois.readObject(); // 读取客户端发送的原联系人姓名
                            Contact newContact = (Contact) ois.readObject(); // 读取客户端发送的新联系人对象
                            boolean updateResult = addressBook.updateContact(oldName, newContact); // 调用通讯录类的修改方法
                            oos.writeObject(updateResult); // 将修改结果发送回客户端
                            break;
                        case "find": // 查找联系人请求
                            String findName = (String) ois.readObject(); // 读取客户端发送的联系人姓名
                            Contact findResult = addressBook.findContact(findName); // 调用通讯录类的查找方法
                            oos.writeObject(findResult); // 将查找结果发送回客户端
                            break;
                        case "all": // 查看所有联系人请求
                            List<Contact> allResult = addressBook.getAllContacts(); // 调用通讯录类的获取所有联系人方法
                            oos.writeObject(allResult); // 将所有联系人列表发送回客户端
                            break;
                        case "exit": // 退出请求
                            System.out.println("客户端" + socket.getInetAddress() + "已断开连接");
                            socket.close(); // 关闭套接字连接
                            return; // 结束线程运行
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(12345);
        server.start(); // 启动服务器
    }
}
