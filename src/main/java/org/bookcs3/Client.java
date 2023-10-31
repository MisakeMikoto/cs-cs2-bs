package org.bookcs3;

/**
 * @Author MisakiMikoto
 * @Date 2023/10/31
 */
// 个人通讯录系统的代码
// 三层C/S结构：客户端、服务器、数据库
// 客户端：负责与用户交互，显示界面，发送请求，接收响应
// 服务器：负责处理客户端的请求，调用数据库操作，返回结果
// 数据库：负责存储联系人的信息，提供增删改查的功能

// 客户端类
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

// 客户端类
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client extends JFrame implements ActionListener {
    // 定义客户端的界面组件
    private JTextField nameField; // 输入联系人姓名的文本框
    private JTextField phoneField; // 输入联系人电话的文本框
    private JTextField emailField; // 输入联系人邮箱的文本框
    private JButton addButton; // 添加新联系人的按钮
    private JButton modifyButton; // 修改联系人信息的按钮
    private JButton deleteButton; // 删除联系人的按钮
    private JButton queryButton; // 查看联系人信息的按钮
    private JTextArea resultArea; // 显示结果的文本区域

    // 定义客户端与服务器通信的套接字和输入输出流
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    // 定义客户端的构造方法，初始化界面和通信
    public Client() {
        super("个人通讯录系统"); // 设置窗口标题
        setSize(400, 300); // 设置窗口大小
        setLocation(200, 200); // 设置窗口位置
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口关闭时退出程序

        // 创建界面组件并添加到窗口中
        JPanel inputPanel = new JPanel(); // 创建输入面板
        inputPanel.setLayout(new GridLayout(3, 2)); // 设置输入面板为网格布局，3行2列
        inputPanel.add(new JLabel("姓名：")); // 添加姓名标签
        nameField = new JTextField(10); // 创建姓名文本框，长度为10个字符
        inputPanel.add(nameField); // 添加姓名文本框到输入面板中
        inputPanel.add(new JLabel("电话：")); // 添加电话标签
        phoneField = new JTextField(10); // 创建电话文本框，长度为10个字符
        inputPanel.add(phoneField); // 添加电话文本框到输入面板中
        inputPanel.add(new JLabel("邮箱：")); // 添加邮箱标签
        emailField = new JTextField(10); // 创建邮箱文本框，长度为10个字符
        inputPanel.add(emailField); // 添加邮箱文本框到输入面板中

        JPanel buttonPanel = new JPanel(); // 创建按钮面板
        buttonPanel.setLayout(new FlowLayout()); // 设置按钮面板为流式布局
        addButton = new JButton("添加"); // 创建添加按钮
        addButton.addActionListener(this); // 给添加按钮添加动作监听器，实现ActionListener接口的actionPerformed方法
        buttonPanel.add(addButton); // 添加添加按钮到按钮面板中
        modifyButton = new JButton("修改"); // 创建修改按钮
        modifyButton.addActionListener(this); // 给修改按钮添加动作监听器，实现ActionListener接口的actionPerformed方法
        buttonPanel.add(modifyButton); // 添加修改按钮到按钮面板中
        deleteButton = new JButton("删除"); // 创建删除按钮
        deleteButton.addActionListener(this); // 给删除按钮添加动作监听器，实现ActionListener接口的actionPerformed方法
        buttonPanel.add(deleteButton); // 添加删除按钮到按钮面板中
        queryButton = new JButton("查询"); // 创建查询按钮
        queryButton.addActionListener(this); // 给查询按钮添加动作监听器，实现ActionListener接口的actionPerformed方法
        buttonPanel.add(queryButton); // 添加查询按钮到按钮面板中

        resultArea = new JTextArea(); // 创建结果文本区域
        resultArea.setEditable(false); // 设置结果文本区域不可编辑
        JScrollPane scrollPane = new JScrollPane(resultArea); // 创建滚动面板，并将结果文本区域添加到滚动面板中

        // 将输入面板、按钮面板和滚动面板添加到窗口中
        add(inputPanel, BorderLayout.NORTH); // 将输入面板添加到窗口的北边
        add(buttonPanel, BorderLayout.CENTER); // 将按钮面板添加到窗口的中间
        add(scrollPane, BorderLayout.SOUTH); // 将滚动面板添加到窗口的南边

        setVisible(true); // 设置窗口可见

        // 初始化通信
        try {
            socket = new Socket("localhost", 8888); // 创建套接字，连接到服务器的本地地址和8888端口
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 创建输入流，从套接字中获取服务器发来的数据
            out = new PrintWriter(socket.getOutputStream(), true); // 创建输出流，向套接字中发送数据给服务器，自动刷新缓冲区
        } catch (IOException e) {
            e.printStackTrace(); // 打印异常信息
        }
    }

    // 实现ActionListener接口的actionPerformed方法，处理按钮的点击事件
    public void actionPerformed(ActionEvent e) {
        String name = nameField.getText().trim(); // 获取姓名文本框中的内容，并去除两端的空格
        String phone = phoneField.getText().trim(); // 获取电话文本框中的内容，并去除两端的空格
        String email = emailField.getText().trim(); // 获取邮箱文本框中的内容，并去除两端的空格
        String command = e.getActionCommand(); // 获取触发事件的按钮的文本
        if (command.equals("添加")) { // 如果是添加按钮
            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) { // 如果姓名、电话或邮箱为空
                resultArea.setText("请输入完整的联系人信息！"); // 在结果文本区域显示提示信息
            } else { // 如果姓名、电话和邮箱都不为空
                out.println("ADD " + name + " " + phone + " " + email); // 向服务器发送添加请求，格式为"ADD 姓名 电话 邮箱"
                try {
                    String response = in.readLine(); // 从服务器读取响应信息
                    resultArea.setText(response); // 在结果文本区域显示响应信息
                } catch (IOException ex) {
                    ex.printStackTrace(); // 打印异常信息
                }
            }
        } else if (command.equals("修改")) { // 如果是修改按钮
            if (name.isEmpty()) { // 如果姓名为空
                resultArea.setText("请输入要修改的联系人姓名！"); // 在结果文本区域显示提示信息
            } else { // 如果姓名不为空
                out.println("MODIFY " + name + " " + phone + " " + email); // 向服务器发送修改请求，格式为"MODIFY 姓名 电话 邮箱"
                try {
                    String response = in.readLine(); // 从服务器读取响应信息
                    resultArea.setText(response); // 在结果文本区域显示响应信息
                } catch (IOException ex) {
                    ex.printStackTrace(); // 打印异常信息
                }
            }
        } else if (command.equals("删除")) { // 如果是删除按钮
            if (name.isEmpty()) { // 如果姓名为空
                resultArea.setText("请输入要删除的联系人姓名！"); // 在结果文本区域显示提示信息
            } else { // 如果姓名不为空
                out.println("DELETE " + name); // 向服务器发送删除请求，格式为"DELETE 姓名"
                try {
                    String response = in.readLine(); // 从服务器读取响应信息
                    resultArea.setText(response); // 在结果文本区域显示响应信息
                } catch (IOException ex) {
                    ex.printStackTrace(); // 打印异常信息
                }
            }
        } else if (command.equals("查询")) { // 如果是查询按钮
            out.println("QUERY"); // 向服务器发送查询请求，格式为"QUERY"
            try {
                String response = in.readLine(); // 从服务器读取响应信息
                resultArea.setText(response); // 在结果文本区域显示响应信息
            } catch (IOException ex) {
                ex.printStackTrace(); // 打印异常信息
            }
        }
    }
}
