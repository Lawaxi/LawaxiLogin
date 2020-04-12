package net.lawaxi.login.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.lawaxi.login.utils.list;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AdminConfig {

    private static Gson gson = new GsonBuilder().create();
    private static JsonObject jsonObject;

    public static String username;
    public static String password;
    public static String protocol;
    public static String host;
    public static String auth;
    public static String port;
    public static String sfp;
    public static String sfc;

    public static ArrayList<String> subject=new ArrayList<>();
    public static ArrayList<String>  content = new ArrayList<>();

    private static void testConfigExist(){
        if(!list.fatherfolder.exists())
            list.fatherfolder.mkdir();

        if(!list.folder.exists())
            list.folder.mkdir();

        if(!list.config_admin.exists())
        {
            try{
                list.config_admin.createNewFile();

                //初始化
                FileWriter fw = new FileWriter(list.config_admin);
                JsonObject initialize = new JsonObject();
                JsonObject initialize1 = new JsonObject();
                initialize1.addProperty("username","example@qq.com");
                initialize1.addProperty("password","abcdefghijklmnop");
                initialize1.addProperty("mail.transport.protocol","smtp");
                initialize1.addProperty("mail.smtp.host","smtp.qq.com");
                initialize1.addProperty("mail.smtp.auth","true");
                initialize1.addProperty("mail.smtp.port","465");
                initialize1.addProperty("mail.smtp.socketFactory.port","465");
                initialize1.addProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
                initialize.add("sender",initialize1);

                System.out.print(list.SystemOutPrefix+"请在配置文件中完成对服务器邮箱账号的填写，如果您使用qq邮箱，则只需填写username和password");
                System.out.print(list.SystemOutPrefix+"password项需要在邮箱中开启“POP3/SMTP”服务并填写邮箱提供的KEY 而不是您的QQ密码!");

                JsonObject initialize2 = new JsonObject();
                initialize2.addProperty("subject1","%player%，欢迎注册御花园！");
                initialize2.addProperty("content1","%player%，您好\n\n欢迎光临御花园服务器！\n\n为了完成验证，请您在服务器中输入：\n/verify %code%");
                initialize2.addProperty("subject2","%player%的御花园账号：一台新电脑请求验证");
                initialize2.addProperty("content2","%player%，您好\n\n地址为 %ip% 的玩家在御花园服务器上试图登陆您的账号，并且已经输入了正确的密码.\n\n如果这是您的操作，为了完成验证，请在服务器中输入：\n/verify %code%\n\n如果这并非您的操作，请即使更换密码!");
                initialize2.addProperty("subject3","%player%的御花园账号：重置密码");
                initialize2.addProperty("content3","%player%，您好\n\n地址为 %ip% 的玩家在御花园服务器上试图重置密码！\n\n如果这是由于您的密码丢失所引起，请在服务器中输入：\n/verify %code%\n\n系统会自动生成一个随机的密码给您，您可以自行更改.");
                initialize2.addProperty("subject4","%player%的御花园账号：账号变更通知");
                initialize2.addProperty("content4","%player%，您好\n\n操作者 %ip% 在服务器上更改了您的密码！\n\n现在您的密码是：%code%");
                initialize.add("reciever",initialize2);

                fw.write(initialize.toString());
                fw.close();

            }
            catch (IOException e)
            {
                System.out.print(list.SystemOutPrefix+"创建服务器邮箱配置时出现错误");
            }
        }
    }

    public AdminConfig(){

        testConfigExist();

        try {
            FileReader fr = new FileReader(list.config_admin);
            jsonObject =gson.fromJson(fr,JsonObject.class);
            fr.close();

            this.username = jsonObject.get("sender").getAsJsonObject().get("username").getAsString();
            this.password=jsonObject.get("sender").getAsJsonObject().get("password").getAsString();
            this.protocol = jsonObject.get("sender").getAsJsonObject().get("mail.transport.protocol").getAsString();
            this.host=jsonObject.get("sender").getAsJsonObject().get("mail.smtp.host").getAsString();
            this.port = jsonObject.get("sender").getAsJsonObject().get("mail.smtp.port").getAsString();
            this.auth=jsonObject.get("sender").getAsJsonObject().get("mail.smtp.auth").getAsString();
            this.sfp=jsonObject.get("sender").getAsJsonObject().get("mail.smtp.socketFactory.port").getAsString();
            this.sfc = jsonObject.get("sender").getAsJsonObject().get("mail.smtp.socketFactory.class").getAsString();

            this.subject.add(jsonObject.get("reciever").getAsJsonObject().get("subject1").getAsString());
            this.subject.add(jsonObject.get("reciever").getAsJsonObject().get("subject2").getAsString());
            this.subject.add(jsonObject.get("reciever").getAsJsonObject().get("subject3").getAsString());
            this.subject.add(jsonObject.get("reciever").getAsJsonObject().get("subject4").getAsString());
            this.content.add(jsonObject.get("reciever").getAsJsonObject().get("content1").getAsString());
            this.content.add(jsonObject.get("reciever").getAsJsonObject().get("content2").getAsString());
            this.content.add(jsonObject.get("reciever").getAsJsonObject().get("content3").getAsString());
            this.content.add(jsonObject.get("reciever").getAsJsonObject().get("content4").getAsString());
        }
        catch (Exception e)
        {
            System.out.print(list.SystemOutPrefix+"加载服务器邮箱配置时出现错误");
        }

    }

}
