package com.fh.shop.api.util;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class Email {

    /**
     * 邮箱发送邮件
     */
    public static void sendEmail(String Email,String title,String content) throws GeneralSecurityException {
        // 收件人电子邮箱
        String to = Email;

        // 发件人电子邮箱
        String from = "1670415267@qq.com";

        // 指定发送邮件的主机为 smtp.qq.com
        String host = SystemConst.EMAIL_SMTP;  //QQ 邮件服务器

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("1670415267@qq.com", "qcgtwcneupqofabi");
            }
        });

        try{
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            //message.setContent();
            // Set Subject: 头部头字段
            message.setSubject(title);

            // 设置消息体
            // message.setContent("<h1>欢迎</h1>","true");
            message.setDataHandler(new DataHandler( new ByteArrayDataSource("<h1 style=\"color: #761c19\"><b>"+content+"</b></h1>", "text/html")));

            // 发送消息
            Transport.send(message);
            System.out.println("Sent message successfully....from runoob.com");
        }catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 带图片邮件
     */
    public static void sendPhotoEmail(){
        Properties prop = new Properties();
        prop.setProperty("mail.host", SystemConst.EMAIL_SMTP);
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.auth", "true");
        //使用JavaMail发送邮件的5个步骤
        //1、创建session
        Session session = Session.getInstance(prop);
        //2、通过session得到transport对象
        Transport ts = null;
        try {
            ts = session.getTransport();
            //3、连上邮件服务器，需要发件人提供邮箱的用户名和密码进行验证
            ts.connect(SystemConst.EMAIL_SMTP, SystemConst.EMAIL_USER, SystemConst.EMAIL_PASSWORD);
            //4、创建邮件
            Message message = createImageMail(session);
            //5、发送邮件
            ts.sendMessage(message, message.getAllRecipients());
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ts!=null){
                try {
                    ts.close();
                    ts=null;
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 生成一封邮件正文带图片的邮件
     */
    public static MimeMessage createImageMail(Session session) throws MessagingException, IOException {
        //创建邮件
        MimeMessage message = new MimeMessage(session);
        // 设置邮件的基本信息
        //发件人
        message.setFrom(new InternetAddress(SystemConst.EMAIL_USER));
        //收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("1652890145@qq.com"));
        //邮件标题
        message.setSubject("带图片的邮件");
        // 准备邮件数据
        // 准备邮件正文数据
        MimeBodyPart text = new MimeBodyPart();
        text.setContent("这是一封邮件正文带图片<img src='cid:xxx.jpg'>的邮件", "text/html;charset=UTF-8");
        // 准备图片数据
        MimeBodyPart image = new MimeBodyPart();
        DataHandler dh = new DataHandler(new FileDataSource("src\\js/html/images/bg.jpg"));
        image.setDataHandler(dh);
        image.setContentID("xxx.jpg");

        // 描述数据关系
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(text);
        mm.addBodyPart(image);
        mm.setSubType("related");
        message.setContent(mm);
        message.saveChanges();
        //将创建好的邮件写入到E盘以文件的形式进行保存
        message.writeTo(new FileOutputStream("E:\\ImageMail.eml"));
        //返回创建好的邮件
        return message;

    }

}
