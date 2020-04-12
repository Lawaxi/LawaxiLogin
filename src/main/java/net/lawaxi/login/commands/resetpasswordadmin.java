package net.lawaxi.login.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.lawaxi.login.utils.EmailSender;
import net.lawaxi.login.utils.list;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class resetpasswordadmin {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("resetpasswordadmin")
                .requires((serverCommandSource) -> { return serverCommandSource.hasPermissionLevel(2);})
                .then(CommandManager.argument("玩家名", StringArgumentType.string())
                    .executes(ctx -> {
                        ServerPlayerEntity admin = ctx.getSource().getPlayer();
                        if(list.isLogin.get(admin.getGameProfile()).equals("true"))
                        {

                            String playername =StringArgumentType.getString(ctx,"玩家名");

                            if(list.accountConfig.hasRegister(playername))
                            {
                                String newpassword =EmailSender.getRandomCode(8);

                                list.accountConfig.setPassword(playername,newpassword);
                                admin.sendMessage(new LiteralText(list.SuccessColor+"成功更改 "+playername+" 的密码为 "+newpassword));

                                if(list.accountConfig.hasRegisterEmail(playername))
                                {
                                    switch (EmailSender.SendEmail(list.accountConfig.getEmail(playername),
                                            newpassword, 3, playername, "管理员"+admin.getEntityName()))
                                    {
                                        case "shit1":
                                            admin.sendMessage(new LiteralText(list.FailedColor+"我们正在向该玩家发送账号变更邮件，但服务器的邮箱不能完成这个操作，您是服主吗？"));
                                            break;
                                        case "shit2":
                                            admin.sendMessage(new LiteralText(list.FailedColor+"账号变更邮件发送失败"));
                                            break;
                                        default:
                                            admin.sendMessage(new LiteralText(list.SuccessColor+"成功向该玩家的邮箱发送了账号变更邮件"));
                                            break;
                                    }
                                }
                                else
                                {
                                    admin.sendMessage(new LiteralText(list.InfoColor+"该玩家尚未绑定邮箱，我们无法向他发送账号变更邮件，请联系他！"));
                                }
                            }
                            else
                            {
                                admin.sendMessage(new LiteralText(list.FailedColor+"该玩家尚未注册"));
                            }
                        }
                        else
                        {
                            admin.sendMessage(new LiteralText(list.FailedColor+"需要登录后执行"));
                        }

                        return 1;
                    }))
                .executes(ctx -> {
                    ctx.getSource().getPlayer().sendMessage(new LiteralText(list.FailedColor+"请输入要重置密码的玩家"));
                    return 1;
                }));
    }
}
