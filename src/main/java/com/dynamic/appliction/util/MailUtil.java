package com.dynamic.appliction.util;

import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.dynamic.appliction.pojo.bean.MailBox;
import com.sun.mail.util.MailSSLSocketFactory;

/**
 * @program: demo
 * @description:邮箱
 * @author: Mr.MO
 * @create: 2018-06-21 17:04
 **/
public class MailUtil implements Runnable {
    private List<String> emails;// 收件人邮箱
    private String email;// 收件人邮箱
    private MailBox mailBox;// 发送信息

    public MailUtil(List<String> emails, MailBox mailBox) {
        this.emails = emails;
        this.mailBox = mailBox;
    }

    public MailUtil(String email, MailBox mailBox) {
        this.email = email;
        this.mailBox = mailBox;
    }

    @Override
    public void run() {
        // 1.创建连接对象javax.mail.Session
        // 2.创建邮件对象 javax.mail.Message
        // 3.发送一封激活邮件
        // String from = "985319193@qq.com";// 发件人电子邮箱
        // String host = "smtp.qq.com"; // 指定发送邮件的主机smtp.qq.com(QQ)|smtp.163.com(网易)
        String from = "yongchaomo@163.com";// 发件人电子邮箱
        String host = "smtp.163.com"; // 指定发送邮件的主机smtp.qq.com(QQ)|smtp.163.com(网易)
        Properties properties = System.getProperties();// 获取系统属性
        properties.setProperty("mail.smtp.host", host);// 设置邮件服务器
        properties.setProperty("mail.smtp.auth", "true");// 打开认证
        try {
            // QQ邮箱需要下面这段代码，163邮箱不需要
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);
            // 1.获取默认session对象
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("yongchaomo@163.com", "love0910"); // 发件人邮箱账号、授权码
                }
            });

            if (email != null) {
                // 2.创建邮件对象
                Message message = new MimeMessage(session);
                // 2.1设置发件人
                message.setFrom(new InternetAddress(from));
                // 2.2设置接收人
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                // 2.3设置邮件主题
                message.setSubject(mailBox.getTitle());
                // 2.4设置邮件内容
//                "<html><head></head><body>" + mailBox.getContent() + "</body></html>"
                message.setContent(mailBox.getContent(), "text/html;charset=UTF-8");
                // 3.发送邮件
                Transport.send(message);
            } else {
                for (int i = 0; i < emails.size(); i++) {
                    // 2.创建邮件对象
                    Message message = new MimeMessage(session);
                    // 2.1设置发件人
                    message.setFrom(new InternetAddress(from));
                    // 2.2设置接收人
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(emails.get(i)));
                    // 2.3设置邮件主题
                    message.setSubject(mailBox.getTitle());
                    // 2.4设置邮件内容
                    message.setContent("<html><head></head><body>" + mailBox.getContent() + "</body></html>", "text/html;charset=UTF-8");
                    // 3.发送邮件
                    Transport.send(message);
                }
            }
            System.out.println("邮件成功发送!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
