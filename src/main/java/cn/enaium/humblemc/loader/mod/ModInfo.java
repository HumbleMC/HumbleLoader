package cn.enaium.humblemc.loader.mod;

/**
 * @author Enaium
 */
public class ModInfo {
    private final String name;
    private final String author;
    private final String version;
    private final String description;
    private final String mixin;

    public ModInfo(String name, String author, String version, String description, String mixin) {
        this.name = name;
        this.author = author;
        this.version = version;
        this.description = description;
        this.mixin = mixin;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public String getMixin() {
        return mixin;
    }
}
