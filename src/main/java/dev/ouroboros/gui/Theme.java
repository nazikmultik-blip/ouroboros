package dev.ouroboros.gui;

public final class Theme {
    public static final int ACCENT = 0xFF3DFF8A;
    public static final int ACCENT_DIM = 0xFF1F9E55;
    public static final int TEXT = 0xFFC9D6CF;
    public static final int MUTED = 0xFF6B7570;
    public static final int PANEL = 0xD00A100D;

    private Theme() {}

    public static int lerp(int a, int b, float t) {
        t = Math.max(0f, Math.min(1f, t));
        int out = 0;
        for (int shift = 0; shift <= 24; shift += 8) {
            int ca = (a >> shift) & 0xFF, cb = (b >> shift) & 0xFF;
            out |= ((int) (ca + (cb - ca) * t) & 0xFF) << shift;
        }
        return out;
    }

    public static float time() {
        return (System.currentTimeMillis() % 1000000L) / 1000f;
    }
}
