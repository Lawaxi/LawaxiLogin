package net.lawaxi.login.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.lawaxi.login.config.AccountConfig;
import net.lawaxi.login.utils.list;
import net.lawaxi.login.tests.PasswordTest;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class login {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("login")
                .redirect(dispatcher.register(CommandManager.literal("l")
                        .then(CommandManager.argument("密码", StringArgumentType.string())
                                .executes(ctx -> {
                                    ServerPlayerEntity player = ctx.getSource().getPlayer();
                                    if(!list.isLogin.get(player.getGameProfile()).equals("true"))
                                    {
                                        if(list.accountConfig.hasRegister(player.getEntityName()))
                                        {
                                            String password= StringArgumentType.getString(ctx,"密码");
                                            if(list.accountConfig.getPassword(player.getEntityName()).equals(password))
                                            {
                                                player.sendMessage(new LiteralText(list.SuccessColor+"登陆成功!"));
                                                PasswordTest.finishTest(player);
                                            }
                                            else
                                            {
                                                player.sendMessage(new LiteralText(list.FailedColor+"密码错误，您可以通过 /resetpassword 来重置密码!"));
                                            }
                                        }
                                        else
                                        {
                                            player.sendMessage(new LiteralText(list.FailedColor+"您尚未注册，请输入 /register <密码> <确认密码> 注册"));
                                        }
                                    }
                                    else
                                    {
                                        player.sendMessage(new LiteralText(list.InfoColor+"您已经完成登录，无需再次登录!"));
                                    }

                                    return 1;
                                }))
                        .executes(ctx -> {
                            ctx.getSource().getPlayer().sendMessage(new LiteralText(list.FailedColor+"请输入密码"));
                            return 1;
                        })
                )));
    }
}
