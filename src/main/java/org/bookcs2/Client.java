package org.bookcs2;

/**
 * @Author MisakiMikoto
 * @Date 2023/10/31
 */
// 客户端代码
import java.io.*;
import java.net.*;
import java.util.*;

// 客户端类，负责与服务器端建立连接，并根据用户的输入发送请求，并接收服务器端的响应
public class Client {
    private Socket socket; // 客户端套接字
    private ObjectInputStream ois; // 输入流对象
    private ObjectOutputStream oos; // 输出流对象

    public Client(String host, int port) throws IOException {
        try {
            socket = new Socket(host, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 启动客户端的方法，循环读取用户的输入，并根据输入发送请求，并接收服务器端的响应
    public void start() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in); // 创建扫描器对象，用于读取用户的输入
        while (true) {
            socket = new Socket("localhost", 12345);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            // 显示菜单选项，并提示用户输入选择
            System.out.println("请选择操作：");
            System.out.println("1. 查看所有联系人");
            System.out.println("2. 添加新联系人");
            System.out.println("3. 删除联系人");
            System.out.println("4. 修改联系人");
            System.out.println("5. 查找联系人");
            System.out.println("6. 退出");
            System.out.print("请输入数字（1-6）：");
            int choice = scanner.nextInt(); // 读取用户输入的数字
            scanner.nextLine(); // 清除换行符

            switch (choice) {
                case 1: // 查看所有联系人选项
                    oos.writeObject("all"); // 向服务器端发送查看所有联系人的请求类型
                    List<Contact> allContacts = (List<Contact>) ois.readObject(); // 从服务器端接收所有联系人的列表
                    if (allContacts.isEmpty()) { // 如果列表为空，表示没有任何联系人
                        System.out.println("通讯录为空");
                    } else { // 如果列表不为空，遍历并显示每个联系人的信息
                        for (Contact c : allContacts) {
                            System.out.println(c);
                        }
                    }
                    break;
                case 2: // 添加新联系人选项
                    oos.writeObject("add"); // 向服务器端发送添加新联系人的请求类型
                    System.out.print("请输入姓名："); // 提示用户输入新联系人的姓名
                    String name = scanner.nextLine(); // 读取用户输入的姓名
                    System.out.print("请输入电话："); // 提示用户输入新联系人的电话
                    String phone = scanner.nextLine(); // 读取用户输入的电话
                    System.out.print("请输入邮箱："); // 提示用户输入新联系人的邮箱
                    String email = scanner.nextLine(); // 读取用户输入的邮箱
                    Contact contact = new Contact(name, phone, email); // 创建新联系人对象
                    oos.writeObject(contact); // 向服务器端发送新联系人对象
                    boolean addResult = (boolean) ois.readObject(); // 从服务器端接收添加结果
                    if (addResult) { // 如果添加结果为true，表示添加成功
                        System.out.println("添加成功");
                    } else { // 如果添加结果为false，表示添加失败，可能是因为已存在同名的联系人
                        System.out.println("添加失败，已存在同名的联系人");
                    }
                    break;
                case 3: // 删除联系人选项
                    oos.writeObject("delete"); // 向服务器端发送删除联系人的请求类型
                    System.out.print("请输入要删除的联系人姓名："); // 提示用户输入要删除的联系人姓名
                    String deleteName = scanner.nextLine(); // 读取用户输入的姓名
                    oos.writeObject(deleteName); // 向服务器端发送要删除的联系人姓名
                    boolean deleteResult = (boolean) ois.readObject(); // 从服务器端接收删除结果
                    if (deleteResult) { // 如果删除结果为true，表示删除成功
                        System.out.println("删除成功");
                    } else { // 如果删除结果为false，表示删除失败，可能是因为不存在该姓名的联系人
                        System.out.println("删除失败，不存在该姓名的联系人");
                    }
                    break;
                case 4: // 修改联系人选项
                    oos.writeObject("update"); // 向服务器端发送修改联系人的请求类型
                    System.out.print("请输入要修改的联系人姓名："); // 提示用户输入要修改的联系人姓名
                    String oldName = scanner.nextLine(); // 读取用户输入的姓名
                    oos.writeObject(oldName); // 向服务器端发送要修改的联系人姓名
                    System.out.print("请输入新的姓名："); // 提示用户输入新的姓名
                    String newName = scanner.nextLine(); // 读取用户输入的姓名
                    System.out.print("请输入新的电话："); // 提示用户输入新的电话
                    String newPhone = scanner.nextLine(); // 读取用户输入的电话
                    System.out.print("请输入新的邮箱："); // 提示用户输入新的邮箱
                    String newEmail = scanner.nextLine(); // 读取用户输入的邮箱
                    Contact newContact = new Contact(newName, newPhone, newEmail); // 创建新联系人对象
                    oos.writeObject(newContact); // 向服务器端发送新联系人对象
                    boolean updateResult = (boolean) ois.readObject(); // 从服务器端接收修改结果
                    if (updateResult) { // 如果修改结果为true，表示修改成功
                        System.out.println("修改成功");
                    } else { // 如果修改结果为false，表示修改失败，可能是因为不存在该姓名的联系人
                        System.out.println("修改失败，不存在该姓名的联系人");
                    }
                    break;
                case 5: // 查找联系人选项
                    oos.writeObject("find"); // 向服务器端发送查找联系人的请求类型
                    System.out.print("请输入要查找的联系人姓名："); // 提示用户输入要查找的联系人姓名
                    String findName = scanner.nextLine(); // 读取用户输入的姓名
                    oos.writeObject(findName); // 向服务器端发送要查找的联系人姓名
                    Contact findResult = (Contact) ois.readObject(); // 从服务器端接收查找结果
                    if (findResult == null) { // 如果查找结果为null，表示查找失败，可能是因为不存在该姓名的联系人
                        System.out.println("查找失败，不存在该姓名的联系人");
                    } else { // 如果查找结果不为null，表示查找成功，显示该联系人的信息
                        System.out.println(findResult);
                    }
                    break;
                case 6: // 退出选项
                    oos.writeObject("exit"); // 向服务器端发送退出请求类型
                    socket.close(); // 关闭套接字连接
                    System.out.println("客户端已断开连接");
                    return; // 结束方法运行
                default: // 其他无效选项
                    System.out.println("无效的选择，请重新输入");
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Client client = new Client("localhost", 12345); // 创建客户端对象，连接到本地主机的8888端口
        client.start(); // 启动客户端
    }
}
