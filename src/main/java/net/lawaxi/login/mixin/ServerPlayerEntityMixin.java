package net.lawaxi.login.mixin;

import net.lawaxi.login.tests.EmailTest;
import net.lawaxi.login.tests.PasswordTest;
import net.lawaxi.login.utils.list;
import net.minecraft.block.BlockState;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

	@Shadow @Final public MinecraftServer server;

	@Shadow public abstract void teleport(ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch);

	ServerWorld world;
	BlockPos pos;

	@Inject(method = "onSpawn", at = @At(value = "RETURN"))
	private void spawn( CallbackInfo info) {

		ServerPlayerEntity player = ((ServerPlayerEntity) (Object) this);
		boolean judge=false;
		if(list.isLogin.containsKey(player.getGameProfile()))
		{
			if(list.isLogin.get(player.getGameProfile()).equals("true"))
			{
				judge=true;
				PasswordTest.IPTest(player);
			}
		}
		else
		{
			list.isLogin.put(((ServerPlayerEntity) (Object) this).getGameProfile(),"false");
		}

		if(judge==false)
		{
			PasswordTest.startTest((ServerPlayerEntity) (Object) this);

			world=((ServerPlayerEntity) (Object) this).getServerWorld();
			pos=((ServerPlayerEntity) (Object) this).getBlockPos();
			((ServerPlayerEntity) (Object) this).setGameMode(GameMode.SPECTATOR);
		}
	}

	@Inject(method = "tick", at = @At(value = "RETURN"))
	private void tick( CallbackInfo info) {
		if(!list.isLogin.get(((ServerPlayerEntity) (Object) this).getGameProfile()).equals("true") || !list.isEmailTest.get(((ServerPlayerEntity) (Object) this).getGameProfile()).equals("true"))
		{
			teleport(world,pos.getX(),pos.getY(),pos.getZ(),0,0);
		}
	}
}
