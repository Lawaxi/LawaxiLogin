package net.lawaxi.login;

import net.fabricmc.api.ModInitializer;
import net.lawaxi.login.utils.list;

public class ExampleMod implements ModInitializer {
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println(list.SystemOutPrefix+"很庆幸，这个辣鸡登陆插件没有连累到你服务器的启动!!");
	}
}
