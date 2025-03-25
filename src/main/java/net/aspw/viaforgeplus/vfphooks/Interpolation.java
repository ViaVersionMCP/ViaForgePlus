package net.aspw.viaforgeplus.vfphooks;

public class Interpolation {

    public static float animatedEyeHeight;

    public static float lerp(float start, float end, float delta) {
        return start + delta * (end - start);
    }
}
