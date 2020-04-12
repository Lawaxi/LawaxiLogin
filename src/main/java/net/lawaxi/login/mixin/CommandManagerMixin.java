package net.lawaxi.login.mixin;


import com.mojang.brigadier.CommandDispatcher;
import net.lawaxi.login.config.AccountConfig;
import net.lawaxi.login.config.AdminConfig;
import net.lawaxi.login.utils.list;
import net.lawaxi.login.commands.*;
import net.lawaxi.login.utils.EmailSender;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public class CommandManagerMixin {

    @Shadow
    @Final
    private final CommandDispatcher<ServerCommandSource> dispatcher = new CommandDispatcher();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onRegistry(boolean boolean_1, CallbackInfo ci) {

        new list();
        new AdminConfig();
        new EmailSender();

        login.register(dispatcher);
        register.register(dispatcher);
        email.register(dispatcher);
        verify.register(dispatcher);
        changepassword.register(dispatcher);
        resetpassword.register(dispatcher);
        resetpasswordadmin.register(dispatcher);
    }
}
