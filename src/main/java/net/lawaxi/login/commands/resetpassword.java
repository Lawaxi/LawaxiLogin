package net.lawaxi.login.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.lawaxi.login.tests.PasswordTest;
import net.lawaxi.login.utils.EmailSender;
import net.lawaxi.login.utils.list;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class resetpassword {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("resetpassword")
                .executes(ctx -> {
                    ServerPlayerEntity player = ctx.getSource().getPlayer();
                    if(list.accountConfig.hasRegister(player.getEntityName()))
                    {
                        if(list.accountConfig.hasRegisterEmail(player.getEntityName()))
                        {
                            String email= list.accountConfig.getEmail(player.getEntityName());
                            String return1 = EmailSender.SendEmail(email,2,player.getEntityName(),player.getIp());
                            if(return1.equals("shit1"))
                                player.sendMessage(new LiteralText(list.FailedColor+"本服务器邮箱验证服务配置有误，请联系管理员!"));
                            else if(return1.equals("shit2"))
                                player.sendMessage(new LiteralText(list.FailedColor+"验证邮件发送失败，请重试"));
                            else {
                                player.sendMessage(new LiteralText(list.SuccessColor + "我们已经将密码重置的验证邮件发送至您的邮箱"));
                                list.EmailVerifyCode.put(player.getGameProfile(), "2" + return1);
                            }
                        }
                        else
                        {
                            player.sendMessage(new LiteralText(list.InfoColor+"您需要先通过 /email <邮箱名> <邮箱域名> 绑定邮箱"));
                        }
                    }
                    else
                    {
                        player.sendMessage(new LiteralText(list.InfoColor+"您尚未注册，可以直接使用 /register <密码> <确认密码> 注册"));
                    }
                    return 1;
                })
        );
    }
}
