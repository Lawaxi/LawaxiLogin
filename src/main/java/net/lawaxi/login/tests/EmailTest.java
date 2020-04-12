package net.lawaxi.login.tests;

import net.lawaxi.login.config.AccountConfig;
import net.lawaxi.login.utils.EmailSender;
import net.lawaxi.login.utils.list;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.world.GameMode;

public class EmailTest {
    public static void startTest(ServerPlayerEntity player){
        list.isEmailTest.put(player.getGameProfile(),"false");

        if(list.accountConfig.hasRegisterEmail(player.getEntityName())){
            list.EmailVerifyCode.put(player.getGameProfile(),"1"+ EmailSender.SendEmail(list.accountConfig.getEmail(player.getEntityName()),1,player.getEntityName(),player.getIp()));
            player.sendMessage(new LiteralText(""));
            player.sendMessage(new LiteralText(list.InfoColor+"您登陆了一台新的电脑，我们已经将认证邮件发送到了您的邮箱!"));
        }
        else
        {
            player.sendMessage(new LiteralText(""));
            player.sendMessage(new LiteralText(list.InfoColor+"请输入 /email <邮箱名> <邮箱域名> 为账号绑定一个邮箱，" ));
            player.sendMessage(new LiteralText( list.InfoColor+"他将成为使用新电脑登录，忘记密码试图重置的唯一验证方式!"));
        }
    }


    public static void finishTest(ServerPlayerEntity player){
        if(list.isEmailTest.containsKey(player.getGameProfile()))
            list.isEmailTest.replace(player.getGameProfile(),"true");

        list.isEmailTest.put(player.getGameProfile(),"true");
        player.setGameMode(GameMode.SURVIVAL);
    }
}
