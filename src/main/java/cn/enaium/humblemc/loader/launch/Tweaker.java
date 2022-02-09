package cn.enaium.humblemc.loader.launch;

import cn.enaium.humblemc.loader.mod.ModInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.launchwrapper.LogWrapper;
import net.minecraft.launchwrapper.template.TemplateTarget;
import net.minecraft.launchwrapper.template.TemplateTransformer;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.rmi.RemoteException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @author Enaium
 */
public class Tweaker implements ITweaker {

    private final List<String> arguments = new ArrayList<>();

    private String gameDir = System.getProperty("user.dir");

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        this.arguments.addAll(args);
        if (gameDir != null) {
            this.gameDir = gameDir.getAbsolutePath();
        }

        addArgument("--gameDir", gameDir != null ? gameDir.getAbsolutePath() : this.gameDir);
        addArgument("--assetsDir", assetsDir != null ? assetsDir.getPath() : null);
        addArgument("--version", profile);
    }

    public void addArgument(String name, String value) {
        if (value != null) {
            arguments.add(name);
            arguments.add(value);
        } else {
            LogWrapper.severe("Argument %s is %s", name, null);
        }
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        List<String> mixins = new ArrayList<>();
        File mods = new File(gameDir, "mods");
        if (!mods.exists()) {
            mods.mkdir();
        }
        try {
            Files.walkFileTree(mods.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toFile().getName().endsWith(".jar")) {
                        try {
                            JarFile jarFile = new JarFile(file.toFile());
                            if (jarFile.getEntry("humble.mod.json") != null) {
                                classLoader.addURL(file.toUri().toURL());
                            } else {
                                LogWrapper.log("HumbleLoader", Level.WARN, "Ignore %s it is not a humble mod or humble.mod.json not found", file.toFile().getName());
                            }
                        } catch (Throwable throwable) {
                            LogWrapper.log("HumbleLoader", Level.ERROR, throwable, "File %s fail to load", file.toFile().getName());
                        }
                    }
                    return super.visitFile(file, attrs);
                }
            });

            Enumeration<URL> resources = classLoader.getResources("humble.mod.json");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();

                ModInfo modInfo = new Gson().fromJson(IOUtils.toString(url.openStream(), StandardCharsets.UTF_8), ModInfo.class);
                if (modInfo.getMixin() != null) {
                    mixins.add(modInfo.getMixin());
                }
                LogWrapper.log("HumbleLoader", Level.INFO, "%s | %s | %s | %s", modInfo.getName(), modInfo.getAuthor(), modInfo.getVersion(), modInfo.getDescription());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        MixinBootstrap.init();
        mixins.forEach(Mixins::addConfiguration);
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public String[] getLaunchArguments() {
        return arguments.toArray(new String[0]);
    }
}
