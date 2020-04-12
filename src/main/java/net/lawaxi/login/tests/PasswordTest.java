package net.lawaxi.login.tests;

import net.lawaxi.login.config.AccountConfig;
import net.lawaxi.login.utils.list;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class PasswordTest {

    public static void startTest(ServerPlayerEntity player){
        if(list.accountConfig.hasRegister(player.getEntityName())){
            player.sendMessage(new LiteralText(list.InfoColor+"请输入/login <密码> 登录"));
        }
        else
        {
            player.sendMessage(new LiteralText(list.InfoColor+"请输入/register <密码> <确认密码> 注册"));
        }
    }


    public static void finishTest(ServerPlayerEntity player){

        list.isLogin.replace(player.getGameProfile(),"true");
        IPTest(player);

    }

    public static void IPTest(ServerPlayerEntity player)
    {
        if(!list.accountConfig.hasIP(player.getEntityName(),player.getIp()))
        {
            EmailTest.startTest(player);
        }
        else
        {
            EmailTest.finishTest(player);
        }
    }

}
