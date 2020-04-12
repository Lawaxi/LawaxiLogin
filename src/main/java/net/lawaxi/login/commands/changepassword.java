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

public class changepassword {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("changepassword")
                .then(CommandManager.argument("旧密码", StringArgumentType.string())
                        .then(CommandManager.argument("新密码", StringArgumentType.string())
                                .executes(ctx -> {
                                    ServerPlayerEntity player = ctx.getSource().getPlayer();
                                    if(list.accountConfig.hasRegister(player.getEntityName()))
                                    {
                                        String old1= StringArgumentType.getString(ctx,"旧密码");
                                        String new1= StringArgumentType.getString(ctx,"新密码");
                                        if(old1.equals(list.accountConfig.getPassword(player.getEntityName())))
                                        {
                                            list.accountConfig.setPassword(player.getEntityName(),new1);
                                            player.sendMessage(new LiteralText(list.SuccessColor+"密码修改成功"));

                                            if(list.accountConfig.hasRegisterEmail(player.getEntityName()))
                                            {
                                                EmailSender.SendEmail(list.accountConfig.getEmail(player.getEntityName()),
                                                    new1, 3, player.getEntityName(), player.getIp());
                                            }
                                            else
                                            {
                                                player.sendMessage(new LiteralText(list.InfoColor+"请绑定邮箱，这样您将来的账户变更都会被告知！"));
                                            }

                                        }
                                        else
                                        {
                                            player.sendMessage(new LiteralText(list.FailedColor+"旧密码输入有误！"));
                                        }
                                    }
                                    else
                                    {
                                        player.sendMessage(new LiteralText(list.InfoColor+"您尚未完成注册，请输入/register <密码> <确认密码> 注册"));
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
                }));
    }
}
