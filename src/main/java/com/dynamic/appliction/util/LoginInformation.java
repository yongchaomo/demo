package com.dynamic.appliction.util;

import com.dynamic.appliction.pojo.bean.MailBox;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Component
public class LoginInformation {
    @Autowired
    RedisUtils redisUtils;

    @Value("${InitializationConfig.fileUrl}")
    String fileUrl;
    @Value("${InitializationConfig.http}")
    String http;

    //获取IP地址
    public String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
            // = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    //获取邮箱
    public String getEmain(HttpServletRequest request) throws Exception {
        String _session_id = CookiesUtil.getCookie(request, "_session_id");
        return redisUtils.get(_session_id);
    }

    //发送验证码
    public void getEmailSend(String email, String token, String readAddress, String method, String WriteAddress, String title, int type) throws Exception {
        SAXReader reader = new SAXReader();
        //获取模板html文档
        ClassPathResource cpr = new ClassPathResource(readAddress);
//        String url = request.getServletContext().getRealPath("/static/upload");
        InputStream inputStream = cpr.getInputStream();
        Document document = reader.read(inputStream);
        Element root = document.getRootElement();
        //分别获取id为name、message、time的节点。
        Element name = getNodes(root, "id", "userName");
        Element content = getNodes(root, "id", "content");
        Element time = getNodes(root, "id", "time");
        //设置收件人姓名，通知信息、当前时间
        time.addText(TimeUtil.getDate());
        //name.setText("小明");
        if (type == 2) {
            content.addText(token);
        } else {
            content.addAttribute("href", http + method + token);
        }
        //保存到临时文件
//        ClassPathResource classPathResource = new ClassPathResource("static/send/activationMail.html");
        File file = new File(fileUrl + WriteAddress);
        FileWriter fw = new FileWriter(file);
        FileWriter fwriter = new FileWriter(file);
        XMLWriter writer = new XMLWriter(fwriter);
        writer.write(document);
        writer.flush();
        //读取临时文件，并把html数据写入到字符串str中，通过邮箱工具发送
        FileReader in = new FileReader(file);
        StringBuffer sb = new StringBuffer();
        BufferedReader bufReader = new BufferedReader(in);
        LineNumberReader reader1 = new LineNumberReader(bufReader);
        String line;
        while ((line = reader1.readLine()) != null) {
            sb.append(line).append(System.getProperty("line.separator"));
        }
        reader1.close();
        MailBox mailBox = new MailBox();
        mailBox.setTitle(title);
        mailBox.setContent(sb.toString());
        new Thread(new MailUtil(email, mailBox)).start();
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
        }
        return null;
    }
}
