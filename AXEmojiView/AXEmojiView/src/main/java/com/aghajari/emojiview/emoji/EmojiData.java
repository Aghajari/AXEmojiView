/*
 * Copyright (C) 2020 - Amir Hossein Aghajari
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


package com.aghajari.emojiview.emoji;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.IntRange;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;

public abstract class EmojiData {

    protected static final char[] emojiToFE0F = {
            0x2B50, 0x2600, 0x26C5, 0x2601, 0x26A1, 0x2744, 0x26C4, 0x2614, 0x2708, 0x26F5,
            0x2693, 0x26FD, 0x26F2, 0x26FA, 0x26EA, 0x2615, 0x26BD, 0x26BE, 0x26F3, 0x231A,
            0x260E, 0x231B, 0x2709, 0x2702, 0x2712, 0x270F, 0x2648, 0x2649, 0x264A, 0x264B,
            0x264C, 0x264D, 0x264E, 0x264F, 0x2650, 0x2651, 0x2652, 0x2653, 0x2734, 0x3299,
            0x3297, 0x26D4, 0x2B55, 0x2668, 0x2757, 0x203C, 0x2049, 0x303D, 0x26A0, 0x267B,
            0x2747, 0x2733, 0x24C2, 0x267F, 0x25B6, 0x25C0, 0x27A1, 0x2B05, 0x2B06, 0x2B07,
            0x2197, 0x2198, 0x2199, 0x2196, 0x2195, 0x2194, 0x21AA, 0x21A9, 0x2934, 0x2935,
            0x2139, 0x2714, 0x2716, 0x2611, 0x26AA, 0x26AB, 0x25AA, 0x25AB, 0x2B1B, 0x2B1C,
            0x25FC, 0x25FB, 0x25FE, 0x25FD, 0x2660, 0x2663, 0x2665, 0x2666, 0x263A, 0x2639,
            0x270C, 0x261D, 0x2764, 0x2603, 0x23CF
    };

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

    protected static final String[] aliasOld = new String[]{
            "👱", "👱🏻", "👱🏼", "👱🏽", "👱🏾", "👱🏿",
            "👳", "👳🏻", "👳🏼", "👳🏽", "👳🏾", "👳🏿",
            "👷", "👷🏻", "👷🏼", "👷🏽", "👷🏾", "👷🏿",
            "👮", "👮🏻", "👮🏼", "👮🏽", "👮🏾", "👮🏿",
            "💂", "💂🏻", "💂🏼", "💂🏽", "💂🏾", "💂🏿",
            "🕵", "🕵🏻", "🕵🏼", "🕵🏽", "🕵🏾", "🕵🏿",
            "🙇", "🙇🏻", "🙇🏼", "🙇🏽", "🙇🏾", "🙇🏿",
            "💁", "💁🏻", "💁🏼", "💁🏽", "💁🏾", "💁🏿",
            "🙅", "🙅🏻", "🙅🏼", "🙅🏽", "🙅🏾", "🙅🏿",
            "🙆", "🙆🏻", "🙆🏼", "🙆🏽", "🙆🏾", "🙆🏿",
            "🙋", "🙋🏻", "🙋🏼", "🙋🏽", "🙋🏾", "🙋🏿",
            "🙎", "🙎🏻", "🙎🏼", "🙎🏽", "🙎🏾", "🙎🏿",
            "🙍", "🙍🏻", "🙍🏼", "🙍🏽", "🙍🏾", "🙍🏿",
            "💇", "💇🏻", "💇🏼", "💇🏽", "💇🏾", "💇🏿",
            "💆", "💆🏻", "💆🏼", "💆🏽", "💆🏾", "💆🏿",
            "🏃", "🏃🏻", "🏃🏼", "🏃🏽", "🏃🏾", "🏃🏿",
            "🏋", "🏋🏻", "🏋🏼", "🏋🏽", "🏋🏾", "🏋🏿",
            "⛹", "⛹🏻", "⛹🏼", "⛹🏽", "⛹🏾", "⛹🏿",
            "🏌", "🏌🏻", "🏌🏼", "🏌🏽", "🏌🏾", "🏌🏿",
            "🏄", "🏄🏻", "🏄🏼", "🏄🏽", "🏄🏾", "🏄🏿",
            "🏊", "🏊🏻", "🏊🏼", "🏊🏽", "🏊🏾", "🏊🏿",
            "🚣", "🚣🏻", "🚣🏼", "🚣🏽", "🚣🏾", "🚣🏿",
            "🚴", "🚴🏻", "🚴🏼", "🚴🏽", "🚴🏾", "🚴🏿",
            "🚵", "🚵🏻", "🚵🏼", "🚵🏽", "🚵🏾", "🚵🏿",

            "🦸", "🦸🏻", "🦸🏼", "🦸🏽", "🦸🏾", "🦸🏿",
            "🦹", "🦹🏻", "🦹🏼", "🦹🏽", "🦹🏾", "🦹🏿",
            "🧙", "🧙🏻", "🧙🏼", "🧙🏽", "🧙🏾", "🧙🏿",
            "🧝", "🧝🏻", "🧝🏼", "🧝🏽", "🧝🏾", "🧝🏿",
            "🧛", "🧛🏻", "🧛🏼", "🧛🏽", "🧛🏾", "🧛🏿",
            "🧟",
            "🧞",
            "🧜", "🧜🏻", "🧜🏼", "🧜🏽", "🧜🏾", "🧜🏿",
            "🧚", "🧚🏻", "🧚🏼", "🧚🏽", "🧚🏾", "🧚🏿",
            "🤦", "🤦🏻", "🤦🏼", "🤦🏽", "🤦🏾", "🤦🏿",
            "🤷", "🤷🏻", "🤷🏼", "🤷🏽", "🤷🏾", "🤷🏿",
            "🧖", "🧖🏻", "🧖🏼", "🧖🏽", "🧖🏾", "🧖🏿",
            "👯",
            "🚶", "🚶🏻", "🚶🏼", "🚶🏽", "🚶🏾", "🚶🏿",
            "🤼",
            "🤸", "🤸🏻", "🤸🏼", "🤸🏽", "🤸🏾", "🤸🏿",
            "🤾", "🤾🏻", "🤾🏼", "🤾🏽", "🤾🏾", "🤾🏿",
            "🧘", "🧘🏻", "🧘🏼", "🧘🏽", "🧘🏾", "🧘🏿",
            "🤽", "🤽🏻", "🤽🏼", "🤽🏽", "🤽🏾", "🤽🏿",
            "🧗", "🧗🏻", "🧗🏼", "🧗🏽", "🧗🏾", "🧗🏿",
            "🤹", "🤹🏻", "🤹🏼", "🤹🏽", "🤹🏾", "🤹🏿",
            "\uD83D\uDC91"};

    protected static final String[] aliasNew = new String[]{
            "👱‍♂", "👱🏻‍♂", "👱🏼‍♂", "👱🏽‍♂", "👱🏾‍♂", "👱🏿‍♂",
            "👳‍♂", "👳🏻‍♂", "👳🏼‍♂", "👳🏽‍♂", "👳🏾‍♂", "👳🏿‍♂",
            "👷‍♂", "👷🏻‍♂", "👷🏼‍♂", "👷🏽‍♂", "👷🏾‍♂", "👷🏿‍♂",
            "👮‍♂", "👮🏻‍♂", "👮🏼‍♂", "👮🏽‍♂", "👮🏾‍♂", "👮🏿‍♂",
            "💂‍♂", "💂🏻‍♂", "💂🏼‍♂", "💂🏽‍♂", "💂🏾‍♂", "💂🏿‍♂",
            "🕵‍♂", "🕵🏻‍♂", "🕵🏼‍♂", "🕵🏽‍♂", "🕵🏾‍♂", "🕵🏿‍♂",
            "🙇‍♂", "🙇🏻‍♂", "🙇🏼‍♂", "🙇🏽‍♂", "🙇🏾‍♂", "🙇🏿‍♂",
            "💁‍♀", "💁🏻‍♀", "💁🏼‍♀", "💁🏽‍♀", "💁🏾‍♀", "💁🏿‍♀",
            "🙅‍♀", "🙅🏻‍♀", "🙅🏼‍♀", "🙅🏽‍♀", "🙅🏾‍♀", "🙅🏿‍♀",
            "🙆‍♀", "🙆🏻‍♀", "🙆🏼‍♀", "🙆🏽‍♀", "🙆🏾‍♀", "🙆🏿‍♀",
            "🙋‍♀", "🙋🏻‍♀", "🙋🏼‍♀", "🙋🏽‍♀", "🙋🏾‍♀", "🙋🏿‍♀",
            "🙎‍♀", "🙎🏻‍♀", "🙎🏼‍♀", "🙎🏽‍♀", "🙎🏾‍♀", "🙎🏿‍♀",
            "🙍‍♀", "🙍🏻‍♀", "🙍🏼‍♀", "🙍🏽‍♀", "🙍🏾‍♀", "🙍🏿‍♀",
            "💇‍♀", "💇🏻‍♀", "💇🏼‍♀", "💇🏽‍♀", "💇🏾‍♀", "💇🏿‍♀",
            "💆‍♀", "💆🏻‍♀", "💆🏼‍♀", "💆🏽‍♀", "💆🏾‍♀", "💆🏿‍♀",
            "🏃‍♂", "🏃🏻‍♂", "🏃🏼‍♂", "🏃🏽‍♂", "🏃🏾‍♂", "🏃🏿‍♂",
            "🏋‍♂", "🏋🏻‍♂", "🏋🏼‍♂", "🏋🏽‍♂", "🏋🏾‍♂", "🏋🏿‍♂",
            "⛹‍♂", "⛹🏻‍♂", "⛹🏼‍♂", "⛹🏽‍♂", "⛹🏾‍♂", "⛹🏿‍♂",
            "🏌‍♂", "🏌🏻‍♂", "🏌🏼‍♂", "🏌🏽‍♂", "🏌🏾‍♂", "🏌🏿‍♂",
            "🏄‍♂", "🏄🏻‍♂", "🏄🏼‍♂", "🏄🏽‍♂", "🏄🏾‍♂", "🏄🏿‍♂",
            "🏊‍♂", "🏊🏻‍♂", "🏊🏼‍♂", "🏊🏽‍♂", "🏊🏾‍♂", "🏊🏿‍♂",
            "🚣‍♂", "🚣🏻‍♂", "🚣🏼‍♂", "🚣🏽‍♂", "🚣🏾‍♂", "🚣🏿‍♂",
            "🚴‍♂", "🚴🏻‍♂", "🚴🏼‍♂", "🚴🏽‍♂", "🚴🏾‍♂", "🚴🏿‍♂",
            "🚵‍♂", "🚵🏻‍♂", "🚵🏼‍♂", "🚵🏽‍♂", "🚵🏾‍♂", "🚵🏿‍♂",

            "🦸‍♀", "🦸🏻‍♀", "🦸🏼‍♀", "🦸🏽‍♀", "🦸🏾‍♀", "🦸🏿‍♀",
            "🦹‍♀", "🦹🏻‍♀", "🦹🏼‍♀", "🦹🏽‍♀", "🦹🏾‍♀", "🦹🏿‍♀",
            "🧙‍♀", "🧙🏻‍♀", "🧙🏼‍♀", "🧙🏽‍♀", "🧙🏾‍♀", "🧙🏿‍♀",
            "🧝‍♂", "🧝🏻‍♂", "🧝🏼‍♂", "🧝🏽‍♂", "🧝🏾‍♂", "🧝🏿‍♂",
            "🧛‍♂", "🧛🏻‍♂", "🧛🏼‍♂", "🧛🏽‍♂", "🧛🏾‍♂", "🧛🏿‍♂",
            "🧟‍♂",
            "🧞‍♂",
            "🧜‍♂", "🧜🏻‍♂", "🧜🏼‍♂", "🧜🏽‍♂", "🧜🏾‍♂", "🧜🏿‍♂",
            "🧚‍♀", "🧚🏻‍♀", "🧚🏼‍♀", "🧚🏽‍♀", "🧚🏾‍♀", "🧚🏿‍♀",
            "🤦‍♂", "🤦🏻‍♂", "🤦🏼‍♂", "🤦🏽‍♂", "🤦🏾‍♂", "🤦🏿‍♂",
            "🤷‍♀", "🤷🏻‍♀", "🤷🏼‍♀", "🤷🏽‍♀", "🤷🏾‍♀", "🤷🏿‍♀",
            "🧖‍♂", "🧖🏻‍♂", "🧖🏼‍♂", "🧖🏽‍♂", "🧖🏾‍♂", "🧖🏿‍♂",
            "👯‍♀",
            "🚶‍♂", "🚶🏻‍♂", "🚶🏼‍♂", "🚶🏽‍♂", "🚶🏾‍♂", "🚶🏿‍♂",
            "🤼‍♀",
            "🤸‍♂", "🤸🏻‍♂", "🤸🏼‍♂", "🤸🏽‍♂", "🤸🏾‍♂", "🤸🏿‍♂",
            "🤾‍♀", "🤾🏻‍♀", "🤾🏼‍♀", "🤾🏽‍♀", "🤾🏾‍♀", "🤾🏿‍♀",
            "🧘‍♀", "🧘🏻‍♀", "🧘🏼‍♀", "🧘🏽‍♀", "🧘🏾‍♀", "🧘🏿‍♀",
            "🤽‍♂", "🤽🏻‍♂", "🤽🏼‍♂", "🤽🏽‍♂", "🤽🏾‍♂", "🤽🏿‍♂",
            "🧗‍♂", "🧗🏻‍♂", "🧗🏼‍♂", "🧗🏽‍♂", "🧗🏾‍♂", "🧗🏿‍♂",
            "🤹‍♂", "🤹🏻‍♂", "🤹🏼‍♂", "🤹🏽‍♂", "🤹🏾‍♂", "🤹🏿‍♂",
            "👩‍❤‍👨"};

    protected static final HashMap<Character, Boolean> emojiToFE0FMap = new HashMap<>(emojiToFE0F.length);
    protected static final HashMap<Character, Boolean> dataCharsMap = new HashMap<>(dataChars.length);
    protected static final HashMap<CharSequence, CharSequence> emojiAliasMap = new HashMap<>(aliasNew.length);

    static {
        for (int a = 0; a < emojiToFE0F.length; a++) {
            emojiToFE0FMap.put(emojiToFE0F[a], true);
        }
        for (int a = 0; a < dataChars.length; a++) {
            dataCharsMap.put(dataChars[a], true);
        }
        for (int a = 0; a < aliasNew.length; a++) {
            emojiAliasMap.put(aliasOld[a], aliasNew[a]);
        }
    }

    public HashMap<Character, Boolean> getDataCharsMap() {
        return dataCharsMap;
    }

    public HashMap<Character, Boolean> getEmojiToFE0FMap() {
        return emojiToFE0FMap;
    }

    public HashMap<CharSequence, CharSequence> getEmojiAliasMap(){
        return emojiAliasMap;
    }

    public Bitmap loadEmoji(Context context, int category, int index, int imageResize) {
        Bitmap bitmap = null;
        try {
            InputStream is = context.getAssets().open(getEmojiFolderNameInAssets() + "/" + String.format(Locale.US, "%d_%d.png", category, index));
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = false;
            opts.inSampleSize = imageResize;
            bitmap = BitmapFactory.decodeStream(is, null, opts);
            is.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public String getEmojiFolderNameInAssets(){
        return "emoji";
    }

    public abstract String[][] getData();
    public abstract String[][] getDataColored();
    public abstract boolean isColoredEmoji(String code);

    public String[] getEmojiVariants(String code){
        String[] variants = new String[5];
        for (int i = 0; i < 5; i++) {
            String color = "";
            switch (i) {
                case 0:
                    color = "\uD83C\uDFFB";
                    break;
                case 1:
                    color = "\uD83C\uDFFC";
                    break;
                case 2:
                    color = "\uD83C\uDFFD";
                    break;
                case 3:
                    color = "\uD83C\uDFFE";
                    break;
                case 4:
                    color = "\uD83C\uDFFF";
                    break;
            }
            variants[i] = EmojiData.addColorToCode(code, color);
        }
        return variants;
    }

    public int getCategoriesLength(){
        return 8;
    }

    public String getTitle(int category){
        switch (category) {
            case 0:
                return "Smileys and people";
            case 1:
                return "Animals and nature";
            case 2:
                return "Food and drink";
            case 3:
                return "Activity";
            case 4:
                return "Travel and places";
            case 5:
                return "Objects";
            case 6:
                return "Symbols";
            case 7:
                return "Flags";
            default:
                return "";
        }
    }

    public abstract int getEmojiCount(int category);

    public static boolean isHeartEmoji(String emoji) {
        return "❤".equals(emoji) || "🧡".equals(emoji) || "💛".equals(emoji) || "💚".equals(emoji) || "💙".equals(emoji) || "💜".equals(emoji) || "🖤".equals(emoji) || "🤍".equals(emoji) || "🤎".equals(emoji);
    }

    public static String[] getHeartEmojis(){
        return new String[]{"❤" , "🧡" , "💛" , "💚" , "💙" , "💜" , "🖤" , "🤍" , "🤎" , "♥" , "💔" , "❣" , "💕" , "💞" , "💓" , "💗" , "💖" , "💘" ,"💝"};
    }

    public static boolean isPeachEmoji(String emoji) {
        return "\uD83C\uDF51".equals(emoji);
    }

    public static String getEmojiColor(String code) {
        String color = null;

        String toCheck = code.replace("\uD83C\uDFFB", "");
        if (!toCheck.equals(code)) {
            color = "\uD83C\uDFFB";
        }
        if (color == null) {
            toCheck = code.replace("\uD83C\uDFFC", "");
            if (!toCheck.equals(code)) {
                color = "\uD83C\uDFFC";
            }
        }
        if (color == null) {
            toCheck = code.replace("\uD83C\uDFFD", "");
            if (!toCheck.equals(code)) {
                color = "\uD83C\uDFFD";
            }
        }
        if (color == null) {
            toCheck = code.replace("\uD83C\uDFFE", "");
            if (!toCheck.equals(code)) {
                color = "\uD83C\uDFFE";
            }
        }
        if (color == null) {
            toCheck = code.replace("\uD83C\uDFFF", "");
            if (!toCheck.equals(code)) {
                color = "\uD83C\uDFFF";
            }
        }
        return color;
    }

    public static String getBaseEmoji(String code) {
        return code.replace("\uD83C\uDFFB", "")
                .replace("\uD83C\uDFFC", "")
                .replace("\uD83C\uDFFD", "")
                .replace("\uD83C\uDFFE", "")
                .replace("\uD83C\uDFFF", "");
    }

    public static String addColorToCode(String code, String color) {
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

    public static String getColorCode (@IntRange(from = 1,to = 5) int index){
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
