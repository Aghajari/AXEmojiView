package com.aghajari;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class EmojiUtils {

    protected static final char[] dataChars = {
            0x262E, 0x271D, 0x262A, 0x2638, 0x2721, 0x262F, 0x2626, 0x26CE, 0x2648, 0x2649,
            0x264A, 0x264B, 0x264C, 0x264D, 0x264E, 0x264F, 0x2650, 0x2651, 0x2652, 0x2653,
            0x269B, 0x2622, 0x2623, 0x2734, 0x3299, 0x3297, 0x26D4, 0x274C, 0x2B55, 0x2668,
            0x2757, 0x2755, 0x2753, 0x2754, 0x203C, 0x2049, 0x269C, 0x303D, 0x26A0, 0x267B,
            0x2747, 0x2733, 0x274E, 0x2705, 0x27BF, 0x24C2, 0x267F, 0x25B6, 0x23F8, 0x23EF,
            0x23F9, 0x23FA, 0x23ED, 0x23EE, 0x23E9, 0x23EA, 0x25C0, 0x23EB, 0x23EC, 0x27A1,
            0x2B05, 0x2B06, 0x2B07, 0x2197, 0x2198, 0x2199, 0x2196, 0x2195, 0x2194, 0x21AA,
            0x21A9, 0x2934, 0x2935, 0x2139, 0x3030, 0x27B0, 0x2714, 0x2795, 0x2796, 0x2797,
            0x2716, 0x00A9, 0x00AE, 0x2122, 0x2611, 0x26AA, 0x26AB, 0x25AA, 0x25AB, 0x2B1B,
            0x2B1C, 0x25FC, 0x25FB, 0x25FE, 0x25FD, 0x2660, 0x2663, 0x2665, 0x2666, 0x263A,
            0x2639, 0x270A, 0x270C, 0x270B, 0x261D, 0x270D, 0x26D1, 0x2764, 0x2763, 0x2615,
            0x26BD, 0x26BE, 0x26F3, 0x26F7, 0x26F8, 0x26F9, 0x231A, 0x2328, 0x260E, 0x23F1,
            0x23F2, 0x23F0, 0x23F3, 0x231B, 0x2696, 0x2692, 0x26CF, 0x2699, 0x26D3, 0x2694,
            0x2620, 0x26B0, 0x26B1, 0x2697, 0x26F1, 0x2709, 0x2702, 0x2712, 0x270F, 0x2708,
            0x26F5, 0x26F4, 0x2693, 0x26FD, 0x26F2, 0x26F0, 0x26FA, 0x26EA, 0x26E9, 0x2618,
            0x2B50, 0x2728, 0x2604, 0x2600, 0x26C5, 0x2601, 0x26C8, 0x26A1, 0x2744, 0x2603,
            0x26C4, 0x2602, 0x2614, 0x26A7, 0x23CF, 0x267E, 0x265F
    };

    protected static final String[] ignoreEmoji = {
            "🇦", "🇧", "🇨", "🇩", "🇪", "🇫", "🇬", "🇭", "🇮",
            "🇯", "🇰", "🇱", "🇲", "🇳", "🇴", "🇵", "🇶", "🇷",
            "🇸", "🇹", "🇺", "🇻", "🇼", "🇽", "🇾", "🇿",
            "♀️", "♂️", "⚕️", "🏻", "🏼", "🏽", "🏾", "🏿",
    };

    protected static final HashMap<Character, Boolean> dataCharsMap = new HashMap<>(dataChars.length);
    protected static final List<String> ignoreEmojiList = Arrays.asList(ignoreEmoji);

    static {
        for (char dataChar : dataChars) {
            dataCharsMap.put(dataChar, true);
        }
    }

    public static boolean shouldIgnoreEmoji(String code){
        return code == null || code.trim().isEmpty() || ignoreEmojiList.contains(code);
    }

    public static String replaceEmoji(String cs) {
        return internalReplaceEmoji(cs)
                .replace("#️⃣", "#⃣")
                .replace("0️⃣", "0⃣");
    }

    public static String internalReplaceEmoji(String cs) {
        if (cs == null || cs.length() == 0) {
            return cs;
        }

        long buf = 0;
        char c;
        int startIndex = -1;
        int previousGoodIndex = 0;
        StringBuilder emojiCode = new StringBuilder(16);
        int length = cs.length();
        boolean doneEmoji = false;

        try {
            for (int i = 0; i < length; i++) {
                c = cs.charAt(i);
                if (c >= 0xD83C && c <= 0xD83E || (buf != 0 && (buf & 0xFFFFFFFF00000000L) == 0 && (buf & 0xFFFF) == 0xD83C && (c >= 0xDDE6 && c <= 0xDDFF))) {
                    if (startIndex == -1) {
                        startIndex = i;
                    }
                    emojiCode.append(c);
                    buf <<= 16;
                    buf |= c;
                } else if (emojiCode.length() > 0 && (c == 0x2640 || c == 0x2642 || c == 0x2695)) {
                    emojiCode.append(c);
                    buf = 0;
                    doneEmoji = true;
                } else if (buf > 0 && (c & 0xF000) == 0xD000) {
                    emojiCode.append(c);
                    buf = 0;
                    doneEmoji = true;
                } else if (c == 0x20E3) {
                    if (i > 0) {
                        char c2 = cs.charAt(previousGoodIndex);
                        if ((c2 >= '0' && c2 <= '9') || c2 == '#' || c2 == '*') {
                            startIndex = previousGoodIndex;
                            emojiCode.append(c2);
                            emojiCode.append(c);
                            doneEmoji = true;
                        }
                    }
                } else if ((c == 0x00A9 || c == 0x00AE || c >= 0x203C && c <= 0x3299)
                        && dataCharsMap.containsKey(c)) {
                    if (startIndex == -1) {
                        startIndex = i;
                    }
                    emojiCode.append(c);
                    doneEmoji = true;
                } else if (startIndex != -1) {
                    emojiCode.setLength(0);
                    startIndex = -1;
                    doneEmoji = false;
                }
                if (doneEmoji && i + 2 < length) {
                    char next = cs.charAt(i + 1);
                    if (next == 0xD83C) {
                        next = cs.charAt(i + 2);
                        if (next >= 0xDFFB && next <= 0xDFFF) {
                            emojiCode.append(cs.subSequence(i + 1, i + 3));
                            i += 2;
                        }
                    } else if (emojiCode.length() >= 2 && emojiCode.charAt(0) == 0xD83C && emojiCode.charAt(1) == 0xDFF4 && next == 0xDB40) {
                        i++;
                        while (true) {
                            emojiCode.append(cs.subSequence(i, i + 2));
                            i += 2;
                            if (i >= cs.length() || cs.charAt(i) != 0xDB40) {
                                i--;
                                break;
                            }
                        }

                    }
                }
                previousGoodIndex = i;
                char prevCh = c;
                for (int a = 0; a < 3; a++) {
                    if (i + 1 < length) {
                        c = cs.charAt(i + 1);
                        if (a == 1) {
                            if (c == 0x200D && emojiCode.length() > 0) {
                                emojiCode.append(c);
                                i++;
                                doneEmoji = false;
                            }
                        } else if (startIndex != -1 || prevCh == '*' || prevCh >= '1' && prevCh <= '9') {
                            if (c >= 0xFE00 && c <= 0xFE0F) {
                                i++;
                            }
                        }
                    }
                }
                if (doneEmoji && i + 2 < length && cs.charAt(i + 1) == 0xD83C) {
                    char next = cs.charAt(i + 2);
                    if (next >= 0xDFFB && next <= 0xDFFF) {
                        emojiCode.append(cs.subSequence(i + 1, i + 3));
                        i += 2;
                    }
                }
                if (doneEmoji) {
                    return emojiCode.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return cs;
        }
        return cs;
    }

    public static String getBaseEmoji(String code) {
        return code.replace("\uD83C\uDFFB", "")
                .replace("\uD83C\uDFFC", "")
                .replace("\uD83C\uDFFD", "")
                .replace("\uD83C\uDFFE", "")
                .replace("\uD83C\uDFFF", "");
    }

    public static String addColorToCode(String code, int colorIndex) {
        String color = getColorCode(colorIndex);
        if (color.isEmpty())
            return code;

        code = getBaseEmoji(code);
        String end = null;
        int length = code.length();
        if (length > 2 && code.charAt(code.length() - 2) == '\u200D') {
            end = code.substring(code.length() - 2);
            code = code.substring(0, code.length() - 2);
        } else if (length > 3 && code.charAt(code.length() - 3) == '\u200D') {
            end = code.substring(code.length() - 3);
            code = code.substring(0, code.length() - 3);
        }
        code += color;
        if (end != null) {
            code += end;
        }
        return code;
    }

    public static String getColorCode(int index) {
        switch (index) {
            case 1:
                return "\uD83C\uDFFB";
            case 2:
                return "\uD83C\uDFFC";
            case 3:
                return "\uD83C\uDFFD";
            case 4:
                return "\uD83C\uDFFE";
            case 5:
                return "\uD83C\uDFFF";
        }
        return "";
    }
}
