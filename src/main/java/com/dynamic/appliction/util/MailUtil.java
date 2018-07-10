package com.dynamic.appliction.util;

import java.io.BufferedReader;
import java.io.LineNumberReader;
import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileReader;
import java.io.FileWriter;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
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

        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            //获取模板html文档
            String fileName = MailUtil.class.getClassLoader().getResource("static/mailTemplate.html").getPath();
            System.out.println(fileName);
            document = reader.read(fileName);
            Element root = document.getRootElement();
            //分别获取id为name、message、time的节点。
            Element name = getNodes(root, "id", "userName");
            Element content = getNodes(root, "id", "content");
            Element time = getNodes(root, "id", "time");
            //设置收件人姓名，通知信息、当前时间
            time.addText(TimeUtil.getDate());
            //name.setText("小明");
            content.addAttribute("href", mailBox.getContent());
            //保存到临时文件
            FileWriter fwriter = new FileWriter("d:/temp.html");
            XMLWriter writer = new XMLWriter(fwriter);
            writer.write(document);
            writer.flush();
            //读取临时文件，并把html数据写入到字符串str中，通过邮箱工具发送
            FileReader in = new FileReader("d:/temp.html");
            StringBuffer sb = new StringBuffer();
            BufferedReader bufReader = new BufferedReader(in);
            LineNumberReader reader1 = new LineNumberReader(bufReader);
            String line;
            while ((line = reader1.readLine()) != null) {
                sb.append(line).append(System.getProperty("line.separator"));
            }
            reader1.close();
            String str = sb.toString();
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
                message.setContent(str, "text/html;charset=UTF-8");
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

    /**
     * 方法描述：递归遍历子节点，根据属性名和属性值，找到对应属性名和属性值的那个子孙节点。
     *
     * @param node      要进行子节点遍历的节点
     * @param attrName  属性名
     * @param attrValue 属性值
     * @return 返回对应的节点或null
     */
    public static Element getNodes(Element node, String attrName, String attrValue) {

        final List<Attribute> listAttr = node.attributes();// 当前节点的所有属性
        for (final Attribute attr : listAttr) {// 遍历当前节点的所有属性
            final String name = attr.getName();// 属性名称
            final String value = attr.getValue();// 属性的值
            System.out.println("属性名称：" + name + "---->属性值：" + value);
            if (attrName.equals(name) && attrValue.equals(value)) {
                return node;
            }
        }
        // 递归遍历当前节点所有的子节点
        final List<Element> listElement = node.elements();// 所有一级子节点的list
        for (Element e : listElement) {// 遍历所有一级子节点
            Element temp = getNodes(e, attrName, attrValue);
            // 递归
            if (temp != null) {
                return temp;
            }
            ;
        }

        return null;
    }


}
