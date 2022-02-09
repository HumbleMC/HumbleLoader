package cn.enaium.humblemc.loader.launch;

import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.service.mojang.MixinServiceLaunchWrapper;

/**
 * @author Enaium
 */
public class LaunchWrapperMixinService extends MixinServiceLaunchWrapper {
    @Override
    public MixinEnvironment.CompatibilityLevel getMaxCompatibilityLevel() {
        return MixinEnvironment.CompatibilityLevel.JAVA_17;
    }
}
