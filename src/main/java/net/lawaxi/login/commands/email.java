package net.lawaxi.login.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.lawaxi.login.config.AccountConfig;
import net.lawaxi.login.utils.EmailSender;
import net.lawaxi.login.utils.list;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class email {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("email")
                .then(CommandManager.argument("邮箱名", StringArgumentType.string())
                        .then(CommandManager.argument("邮箱域名", StringArgumentType.string())
                                .executes(ctx -> {
                                    ServerPlayerEntity player = ctx.getSource().getPlayer();
                                    if(!list.accountConfig.hasRegisterEmail(player.getEntityName()))
                                    {
                                        String email= StringArgumentType.getString(ctx,"邮箱名")+"@"+StringArgumentType.getString(ctx,"邮箱域名");
                                        if(list.accountConfig.alreadyRegister(email))
                                        {

                                            String return1 = EmailSender.SendEmail(email,0,player.getEntityName(),player.getIp());
                                            if(return1.equals("shit1"))
                                                player.sendMessage(new LiteralText(list.FailedColor+"本服务器邮箱验证服务配置有误，请联系管理员!"));
                                            else if(return1.equals("shit2"))
                                                player.sendMessage(new LiteralText(list.FailedColor+"无法将邮件发送至您的邮箱，您可以选择重试或换个邮箱地址!"));
                                            else
                                            {
                                                player.sendMessage(new LiteralText(list.SuccessColor+"邮件发送成功，请根据邮件中的提示步骤完成验证"));
                                                list.EmailVerifyCode.put(player.getGameProfile(),"0"+return1+email);
                                            }
                                        }
                                        else
                                        {
                                            player.sendMessage(new LiteralText(list.FailedColor+"同一邮箱只支持绑定一个账号，谢谢配合!"));
                                        }

                                    }
                                    else
                                    {
                                        player.sendMessage(new LiteralText(list.InfoColor+"您已经完成邮箱绑定，邮箱修改请联系管理员!"));
                                    }

                                    return 1;
                                }))
                        .executes(ctx -> {
                            ctx.getSource().getPlayer().sendMessage(new LiteralText(list.FailedColor+"新版本Minecraft不支持“@”字符的输入，我们是迫不得已的!"));
                            return 1;
                        }))
                .executes(ctx -> {
                    ctx.getSource().getPlayer().sendMessage(new LiteralText(list.FailedColor+"请输入邮箱"));
                    return 1;
                })
        );
    }
}
