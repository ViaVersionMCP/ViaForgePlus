package net.aspw.viaforgeplus.screen;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.util.DumpUtil;
import net.aspw.viaforgeplus.common.CommonViaForgePlus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class GuiProtocolSelectorScreen extends GuiScreen {

    private final GuiScreen parent;
    private final boolean simple;
    private final FinishedCallback finishedCallback;

    private SlotList list;

    public GuiProtocolSelectorScreen(final GuiScreen parent) {
        this(parent, false, (version, unused) -> {
            CommonViaForgePlus.getManager().setTargetVersion(version);
        });
    }

    public GuiProtocolSelectorScreen(final GuiScreen parent, final boolean simple, final FinishedCallback finishedCallback) {
        this.parent = parent;
        this.simple = simple;
        this.finishedCallback = finishedCallback;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(new GuiButton(1, 5, height - 25, 20, 20, "<-"));
        if (!this.simple) {
            buttonList.add(new GuiButton(2, width - 105, height - 25, 100, 20, "Reload configs"));
        }

        list = new SlotList(mc, width, height, 3 + 3 + (fontRendererObj.FONT_HEIGHT + 2) * 3, height - 30, fontRendererObj.FONT_HEIGHT + 2);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        list.actionPerformed(button);

        if (button.id == 1) {
            mc.displayGuiScreen(parent);
        } else if (button.id == 2) {
            Via.getManager().getConfigurationProvider().reloadConfigs();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parent);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        list.handleMouseInput();
        super.handleMouseInput();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        list.drawScreen(mouseX, mouseY, partialTicks);

        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        drawCenteredString(fontRendererObj, ChatFormatting.GOLD + "ViaForgePlus (" + (CommonViaForgePlus.isModLatest ? "Latest Build" : "Outdated Build") + ")", width / 4, 3, 16777215);
        GL11.glPopMatrix();

        drawCenteredString(fontRendererObj, "https://nattogreatapi.pages.dev/ViaForgePlus/", width / 2, (fontRendererObj.FONT_HEIGHT + 2) * 2 + 3, -1);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    class SlotList extends GuiSlot {

        public SlotList(Minecraft client, int width, int height, int top, int bottom, int slotHeight) {
            super(client, width, height, top, bottom, slotHeight);
        }

        @Override
        protected int getSize() {
            return CommonViaForgePlus.supportedProtocols.size();
        }

        @Override
        protected void elementClicked(int index, boolean b, int i1, int i2) {
            finishedCallback.finished(CommonViaForgePlus.supportedProtocols.get(index), parent);
        }

        @Override
        protected boolean isSelected(int index) {
            return false;
        }

        @Override
        protected void drawBackground() {
            drawDefaultBackground();
        }

        @Override
        protected void drawSlot(int index, int x, int y, int slotHeight, int mouseX, int mouseY) {
            final ProtocolVersion targetVersion = CommonViaForgePlus.getManager().getTargetVersion();
            final ProtocolVersion version = CommonViaForgePlus.supportedProtocols.get(index);

            String color;
            if (targetVersion == version) {
                color = GuiProtocolSelectorScreen.this.simple ? ChatFormatting.GOLD.toString() : ChatFormatting.GREEN.toString();
            } else {
                color = GuiProtocolSelectorScreen.this.simple ? ChatFormatting.WHITE.toString() : ChatFormatting.DARK_RED.toString();
            }

            drawCenteredString(mc.fontRendererObj,(color) + version.getName(), width / 2, y, -1);
        }
    }

    public interface FinishedCallback {

        void finished(final ProtocolVersion version, final GuiScreen parent);
        
    }
    
}
