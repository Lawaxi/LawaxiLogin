package net.lawaxi.login.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.lawaxi.login.config.AccountConfig;
import net.lawaxi.login.tests.EmailTest;
import net.lawaxi.login.tests.PasswordTest;
import net.lawaxi.login.utils.EmailSender;
import net.lawaxi.login.utils.list;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class verify {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("verify")
                .then(CommandManager.argument("验证码", StringArgumentType.string())
                        .executes(ctx -> {
                            ServerPlayerEntity player = ctx.getSource().getPlayer();
                            if(list.EmailVerifyCode.containsKey(player.getGameProfile()))
                            {
                                String code= StringArgumentType.getString(ctx,"验证码");
                                String verifycode = list.EmailVerifyCode.get(player.getGameProfile());
                                if(code.equals(verifycode.substring(1,7)))
                                {
                                    if(verifycode.substring(0,1).equals("0"))//注册邮箱
                                    {
                                        list.accountConfig.setEmail(player.getEntityName(),verifycode.substring(7));
                                        list.accountConfig.newIP(player.getEntityName(),player.getIp());
                                        EmailTest.finishTest(player);
                                    }
                                    else if(verifycode.substring(0,1).equals("1"))//新ip
                                    {
                                        list.accountConfig.newIP(player.getEntityName(),player.getIp());
                                        EmailTest.finishTest(player);
                                    }
                                    else if(verifycode.substring(0,1).equals("2"))//更换密码
                                    {

                                        String newpassword = EmailSender.getRandomCode(8);

                                        list.accountConfig.setPassword(player.getEntityName(),newpassword);

                                        EmailSender.SendEmail(list.accountConfig.getEmail(player.getEntityName()),
                                                newpassword, 3, player.getEntityName(), player.getIp());

                                        player.sendMessage(new LiteralText(list.SuccessColor+"您的新密码："+list.InfoColor+newpassword));
                                        player.sendMessage(new LiteralText(list.SuccessColor+"您可以通过：/changepassword "+newpassword+" <新密码> 更改"));

                                        PasswordTest.finishTest(player);
                                        EmailTest.finishTest(player);
                                    }
                                    else
                                    {
                                        player.sendMessage(new LiteralText(list.FailedColor+"插件功能有误！"));
                                    }


                                }
                                else
                                {
                                    player.sendMessage(new LiteralText(list.FailedColor+"验证码输入有误!"));
                                }
                            }
                            else
                            {
                                player.sendMessage(new LiteralText(list.FailedColor+"您没有待完成的邮箱验证"));
                            }
                            return 1;
                        }))
                .executes(ctx -> {
                    ctx.getSource().getPlayer().sendMessage(new LiteralText(list.FailedColor+"请输入邮箱"));
                    return 1;
                })
        );
    }
}
