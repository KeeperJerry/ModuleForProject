package ru.keeperjerry.forge.moduleforproject;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import ru.keeperjerry.forge.moduleforproject.gui.GuiCustomMainMenu;
import ru.keeperjerry.forge.moduleforproject.gui.GuiPlayerList;

@Mod.EventBusSubscriber(Side.CLIENT)
public class Event
{
    private static final GuiPlayerList guiPlayerList = new GuiPlayerList();

    @SubscribeEvent
    public static void onOverlay(RenderGameOverlayEvent event)
    {
        if (((Minecraft.getMinecraft().currentScreen instanceof GuiPlayerList)) && (event.isCancelable()))
        {
            event.setCanceled(true);
        }

        if (event.getType() == RenderGameOverlayEvent.ElementType.PLAYER_LIST) {
            event.setCanceled(true);
            guiPlayerList.setResolution(event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight());
            Minecraft.getMinecraft().displayGuiScreen(guiPlayerList);
        }
    }

    @SubscribeEvent
    public static void replaceMM(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiMainMenu)
            event.setGui(new GuiCustomMainMenu());
    }
}

