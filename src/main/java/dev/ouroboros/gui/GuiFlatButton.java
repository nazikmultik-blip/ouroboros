package dev.ouroboros.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiFlatButton extends GuiButton {
    private float anim;

    public GuiFlatButton(int id, int x, int y, int w, int h, String text) {
        super(id, x, y, w, h, text);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!visible) return;
        hovered = mouseX >= xPosition && mouseY >= yPosition
                && mouseX < xPosition + width && mouseY < yPosition + height;
        anim += ((hovered && enabled ? 1f : 0f) - anim) * 0.25f;

        int bg = Theme.lerp(0xC00C1410, 0xE0123A24, anim);
        drawRect(xPosition, yPosition, xPosition + width, yPosition + height, bg);
        int bar = 2 + (int) (anim * 2);
        drawRect(xPosition, yPosition, xPosition + bar, yPosition + height, enabled ? Theme.ACCENT : 0xFF3A4540);
        int col = !enabled ? Theme.MUTED : Theme.lerp(Theme.TEXT, Theme.ACCENT, anim);
        drawCenteredString(mc.fontRendererObj, displayString, xPosition + width / 2 + 1,
                yPosition + (height - 8) / 2, col);
    }
}
