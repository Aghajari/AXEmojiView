package com.aghajari;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aghajari.Main.*;

public class Microsoft3D {

    // https://github.com/microsoft/fluentui-emoji/tree/main/assets
    private final static File ASSETS = new File("/Users/aghajari/Downloads/fluentui-emoji-main/assets");

    private final static Map<String, Integer> GROUPS = new HashMap<>();

    private final static String FINDER_PATH = "3D";
    private final static String[] FINDER_PATHS = {
            "Default/3D",
            "Light/3D",
            "Medium-Light/3D",
            "Medium/3D",
            "Medium-Dark/3D",
            "Dark/3D"
    };

    static {
        // map groups to categories
        GROUPS.put("smileys & emotion", 0);
        GROUPS.put("people & body", 0);
        GROUPS.put("animals & nature", 1);
        GROUPS.put("food & drink", 2);
        GROUPS.put("activities", 3);
        GROUPS.put("travel & places", 4);
        GROUPS.put("objects", 5);
        GROUPS.put("symbols", 6);
        GROUPS.put("flags", 7);
    }


    public static void main(String[] args) throws FileNotFoundException {
        platform = "microsoft3d";

        dataPlatform = new ArrayList<>();
        for (int i = 0; i < CATEGORIES.length; i++)
            dataPlatform.add(new ArrayList<>());

        File[] files = ASSETS.listFiles();
        if (files == null)
            exit(-2);

        int index = 0;
        Gson gson = new Gson();

        for (File f : files) {
            if (!f.isDirectory()) {
                index++;
                continue;
            }

            Microsoft3DMetadata metadata = gson.fromJson(new FileReader(new File(f, "metadata.json")),
                    Microsoft3DMetadata.class);
            metadata.glyph = EmojiUtils.replaceEmoji(metadata.glyph);
            metadata.colored = !new File(f, FINDER_PATH).exists();

            dataPlatform.get(getCategory(metadata)).addAll(toEmojiInfos(metadata, f));
            loading("Loading", ++index, files.length);
        }

        System.out.println("\nSorting...");

        count = 0;

        for (int i = 0; i < dataPlatform.size(); i++) {
            count += dataPlatform.get(i).size();
            sort(i, dataPlatform.get(i));
        }

        EmojiInfo.sort(dataPlatform, platform);
        save(Microsoft3D::saveFile);
    }

    public static void saveFile(EmojiInfo emoji, String outputPath) {
        try {
            Files.copy(Path.of(emoji.imageFile.toURI()), new FileOutputStream(outputPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getCategory(Microsoft3DMetadata metadata) {
        for (int i = 0; i < EmojiInfo.data.size(); i++)
            for (int j = 0; j < EmojiInfo.data.get(i).size(); j++) {
                if (EmojiInfo.data.get(i).get(j).base.equals(metadata.glyph))
                    return i;
            }

        return GROUPS.get(metadata.group.toLowerCase().trim());
    }

    private static List<EmojiInfo> toEmojiInfos(Microsoft3DMetadata metadata, File file) {
        if (!metadata.colored) {
            return List.of(toEmojiInfo(metadata, 0, false, file));
        } else {
            ArrayList<EmojiInfo> list = new ArrayList<>(FINDER_PATHS.length);
            for (int i = 0; i < FINDER_PATHS.length; i++)
                list.add(toEmojiInfo(metadata, i, true, file));
            return list;
        }
    }

    private static EmojiInfo toEmojiInfo(Microsoft3DMetadata metadata, int skinIndex, boolean colored, File file) {
        EmojiInfo info = new EmojiInfo();
        info.base_name = metadata.cldr.toLowerCase()
                .replace(' ', '-')
                .replace(":", "");
        info.isColored = colored;
        info.name = info.base_name;
        info.base = metadata.glyph;
        if (colored)
            info.imageFile = findEmojiFile(file, skinIndex);
        else
            info.imageFile = findEmojiFile(file, -1);

        if (colored && skinIndex > 0) {
            info.name += COLOR_NAMES[skinIndex - 1];
            info.code = EmojiUtils.addColorToCode(info.base, skinIndex);
        } else {
            info.code = info.base;
        }
        return info;
    }

    private static File findEmojiFile(File file, int index) {
        file = new File(file, index == -1 ? FINDER_PATH : FINDER_PATHS[index]);
        File[] f = file.listFiles();

        if (f == null)
            throw new RuntimeException("File not found: " + file);

        for (File f0 : f) {
            if (f0.getName().toLowerCase().endsWith(".png"))
                return f0;
        }
        throw new RuntimeException("File not found: " + file);
    }

    public static class Microsoft3DMetadata {
        public String cldr;
        public String glyph;
        public String group;
        public boolean colored;
    }

}
