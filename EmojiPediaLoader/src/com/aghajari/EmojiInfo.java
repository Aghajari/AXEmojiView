package com.aghajari;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmojiInfo {
    public String code, name, base, base_name;
    public boolean isColored;
    public String image;

    public EmojiInfo() {
    }

    public EmojiInfo(String code, String name) {
        this.code = code;
        this.name = name;
        fixBaseName();
    }

    public EmojiInfo(String code, String name, String base, String base_name, boolean isColored) {
        this.code = code;
        this.name = name;
        this.base = base;
        this.base_name = base_name;
        this.isColored = isColored;
    }


    public void fixBaseName() {
        String base_name = name;
        String[] target = Arrays.copyOf(Main.COLOR_NAMES, Main.COLOR_NAMES.length);
        Arrays.sort(target, 0, target.length, (o1, o2) -> Integer.compare(o2.length(), o1.length()));
        for (String c : target) {
            base_name = base_name.replace(c, "");
        }
        this.base_name = base_name;
        if (this.base == null || this.base.isEmpty()) {
            if (this.base_name.equals(this.name))
                this.base = this.code;
        }
    }

    @Override
    public String toString() {
        return code;
    }

    public int skin() {
        int a1 = -1, a2 = -1;
        int i = 0;

        String base_name = this.base_name;
        int colorIndex = base_name.length();
        for (String c : Main.COLOR_NAMES) {
            int ind = base_name.indexOf(c);
            if (ind != -1 && colorIndex > ind) {
                colorIndex = ind;
                a1 = i;
            }
            i++;
        }
        if (colorIndex != base_name.length())
            base_name = base_name.substring(colorIndex + 2);

        i = 0;
        for (String c : Main.COLOR_NAMES) {
            if (base_name.contains(c)) {
                a2 = i;
                break;
            }
            i++;
        }
        a2 = Math.max(0, a2) + 1;
        a1 = Math.max(0, a1) + 1;
        if (a2 >= 6)
            a2 = 0;
        if (a1 >= 6)
            a1 = 0;
        return 100 + 10 * a1 + a2;
    }

    public int skinFromCode() {
        int a1 = -1, a2 = -1;
        int i = 0;

        String code = this.code;
        int colorIndex = code.length();
        String[] colors = new String[5];

        for (int j = 1; j <= 5; j++)
            colors[j - 1] = EmojiUtils.getColorCode(j);

        for (String c : colors) {
            int ind = code.indexOf(c);
            if (ind != -1 && colorIndex > ind) {
                colorIndex = ind;
                a1 = i;
            }
            i++;
        }

        i = 0;
        for (String c : colors) {
            int ind = code.lastIndexOf(c);
            if (ind != -1 && colorIndex < ind) {
                colorIndex = ind;
                a2 = i;
            }
            i++;
        }

        a2 = Math.max(0, a2) + 1;
        a1 = Math.max(0, a1) + 1;
        if (a2 >= 6)
            a2 = 0;
        if (a1 >= 6)
            a1 = 0;
        return 100 + 10 * a1 + a2;
    }

    public int gender() {
        String name2 = name.replace("-and-", "-");
        for (int i = 0; i < Main.GENDER_NAMES.length; i++) {
            if (name2.contains(Main.GENDER_NAMES[i]))
                return i + 1;
            if (name2.startsWith(Main.GENDER_NAMES[i].substring(1) + "-"))
                return i + 1;
        }
        return 0;
    }

    public String nameWithoutGender() {
        String[] replace = {
                "and",
                "girl",
                "boy",
                "woman",
                "man",
                "people",
                "person",
                "women",
                "men",
        };
        String base_name = this.base_name;
        for (String g : replace) {
            base_name = base_name.replace("-" + g, "");
            base_name = base_name.replace(g + "-", "");
        }
        return base_name;
    }

    public static void sort(final ArrayList<ArrayList<EmojiInfo>> emojiData, String platform) {
        for (List<EmojiInfo> i : emojiData) {
            List<EmojiInfo> i2 = new ArrayList<>(i);
            i2.sort((o1, o2) -> {
                if (o1.base_name.equals(o2.base_name)) {
                    if (o1.base_name.equals(o1.name))
                        return -1;
                    if (o2.base_name.equals(o2.name))
                        return 1;
                    return Integer.compare(o1.skin(), o2.skin());
                }

                int a = i.indexOf(getInfoByName(emojiData, o1.base_name));
                int b = i.indexOf(getInfoByName(emojiData, o2.base_name));
                return Integer.compare(a, b);
            });
            i2.sort((o1, o2) -> {
                if (o1.nameWithoutGender().equals(o2.nameWithoutGender()))
                    return Integer.compare(o1.gender(), o2.gender());

                int a = i.indexOf(getInfoByReallyBaseName(emojiData, o1.nameWithoutGender()));
                int b = i.indexOf(getInfoByReallyBaseName(emojiData, o2.nameWithoutGender()));
                return Integer.compare(a, b);
            });
            i.clear();
            i.addAll(i2);
        }
        save(emojiData, platform);
    }

    public static EmojiInfo getInfoByName(ArrayList<ArrayList<EmojiInfo>> data, String name) {
        for (List<EmojiInfo> i : data) {
            for (EmojiInfo i2 : i) {
                if (i2.name.equals(name))
                    return i2;
            }
        }
        return null;
    }

    public static EmojiInfo getInfoByReallyBaseName(ArrayList<ArrayList<EmojiInfo>> data, String name) {
        for (List<EmojiInfo> i : data) {
            for (EmojiInfo i2 : i) {
                if (i2.nameWithoutGender().equals(name))
                    return i2;
            }
        }
        return null;
    }

    public static EmojiInfo getInfoByCode(String code) {
        for (List<EmojiInfo> i : data) {
            for (EmojiInfo i2 : i) {
                if (i2.code.equals(code))
                    return i2;
            }
        }
        return null;
    }

    public static int findCategory(String code, int old) {
        for (int c = 0; c < data.size(); c++) {
            for (EmojiInfo inf : data.get(c)) {
                if (inf.code.equals(code) || (inf.base != null && inf.base.equals(EmojiUtils.getBaseEmoji(code))))
                    return c;
            }
        }
        return old;
    }

    public static void save(ArrayList<ArrayList<EmojiInfo>> data, String platform) {
        try {
            File file = new File("data_" + platform + ".txt");
            if (!file.exists())
                file.createNewFile();
            PrintWriter writer = new PrintWriter(file);
            int ind = 0;
            for (List<EmojiInfo> inf : data) {
                ind++;
                int ind2 = 1;
                for (EmojiInfo i : inf) {
                    writer.write(i.code + "|" + i.name + "|" + i.base + "|"
                            + i.base_name + "|" + i.isColored + "|" + ind + "|" + (ind2++) + "|" + i.image + "\n");
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ArrayList<EmojiInfo>> data = new ArrayList<>(8);

    static {
        for (int i = 0; i < 8; i++)
            data.add(new ArrayList<>());

        try {
            List<String> lines = Files.readAllLines(Paths.get("data.txt"), Charset.defaultCharset());
            for (String line : lines) {
                if (line.trim().isEmpty() || !line.contains("|"))
                    continue;

                String[] s = line.split("\\|");
                data.get(Integer.parseInt(s[5]) - 1).add(new EmojiInfo(s[0], s[1], s[2], s[3], Boolean.parseBoolean(s[4])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
