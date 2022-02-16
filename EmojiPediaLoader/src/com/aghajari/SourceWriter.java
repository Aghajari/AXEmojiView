package com.aghajari;

import java.util.*;

public class SourceWriter {

    public static String source(String packageName, String className,
                                ArrayList<ArrayList<EmojiInfo>> emoji, String folder) {
        className = "AX" + className + "EmojiData";

        StringBuilder coloredData = new StringBuilder();
        for (List<EmojiInfo> list : emoji) {
            for (EmojiInfo info : list) {
                if (info.isColored && info.base_name.equals(info.name))
                    coloredData.append('"').append(info.code).append("\", ");
            }
        }

        String tab1 = "            ", tab2 = "                    ";
        StringBuilder dataColored = new StringBuilder();
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < emoji.size(); i++) {
            dataColored.append(tab1).append("new String[] {\n").append(tab2);
            data.append(tab1).append("new String[] {\n").append(tab2);
            for (EmojiInfo e : emoji.get(i)) {
                data.append('"').append(e).append("\", ");
                if (e.base_name.equals(e.name) && !EmojiUtils.shouldIgnoreEmoji(e.code))
                    dataColored.append('"').append(e).append("\", ");
            }
            dataColored.append('\n').append(tab1).append("},\n");
            data.append('\n').append(tab1).append("},\n");
        }

        HashMap<String, ArrayList<EmojiInfo>> custom = new HashMap<>();
        for (int i = 0; i < emoji.size(); i++) {
            for (EmojiInfo e : emoji.get(i)) {
                if (e.isColored) {
                    ArrayList<EmojiInfo> l = custom.computeIfAbsent(e.base_name, s -> new ArrayList<>());
                    if (e.base_name.equals(e.name))
                        l.add(0, e);
                    else
                        l.add(e);
                }
            }
        }

        HashSet<String> keys = new HashSet<>(custom.keySet());
        for (String key : keys) {
            ArrayList<EmojiInfo> infos = custom.get(key);
            infos.sort(Comparator.comparingInt(EmojiInfo::skinFromCode));
            if (infos.size() == 6) {
                boolean notCustom = true;
                String base = infos.get(0).base == null ? infos.get(0).code : infos.get(0).base;
                for (int i = 0; i < 6; i++) {
                    boolean find = false;
                    String code = EmojiUtils.replaceEmoji(EmojiUtils.addColorToCode(base, i));
                    for (EmojiInfo info : infos) {
                        if (EmojiUtils.replaceEmoji(info.code).equals(code)) {
                            find = true;
                            break;
                        }
                    }
                    if (!find) {
                        notCustom = false;
                        break;
                    }
                }
                if (notCustom) {
                    custom.remove(key);
                }
            } else if (infos.size() < 6)
                custom.remove(key);
        }

        StringBuilder customText;
        boolean hasCustom = false;
        if (custom.size() == 0) {
            customText = new StringBuilder("\n");
        } else {
            hasCustom = true;
            customText = new StringBuilder("\n    public static final String[][] emojiCustomColored = {\n");
            for (ArrayList<EmojiInfo> l : custom.values()) {
                customText.append(tab1).append("new String[] {");
                for (EmojiInfo e : l) {
                    customText.append('"').append(e).append("\", ");
                }
                customText.deleteCharAt(customText.length() - 1);
                customText.deleteCharAt(customText.length() - 1);
                customText.append("},\n");
            }
            customText.deleteCharAt(customText.length() - 1);
            customText.deleteCharAt(customText.length() - 1);
            customText.append("\n    };\n\n");
        }

        return "/*\n" +
                " * Copyright (C) 2022 - Amir Hossein Aghajari\n" +
                " *\n" +
                " * Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                " * you may not use this file except in compliance with the License.\n" +
                " * You may obtain a copy of the License at\n" +
                " *\n" +
                " *     http://www.apache.org/licenses/LICENSE-2.0\n" +
                " *\n" +
                " * Unless required by applicable law or agreed to in writing, software\n" +
                " * distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                " * See the License for the specific language governing permissions and\n" +
                " * limitations under the License.\n" +
                " *\n" +
                " */\n" +
                "\n" +
                "\n" +
                "package com.aghajari.emojiview." + packageName + ";\n" +
                "\n" +
                "import com.aghajari.emojiview.emoji.EmojiData;\n" +
                "\n" +
                "import java.util.Collections;\n" +
                (hasCustom ? "import java.util.HashMap;\n" : "") +
                "import java.util.HashSet;\n" +
                "\n" +
                "public class " + className + " extends EmojiData {\n" +
                "    static final " + className + " instance = new " + className + "();\n" +
                "\n" +
                "    public static final String[] emojiColored = {\n" +
                "            " + coloredData.toString() +
                "\n    };\n" +
                customText.toString() +
                "    public static final String[][] dataColored = {\n" + dataColored.toString() +
                "\n    };\n" +
                "\n" +
                "    public static final String[][] data = {\n" + data.toString() +
                "    };\n" +
                "\n" +
                "\n" +
                "    protected static final HashSet<String> emojiColoredMap = new HashSet<>(emojiColored.length);\n" +
                (hasCustom ? "    protected static final HashMap<String, String[]> emojiCustomColoredMap = new HashMap<>(emojiCustomColored.length);\n" : "") +
                "\n" +
                "    static {\n" +
                "        Collections.addAll(emojiColoredMap, emojiColored);\n" +
                (hasCustom ? "        for (int i = 0; i < emojiCustomColored.length; i++) {\n" +
                        "            emojiCustomColoredMap.put(emojiCustomColored[i][0], emojiCustomColored[i]);\n" +
                        "        }\n" : "") +
                "    }\n" +
                "\n" +
                "    private " + className + "(){}\n" +
                "\n" +
                "    @Override\n" +
                "    public String[][] getData() {\n" +
                "        return data;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String[][] getDataColored() {\n" +
                "        return dataColored;\n" +
                "    }\n" +
                "\n" +
                (hasCustom ? "    @Override\n" +
                        "    public String[] getEmojiVariants(String code) {\n" +
                        "        if (emojiCustomColoredMap.containsKey(code)) {\n" +
                        "            String[] vars = emojiCustomColoredMap.get(code);\n" +
                        "            String[] out = new String[vars.length - 1];\n" +
                        "            System.arraycopy(vars, 1, out, 0, out.length);\n" +
                        "            return out;\n" +
                        "        }\n" +
                        "        return super.getEmojiVariants(code);\n" +
                        "    }\n\n" : "") +
                "    @Override\n" +
                "    public boolean isColoredEmoji(String code) {\n" +
                "        return emojiColoredMap.contains(getBaseEmoji(code));\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public int getEmojiCount(int category) {\n" +
                "        return data[category].length;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String getEmojiFolderNameInAssets() {\n" +
                "    " +
                "    return \"" + folder + "\";\n" +
                "    }\n" +
                "}\n";
    }

    public static String source2(String packageName, String className) {
        String classData = "AX" + className + "EmojiData";

        return "/*\n" +
                " * Copyright (C) 2022 - Amir Hossein Aghajari\n" +
                " *\n" +
                " * Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                " * you may not use this file except in compliance with the License.\n" +
                " * You may obtain a copy of the License at\n" +
                " *\n" +
                " *     http://www.apache.org/licenses/LICENSE-2.0\n" +
                " *\n" +
                " * Unless required by applicable law or agreed to in writing, software\n" +
                " * distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                " * See the License for the specific language governing permissions and\n" +
                " * limitations under the License.\n" +
                " *\n" +
                " */\n" +
                "\n" +
                "package com.aghajari.emojiview." + packageName + ";\n" +
                "\n" +
                "import android.content.Context;\n" +
                "\n" +
                "import com.aghajari.emojiview.R;\n" +
                "import com.aghajari.emojiview.emoji.Emoji;\n" +
                "import com.aghajari.emojiview.emoji.EmojiCategory;\n" +
                "import com.aghajari.emojiview.emoji.EmojiData;\n" +
                "import com.aghajari.emojiview.preset.AXPresetEmoji;\n" +
                "import com.aghajari.emojiview.preset.AXPresetEmojiCategory;\n" +
                "import com.aghajari.emojiview.preset.AXPresetEmojiProvider;\n" +
                "\n" +
                "public final class AX" + className + "EmojiProvider extends AXPresetEmojiProvider {\n" +
                "\n" +
                "    public AX" + className + "EmojiProvider(Context context) {\n" +
                "        super(context, new int[]{\n" +
                "                R.drawable.emoji_ios_category_people,\n" +
                "                R.drawable.emoji_ios_category_nature,\n" +
                "                R.drawable.emoji_ios_category_food,\n" +
                "                R.drawable.emoji_ios_category_activity,\n" +
                "                R.drawable.emoji_ios_category_travel,\n" +
                "                R.drawable.emoji_ios_category_objects,\n" +
                "                R.drawable.emoji_ios_category_symbols,\n" +
                "                R.drawable.emoji_ios_category_flags\n" +
                "        });\n" +
                "    }\n" +
                "\n" +
                "    public AX" + className + "EmojiProvider(Context context, int[] icons) {\n" +
                "        super(context, icons);\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public EmojiData getEmojiData() {\n" +
                "        return " + classData + ".instance;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    protected EmojiCategory createCategory(int i, int icon) {\n" +
                "        return new AX" + className + "EmojiCategory(i, icon, getEmojiData());\n" +
                "    }\n" +
                "\n" +
                "    public static class AX" + className + "EmojiCategory extends AXPresetEmojiCategory {\n" +
                "        public AX" + className + "EmojiCategory(int i, int icon, EmojiData emojiData) {\n" +
                "            super(i, icon, emojiData);\n" +
                "        }\n" +
                "\n" +
                "        @Override\n" +
                "        protected Emoji createEmoji(String code, int category, int index, EmojiData emojiData){\n" +
                "            return new AX" + className + "Emoji(code, emojiData);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    public static class AX" + className + "Emoji extends AXPresetEmoji {\n" +
                "        public AX" + className + "Emoji(String code, EmojiData emojiData) {\n" +
                "            super(code, emojiData);\n" +
                "        }\n" +
                "    }\n" +
                "}\n";
    }
}
