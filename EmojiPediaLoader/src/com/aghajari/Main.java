package com.aghajari;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static final String BASE_URL = "https://emojipedia.org/";

    public static final String[] CATEGORIES = {
            "people",
            "nature",
            "food-drink",
            "activity",
            "travel-places",
            "objects",
            "symbols",
            "flags"
    };
    public static final String[] COLOR_NAMES = {
            "-light-skin-tone",
            "-medium-light-skin-tone",
            "-medium-skin-tone",
            "-medium-dark-skin-tone",
            "-dark-skin-tone",
            "-no-skin-tone"
    };
    public static final String[] GENDER_NAMES = new String[]{
            "-woman-woman",
            "-women",
            "-woman-man",
            "-man-woman",
            "-person-woman",
            "-woman-person",
            "-man-man",
            "-men",
            "-person-person",
            "-person-man",
            "-man-person",
    };

    public static ArrayList<ArrayList<EmojiInfo>> dataPlatform;
    public static String platform;
    public static int count;


    public static void main(String[] args) {
        //initializeEmoji();
        loadPlatform();
        save();
    }

    /*public static void initializeEmoji() {
        if (emoji != null)
            return;

        emoji = new ArrayList<>();
        String format = "\rGetting latest updates... (%d/" + CATEGORIES.length + ")";
        int index = 0, count = 0;


        final Pattern pattern = Pattern.compile("<ul class=\"emoji-list\">([\\S\\s]*?)<\\/ul>", Pattern.MULTILINE);
        final Pattern pattern2 = Pattern.compile("<a href=\"\\/(.*?)\\/\"><span class=\"emoji\">(.*?)<\\/span>", Pattern.MULTILINE);

        for (String c : CATEGORIES) {
            System.out.printf(format, ++index);
            String res = getContent(c);
            if (res.isEmpty())
                exit(-1);

            HashMap<String, String> cEmoji = new HashMap<>();
            emoji.add(cEmoji);

            Matcher matcher = pattern.matcher(res);
            if (matcher.find()) {
                matcher = pattern2.matcher(matcher.group(0));

                while (matcher.find()) {
                    cEmoji.put(EmojiUtils.replaceEmoji(matcher.group(2).trim()), matcher.group(1));
                }
            } else
                exit(-2);

            count += cEmoji.size();
        }
        System.out.println("\r" + count + " emoji loaded...");
    }*/

    public static void loadPlatform() {
        System.out.print("Enter platform name: ");
        platform = new Scanner(System.in).next();

        System.out.println("Getting platform data...");
        String platformPage = getContent(platform);
        if (platformPage.isEmpty())
            exit(-1);

        final Pattern pattern = Pattern.compile("<ul class=\"emoji-grid\">([\\S\\s]*?)<\\/ul>", Pattern.MULTILINE);
        final Pattern href = Pattern.compile("<a href=\"\\/(.*?)\\/\"", Pattern.MULTILINE);
        final Pattern title = Pattern.compile("<title>(.*?)\\s", Pattern.MULTILINE);
        final Pattern data_src = Pattern.compile("<img.*?data-src=\"(.*?)\"", Pattern.MULTILINE);
        final Pattern src = Pattern.compile("<img.*?src=\"(.*?)\"", Pattern.MULTILINE);

        Matcher matcher = pattern.matcher(platformPage);
        dataPlatform = new ArrayList<>();
        for (int i = 0; i < CATEGORIES.length; i++)
            dataPlatform.add(new ArrayList<>());

        if (matcher.find()) {
            String[] lines = matcher.group(1).split("[\\r\\n]+");

            EmojiInfo emoji = null;
            int category = 0, index = 0;
            count = 0;

            for (String line : lines) {
                loading("Loading", ++index, lines.length);

                line = line.trim();
                if (line.startsWith("<li")) {
                    emoji = new EmojiInfo();
                } else if (line.startsWith("</li")) {
                    if (emoji == null)
                        continue;

                    category = EmojiInfo.findCategory(emoji.code, category);
                    dataPlatform.get(category).add(emoji);
                    emoji = null;
                    count++;
                } else if (emoji != null) {
                    if (line.startsWith("<a")) {
                        matcher = href.matcher(line);
                        matcher.find();
                        String h = matcher.group(1).trim();

                        EmojiInfo find = EmojiInfo.getInfoByName(EmojiInfo.data, h);

                        if (find == null) {
                            String res = getContent(h);
                            if (res.isEmpty())
                                exit(-3);

                            matcher = title.matcher(res);
                            matcher.find();
                            emoji.name = h;
                            emoji.code = EmojiUtils.replaceEmoji(matcher.group(1).trim());
                            emoji.fixBaseName();
                            find = EmojiInfo.getInfoByCode(emoji.code);
                        }

                        if (find != null) {
                            emoji.code = find.code;
                            emoji.base = find.base;
                            emoji.base_name = find.base_name;
                            emoji.name = find.name;
                        }
                    } else if (line.startsWith("<img")) {
                        Pattern src_pattern = line.contains("data-src=") ? data_src : src;
                        matcher = src_pattern.matcher(line);
                        matcher.find();
                        emoji.image = matcher.group(1).trim();
                    }
                }
            }
        } else
            exit(-2);

        System.out.println();
        System.out.println("Sorting...");
        for (int i = 0; i < dataPlatform.size(); i++) {
            List<EmojiInfo> list = dataPlatform.get(i);
            for (EmojiInfo info : list) {
                info.isColored = false;
                a:
                for (List<EmojiInfo> list2 : dataPlatform) {
                    for (EmojiInfo info2 : list2) {
                        if (info != info2 && info.base_name.equals(info2.base_name)) {
                            info.isColored = true;
                            break a;
                        }
                    }
                }

                if (info.isColored && (info.base == null || info.base.isEmpty())) {
                    if (info.name.equals(info.base_name))
                        info.base = info.code;
                    else {
                        b:
                        for (List<EmojiInfo> list2 : dataPlatform) {
                            for (EmojiInfo info2 : list2) {
                                if (info != info2 && info.base_name.equals(info2.name)) {
                                    info.base = info2.code;
                                    break b;
                                }
                            }
                        }
                    }
                }
            }
            final int finalI = i;
            list.sort((o1, o2) -> {
                int a = indexOf(EmojiInfo.data.get(finalI), o1);
                int b = indexOf(EmojiInfo.data.get(finalI), o2);
                if (a == -1)
                    return b == -1 ? 0 : 1;
                else if (b == -1)
                    return -1;
                return Integer.compare(a, b);
            });
        }
        EmojiInfo.sort(dataPlatform, platform);
    }

    public static void save() {
        platform = platform.toLowerCase();
        System.out.print("Enter class name: ");
        String className = new Scanner(System.in).next();
        String packageName = platform.toLowerCase() + "provider";
        loading("Saving", 0, count);

        new File(platform + "_emoji").mkdir();
        new File(platform + "_source").mkdir();

        File src = new File(platform + "_source/AX" + className + "EmojiData.java");
        File src2 = new File(platform + "_source/AX" + className + "EmojiProvider.java");
        try {
            src.createNewFile();
            src2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter out = new PrintWriter(src)) {
            out.println(SourceWriter.source(packageName,
                    className, dataPlatform, platform + "_emoji"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (PrintWriter out = new PrintWriter(src2)) {
            out.println(SourceWriter.source2(packageName, className));
        } catch (Exception e) {
            e.printStackTrace();
        }

        int index = 1;
        for (int i = 0; i < dataPlatform.size(); i++) {
            for (int j = 0; j < dataPlatform.get(i).size(); j++) {
                loading("Saving", index++, count);
                downloadEmoji(dataPlatform.get(i).get(j).image, i, j);
            }
        }
    }

    public static void downloadEmoji(String link, int c, int i) {
        //if (true) return;

        String outputPath = platform + "_emoji/" + c + "_" + i + ".png";
        if (new File(platform).exists())
            return;

        try {
            URL url = new URL(link);
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();

            FileOutputStream fos = new FileOutputStream(outputPath);
            fos.write(response);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getContent(String url) {
        try {
            Scanner s = new Scanner(new URL(BASE_URL + url).openStream()).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getContent2(String url) throws Exception {
        Scanner s = new Scanner(new URL(BASE_URL + url).openStream()).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static void exit(int code) {
        System.exit(code);
    }

    public static int indexOf(ArrayList<EmojiInfo> list, EmojiInfo inf) {
        int i = 0;
        for (EmojiInfo e : list) {
            if (e.code.equals(inf.code) || e.name.equals(inf.name))
                return i;
            i++;
        }
        return -1;
    }

    public static void loading(String text, int loaded, int max) {
        char loading = '━';
        char current = '╺';
        int progress = 40;

        StringBuilder builder = new StringBuilder(progress);
        for (int i = 0; i < progress; i++)
            builder.append(loading);
        if (loaded < max)
            builder.setCharAt(loaded * progress / max, current);

        int percent = loaded * 100 / max;
        System.out.print("\r\u001B[31m" + builder.toString()
                + "    \u001B[34m" + text + " (" + loaded + "/" + max + ") " + percent + "%\u001B[0m");
    }
}
