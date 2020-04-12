package net.lawaxi.login.utils;

import com.mojang.authlib.GameProfile;
import net.lawaxi.login.config.AccountConfig;
import net.lawaxi.login.config.AdminConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class list {

    public static File fatherfolder = new File("Lawaxi");
    public static File folder = new File("Lawaxi"+File.separator+"account");
    public static File config_player = new File("Lawaxi"+File.separator+"account"+File.separator+"players.json");
    public static File config_admin = new File("Lawaxi"+File.separator+"account"+File.separator+"admin-email.json");

    public static final String SuccessColor="§a";
    public static final String FailedColor="§c";
    public static final String InfoColor="§6";

    public static final String SystemOutPrefix="[Login] ";

    public static Map<GameProfile,String> isLogin = new HashMap<>();
    public static Map<GameProfile,String> isEmailTest = new HashMap<>();
    public static Map<GameProfile, String> EmailVerifyCode = new HashMap<>();

    //EmailVerifyCode格式：
    //index 0：   0/1   模式：0代表注册 1代表验证
    //index 1-6： *     验证码：邮件发送的随机验证码
    //index 7-*： *     邮箱地址：只在模式为0时启用


    public static AccountConfig accountConfig = new AccountConfig();

}
