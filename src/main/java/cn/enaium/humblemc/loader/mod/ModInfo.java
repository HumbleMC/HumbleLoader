/**
 * Copyright (C) 2022 Enaium
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
