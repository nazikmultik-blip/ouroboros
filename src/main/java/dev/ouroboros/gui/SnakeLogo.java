package dev.ouroboros.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Ouroboros logo (snake biting its own tail), drawn from
 * assets/ouroboros/textures/gui/logo.png with a soft pulsing glow behind it.
 */
public final class SnakeLogo {
    private static final ResourceLocation TEX = new ResourceLocation("ouroboros", "textures/gui/logo.png");

    private SnakeLogo() {}

    /** @param r radius of the ring in GUI pixels (the image is drawn ~2.2r wide) */
    public static void draw(float cx, float cy, float r, float time) {
        float half = r * 1.1f;
        float pulse = 0.75f + 0.25f * (float) Math.sin(time * 2f);

        GlStateManager.pushMatrix();
        GlStateManager.translate(cx, cy, 0);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        // glow
        GlStateManager.disableTexture2D();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        Tessellator t = Tessellator.getInstance();
        WorldRenderer wr = t.getWorldRenderer();
        wr.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(0, 0, 0).color(255, 255, 255, (int) (28 * pulse)).endVertex();
        for (int k = 0; k <= 32; k++) {
            double a = Math.PI * 2 * k / 32;
            wr.pos(Math.cos(a) * r * 1.5, Math.sin(a) * r * 1.5, 0).color(255, 255, 255, 0).endVertex();
        }
        t.draw();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableTexture2D();

        // logo texture (linear filtering so it stays smooth when scaled down)
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEX);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GlStateManager.color(1f, 1f, 1f, 1f);
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        wr.pos(-half, half, 0).tex(0, 1).endVertex();
        wr.pos(half, half, 0).tex(1, 1).endVertex();
        wr.pos(half, -half, 0).tex(1, 0).endVertex();
        wr.pos(-half, -half, 0).tex(0, 0).endVertex();
        t.draw();

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
