package net.lawaxi.login.utils;

import net.lawaxi.login.config.AdminConfig;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.system.CallbackI;

import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    public static Random random = new Random();
    private static InternetAddress from;
    private static Session session;

    public EmailSender(){

        String username = AdminConfig.username;
        String password = AdminConfig.password;
        System.out.print(username);
        try {
            from = new InternetAddress(username);
        } catch (AddressException e) {
            System.out.print(list.SystemOutPrefix+"邮箱连接错误");
        }
        Properties props = new Properties();
        props.put("mail.transport.protocol", AdminConfig.protocol);
        props.put("mail.smtp.host", AdminConfig.host);
        props.put("mail.smtp.socketFactory.port", AdminConfig.sfp);
        props.put("mail.smtp.socketFactory.class", AdminConfig.sfc);
        props.put("mail.smtp.auth", AdminConfig.auth);
        props.put("mail.smtp.port", AdminConfig.port);

        session = Session.getInstance(props, new javax.mail.Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

    }

    public static String getRandomCode(int length)
    {
        String code="";
        for(int i=0;i<length;i++)
        {
            if(MathHelper.nextInt(random,0,1)==0)
            {
                //数字
                code+=String.valueOf(MathHelper.nextInt(random,0,9));
            }
            else
            {
                //字母
                if(MathHelper.nextInt(random,0,1)==0) {

                    //大写字母 ASCII码
                    code += String.valueOf((char)MathHelper.nextInt(random, 65, 90));
                }
                else
                {
                    //小写字母
                    code += String.valueOf((char)MathHelper.nextInt(random, 97, 122));
                }
            }
        }
        return code;
    }

    public static String SendEmail(String address,int mode,String Playername,String IP) {

        //返回值：成功返回验证码；服务器邮箱连接错误返回shit1，邮件发送失败返回shit2

        //mode：注册邮箱为0，验证邮箱为1，重置密码为2

        String code=getRandomCode(6);
        return SendEmail(address,code,mode,Playername,IP);

    }

    public static String SendEmail(String address,String code,int mode,String Playername,String IP) {

        switch (SendEmail(address,
                AdminConfig.subject.get(mode).replace("%player%",Playername).replace("%ip%",IP).replace("%code%",code),
                AdminConfig.content.get(mode).replace("%player%",Playername).replace("%ip%",IP).replace("%code%",code)))
        {
            case 2:
                return code;
            case 1:
                return "shit2";
            default:
                return "shit1";
        }

    }

    public static int SendEmail(String address,String Subject,String Text)
    {
        //邮箱连接错误返回0   邮件发送失败返回1    成功返回2

        InternetAddress TO;
        try {
            TO = new InternetAddress(address);
        } catch (AddressException e) {
            System.out.print(list.SystemOutPrefix+"");
            return 0;
        }


        try{
            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, TO);
            message.setSubject(Subject);
            message.setText(Text);
            message.setFrom(from);
            Transport.send(message);

            return 2;
        }
        catch (MessagingException e)
        {
            return 1;
        }
    }
}
