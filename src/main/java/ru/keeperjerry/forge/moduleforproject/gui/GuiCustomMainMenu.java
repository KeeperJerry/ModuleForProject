package ru.keeperjerry.forge.moduleforproject.gui;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.List;

public class GuiCustomMainMenu extends GuiMainMenu {
	@Override
	public void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer")));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_, I18n.format("menu.multiplayer")));
	}

	@Override
	public void initGui() {
		this.viewportTexture = new DynamicTexture(256, 256);
		this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
		this.widthCopyright = this.fontRenderer.getStringWidth("Copyright Mojang AB. Do not distribute!");
		this.widthCopyrightRest = this.width - this.widthCopyright - 2;
		int j = this.height / 4 + 48;

		this.addSingleplayerMultiplayerButtons(j, 24);

		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 24 * 2, I18n.format("menu.options")));
		this.buttonList.add(new GuiButton(4, this.width / 2 - 100, j + 72 + 12, I18n.format("menu.quit")));

		synchronized (this.threadLock) {
			this.openGLWarning1Width = this.fontRenderer.getStringWidth(this.openGLWarning1);
			this.openGLWarning2Width = this.fontRenderer.getStringWidth(this.openGLWarning2);
			int k = Math.max(this.openGLWarning1Width, this.openGLWarning2Width);
			this.openGLWarningX1 = (this.width - k) / 2;
			this.openGLWarningY1 = (this.buttonList.get(0)).y - 24;
			this.openGLWarningX2 = this.openGLWarningX1 + k;
			this.openGLWarningY2 = this.openGLWarningY1 + 24;
		}

		this.mc.setConnectedToRealms(false);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.panoramaTimer += partialTicks;
		GlStateManager.disableAlpha();
		this.renderSkybox(mouseX, mouseY, partialTicks);
		GlStateManager.enableAlpha();
		int j = this.width / 2 - 137;
		this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
		this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
		this.mc.getTextureManager().bindTexture(MINECRAFT_TITLE_TEXTURES);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		if ((double) this.minceraftRoll < 1.0E-4D) {
			this.drawTexturedModalRect(j, 30, 0, 0, 99, 44);
			this.drawTexturedModalRect(j + 99, 30, 129, 0, 27, 44);
			this.drawTexturedModalRect(j + 99 + 26, 30, 126, 0, 3, 44);
			this.drawTexturedModalRect(j + 99 + 26 + 3, 30, 99, 0, 26, 44);
			this.drawTexturedModalRect(j + 155, 30, 0, 45, 155, 44);
		} else {
			this.drawTexturedModalRect(j, 30, 0, 0, 155, 44);
			this.drawTexturedModalRect(j + 155, 30, 0, 45, 155, 44);
		}

		this.mc.getTextureManager().bindTexture(field_194400_H);
		drawModalRectWithCustomSizedTexture(j + 88, 67, 0.0F, 0.0F, 98, 14, 128.0F, 16.0F);

		this.splashText = net.minecraftforge.client.ForgeHooksClient.renderMainMenu(this, this.fontRenderer, this.width, this.height, this.splashText);

		GlStateManager.pushMatrix();
		GlStateManager.translate((float) (this.width / 2 + 90), 70.0F, 0.0F);
		GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
		float f = 1.8F - MathHelper.abs(MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * ((float) Math.PI * 2F)) * 0.1F);
		f = f * 100.0F / (float) (this.fontRenderer.getStringWidth(this.splashText) + 32);
		GlStateManager.scale(f, f, f);
		this.drawCenteredString(this.fontRenderer, this.splashText, 0, -8, -256);
		GlStateManager.popMatrix();

		List<String> brandings = Lists.reverse(FMLCommonHandler.instance().getBrandings(true));
		List<String> brdlist = new ArrayList<>();
		for (String branding : brandings)
			if (branding.contains("Forge")) brdlist.add("Forge:" + (branding.split("Forge")[1]));
			else if (!branding.contains("MCP")) brdlist.add(branding);

		for (String line : brdlist)
			if (!Strings.isNullOrEmpty(line))
				this.drawString(this.fontRenderer, line, 2, this.height - (10 + brdlist.indexOf(line) * (this.fontRenderer.FONT_HEIGHT + 1)), 16777215);


		if (this.openGLWarning1 != null && !this.openGLWarning1.isEmpty()) {
			drawRect(this.openGLWarningX1 - 2, this.openGLWarningY1 - 2, this.openGLWarningX2 + 2, this.openGLWarningY2 - 1, 1428160512);
			this.drawString(this.fontRenderer, this.openGLWarning1, this.openGLWarningX1, this.openGLWarningY1, -1);
			this.drawString(this.fontRenderer, this.openGLWarning2, (this.width - this.openGLWarning2Width) / 2, (this.buttonList.get(0)).y - 12, -1);
		}

		for (GuiButton guiButton : this.buttonList) guiButton.drawButton(this.mc, mouseX, mouseY, partialTicks);

		for (GuiLabel guiLabel : this.labelList) guiLabel.drawLabel(this.mc, mouseX, mouseY);
	}
}
