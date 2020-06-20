package ru.keeperjerry.forge.moduleforproject;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.Display;

@Mod(
		modid = Main.MOD_ID,
		name = Main.MOD_NAME,
		version = Main.VERSION
)
public class Main {
	public static final String MOD_ID = "moduleforproject";
	public static final String MOD_NAME = "ModuleForProject";
	public static final String VERSION = "1.0.0.0";

	// Project Settings
	public static String project_Name = "FlyWars";
	public static String client_Name = "Client";

	// For debug
	public static boolean debug = true;

	@SideOnly(Side.CLIENT)
	private void minecarftTitle(String operatorTitle) {
		Display.setTitle(project_Name + " | " + client_Name + " | " + operatorTitle);
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		minecarftTitle("Идет pre-инициализация...");

		System.out.println("[" + MOD_NAME + "][Starting PRE-INITIALIZATION]");
		System.out.println("[" + MOD_NAME + "][Start Support's Module]");
		System.out.println("[" + MOD_NAME + "][Project] = " + project_Name);
		System.out.println("[" + MOD_NAME + "][Client] = " + client_Name);
		System.out.println("[" + MOD_NAME + "][Assembly] = " + (debug ? "Debug" : "Release"));

		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		minecarftTitle("Идет инициализация...");
		System.out.println("[" + MOD_NAME + "][Starting INITIALIZATION]");
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		minecarftTitle("Идет post-инициализация...");
		System.out.println("[" + MOD_NAME + "][Starting POST-INITIALIZATION]");
	}

	@SubscribeEvent
	public void firstJoinMenu(GuiOpenEvent event) {
		if (event.getGui() instanceof GuiMainMenu) {
			minecarftTitle("Игрок: " + Minecraft.getMinecraft().getSession().getUsername());
		}
	}
}
