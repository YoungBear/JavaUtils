package com.ysx.utils.gui;

import com.ysx.utils.crypto.aes.AESException;
import com.ysx.utils.crypto.aes.AESPractise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2024-10-28 22:53
 * @blog <a href="https://blog.csdn.net/next_second">...</a>
 * @github <a href="https://github.com/YoungBear">...</a>
 * @description
 */



public class SimpleGUI {

    private static final AESPractise PRACTISE = new AESPractise();
    public static void main(String[] args) {
        // 创建一个新的JFrame容器
        JFrame frame = new JFrame("Simple GUI Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        frame.setLayout(new FlowLayout());
//        frame.setLayout(new GridLayout(4,2));

//        JLabel jLabel1 = new JLabel("secret");
//        JTextField inputField1= new JTextField();
//
//        frame.add(jLabel1);
//        frame.add(inputField1);
//
//        JLabel jLabel2 = new JLabel("plain text");
//        JTextField inputField2= new JTextField();
//
//        frame.add(jLabel2);
//        frame.add(inputField2);


        // 创建一个输入框
        JTextField inputField = new JTextField();

        // 创建一个输出框（不可编辑）
        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);

        // 创建一个按钮
        JButton button = new JButton("encrypt");

        // 添加按钮的点击事件监听器
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取输入框中的内容
                String inputText = inputField.getText();
                try {
                    String encrypted = PRACTISE.encrypt(inputText.getBytes(StandardCharsets.UTF_8));
                    outputArea.setText(encrypted);
                } catch (AESException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // 创建一个输出框（不可编辑）
        JTextArea outputArea2 = new JTextArea();
        outputArea2.setEditable(false);

        // 创建一个按钮
        JButton button2 = new JButton("decrypt");
        // 添加按钮的点击事件监听器
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取输入框中的内容
                String inputText = inputField.getText();
                try {
                    String decrypted = new String(PRACTISE.decrypt(inputText), StandardCharsets.UTF_8);
                    outputArea2.setText(decrypted);
                } catch (AESException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // 将组件添加到框架中
        panel.add(inputField);
        panel.add(new JScrollPane(outputArea)); // 使用滚动面板来包含输出框，以便在内容多时支持滚动
        panel.add(button);
        panel.add(new JScrollPane(outputArea2)); // 使用滚动面板来包含输出框，以便在内容多时支持滚动
        panel.add(button2);
        frame.add(panel);

        // 设置框架可见
        frame.setVisible(true);
    }
}