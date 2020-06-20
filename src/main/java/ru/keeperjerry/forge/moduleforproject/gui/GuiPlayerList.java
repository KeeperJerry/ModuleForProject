package ru.keeperjerry.forge.moduleforproject.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

// Я взял это у Pixelmon 1.7.10
public class GuiPlayerList extends GuiScreen
{
    static ConcurrentHashMap<String, DynamicTexture> cachedTextures = new ConcurrentHashMap();
    int buttonID = 0;
    GuiButton previousButton;
    GuiButton nextButton;
    int currentPage = 0;

    public GuiPlayerList()
    {
    }

    public void setResolution(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void initGui() {
        this.previousButton = new GuiButton(this.buttonID++, (this.width - 60) / 2 - 110, 180, 60, 20, "Previous");
        this.nextButton = new GuiButton(this.buttonID++, (this.width - 60) / 2 + 110, 180, 60, 20, "Next");
        this.buttonList.add(this.previousButton);
        this.buttonList.add(this.nextButton);
        this.previousButton.enabled = false;
        this.nextButton.enabled = false;
    }

    public void onGuiClosed() {
        super.onGuiClosed();
        Iterator var1 = cachedTextures.values().iterator();

        while(var1.hasNext()) {
            DynamicTexture texture = (DynamicTexture)var1.next();
            texture.deleteGlTexture();
        }

        cachedTextures.clear();
    }

    public void drawScreen(int mouseX, int mouseY, float random) {
        try {
            if (!Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindPlayerList.getKeyCode())) {
                this.mc.displayGuiScreen(null);
            }
        } catch (IndexOutOfBoundsException var23) {
            this.mc.displayGuiScreen(null);
        }

        NetHandlerPlayClient nethandlerplayclient = mc.player.connection;
        List<NetworkPlayerInfo> players = new ArrayList<>(nethandlerplayclient.getPlayerInfoMap());

        int rows = (int)Math.ceil((double)players.size() / 3.0D);
        if (rows > 9) {
            rows = 9;
        }

        int columns = 3;
        int playersPerPage = 27;
        int columnWidth = 300 / columns;
        int columnHeight = 18;
        if (columnWidth > 150) {
            columnWidth = 150;
        }

        int left = (this.width - columns * columnWidth) / 2;
        byte border = 10;
        int extraRows = 0;
        if (players.size() > playersPerPage) {
            extraRows = 2;
        } else {
            this.currentPage = 0;
        }

        drawRect(left - 1, border - 1, left + columnWidth * columns, border + columnHeight * (rows + extraRows), -2147483648);
        int pages = players.size() / playersPerPage + 1;

        int strWidth;
        for(int i = this.currentPage * playersPerPage; i < this.currentPage * playersPerPage + playersPerPage; ++i) {
            strWidth = i - this.currentPage * playersPerPage;
            int xPos = left + strWidth % columns * columnWidth;
            int yPos = border + strWidth / columns * columnHeight;
            if (i < players.size()) {
                drawRect(xPos, yPos, xPos + columnWidth - 1, yPos + columnHeight - 1, (new Color(158, 152, 152, 100)).getRGB());
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(3008);
                NetworkPlayerInfo player = players.get(i);
                ScorePlayerTeam team = this.mc.world.getScoreboard().getPlayersTeam(player.getGameProfile().getName());
                String displayName = ScorePlayerTeam.formatPlayerName(team, player.getGameProfile().getName());
                this.mc.fontRenderer.drawStringWithShadow(displayName, xPos, yPos, 16777215);
                TextFormatting pingColour = TextFormatting.DARK_RED;
                if (player.getResponseTime() < 151) {
                    pingColour = TextFormatting.GREEN;
                } else if (player.getResponseTime() < 300) {
                    pingColour = TextFormatting.GOLD;
                }

                this.mc.fontRenderer.drawStringWithShadow("Ping: " + pingColour + player.getResponseTime(), xPos, yPos + 9, 16777215);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

                if (player.getLocationSkin() != null)
                {
                    mc.getTextureManager().bindTexture(player.getLocationSkin());
                    renderSkinHead((xPos + columnWidth - 20),yPos,2.0F, 2.0F);
                }
            }
        }

        if (players.size() > playersPerPage) {
            this.previousButton.enabled = this.currentPage > 0;
            this.nextButton.enabled = this.currentPage != pages - 1;
            this.previousButton.drawButton(this.mc, mouseX, mouseY, random);
            this.nextButton.drawButton(this.mc, mouseX, mouseY, random);
            String pageCount = this.currentPage + 1 + "/" + (players.size() / 27 + 1);
            strWidth = this.mc.fontRenderer.getStringWidth(pageCount);
            this.mc.fontRenderer.drawStringWithShadow(pageCount, (this.width - strWidth) / 2, 185, 16777215);
        } else {
            this.previousButton.enabled = false;
            this.nextButton.enabled = false;
        }

    }

    public static void renderSkinHead(int x, int y, float w, float h)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 2f);
        GlStateManager.scale(w, h, 0f);
        Gui.drawScaledCustomSizeModalRect(0, 0, 8.0F, 8.0F, 8, 8, 8, 8, 64.0F, 64.0F);
        GlStateManager.popMatrix();
    }

    protected void actionPerformed(GuiButton button)
    {
        if (button.equals(this.previousButton))
        {
            --this.currentPage;
        }
        else if (button.equals(this.nextButton))
        {
            ++this.currentPage;
        }

    }
}
