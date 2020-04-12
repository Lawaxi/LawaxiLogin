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

public class register {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("register")
                .redirect(dispatcher.register(CommandManager.literal("r")
                        .then(CommandManager.argument("密码", StringArgumentType.string())
                                .then(CommandManager.argument("确认密码", StringArgumentType.string())
                                        .executes(ctx -> {
                                            ServerPlayerEntity player = ctx.getSource().getPlayer();
                                            if(!list.accountConfig.hasRegister(player.getEntityName()))
                                            {
                                                String password= StringArgumentType.getString(ctx,"密码");
                                                String password2= StringArgumentType.getString(ctx,"确认密码");
                                                if(password.equals(password2))
                                                {
                                                    list.accountConfig.setPassword(player.getEntityName(),password);
                                                    PasswordTest.finishTest(player);
                                                }
                                                else
                                                {
                                                    player.sendMessage(new LiteralText(list.FailedColor+"您两次输入的密码不同!"));
                                                }
                                            }
                                            else
                                            {
                                                player.sendMessage(new LiteralText(list.InfoColor+"您已经完成注册，修改密码请使用 /changepassword <旧密码> <新密码> !"));
                                            }
                                            return 1;
                                        }))
                                .executes(ctx -> {
                                    ctx.getSource().getPlayer().sendMessage(new LiteralText(list.FailedColor+"请输入确认密码"));
                                    return 1;
                                }))
                        .executes(ctx -> {
                            ctx.getSource().getPlayer().sendMessage(new LiteralText(list.FailedColor+"请输入密码"));
                            return 1;
                        }))));
    }
}
