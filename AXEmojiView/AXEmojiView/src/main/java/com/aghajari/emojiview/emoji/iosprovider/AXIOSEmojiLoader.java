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


package com.aghajari.emojiview.emoji.iosprovider;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aghajari.emojiview.emoji.EmojiData;
import com.aghajari.emojiview.utils.Utils;

public class AXIOSEmojiLoader {
    private static final String emojiFolderName = "emoji";
    private static HashMap<CharSequence, DrawableInfo> rects = new HashMap<>();
    private static int drawImgSize;
    private static int bigImgSize;
    private static Paint placeholderPaint;
    private static int[] emojiCounts = new int[]{1620, 184, 115, 328, 125, 207, 288, 258};
    private static Bitmap[][] emojiBmp = new Bitmap[8][];
    private static boolean[][] loadingEmoji = new boolean[8][];

    private static Context context;
    private static boolean isTablet = false;
    public static volatile DispatchQueue globalQueue = new DispatchQueue("globalQueue");
    private static Handler uiThread;

    static void init(Context context) {
        AXIOSEmojiLoader.context = context;
        drawImgSize = Utils.dp(context, 20);
        bigImgSize = Utils.dp(context, isTablet ? 40 : 34);
    }

    static {
        uiThread = new Handler(Looper.getMainLooper());
        for (int a = 0; a < emojiBmp.length; a++) {
            emojiBmp[a] = new Bitmap[emojiCounts[a]];
            loadingEmoji[a] = new boolean[emojiCounts[a]];
        }

        for (int j = 0; j < EmojiData.data.length; j++) {
            for (int i = 0; i < EmojiData.data[j].length; i++) {
                rects.put(EmojiData.data[j][i], new DrawableInfo((byte) j, (short) i, i));
            }
        }
        placeholderPaint = new Paint();
        placeholderPaint.setColor(0x00000000);
    }

    /**
     * try to load emoji before showing EmojiView
     */
    public static void preloadEmoji(CharSequence code) {
        final DrawableInfo info = getDrawableInfo(code);
        if (info != null) {
            loadEmoji(info.page, info.page2, null);
        }
    }

    /**
     * try to load emoji before showing EmojiView
     */
    public static void preloadEmoji(String code, EmojiLoaderListener listener) {
        final DrawableInfo info = getDrawableInfo(code);
        if (info != null) {
            loadEmoji(code, info.page, info.page2, listener);
        }
    }

    private static void loadEmoji(final byte page, final short page2, final EmojiDrawable drawable) {
        if (emojiBmp[page][page2] == null) {
            if (loadingEmoji[page][page2]) {
                return;
            }
            loadingEmoji[page][page2] = true;
            globalQueue.postRunnable(new Runnable() {
                @Override
                public void run() {
                    loadEmojiInternal(page, page2);
                    loadingEmoji[page][page2] = false;
                    uiThread.post(new Runnable() {
                        @Override
                        public void run() {
                            if (drawable != null) drawable.invalidateSelf();
                        }
                    });
                }
            });
        }
    }

    public interface EmojiLoaderListener {
        void onEmojiLoaded(AXIOSEmoji emoji);
    }

    private static class ListenerData {
        EmojiLoaderListener listener;
        String code;

        ListenerData(EmojiLoaderListener listener, String code) {
            this.listener = listener;
            this.code = code;
        }
    }

    private static List<ListenerData> loadingListeners = null;

    private static void loadEmoji(final String code, final byte page, final short page2, final EmojiLoaderListener listener) {
        if (emojiBmp[page][page2] == null) {
            if (loadingEmoji[page][page2]) {
                if (listener == null) return;
                if (loadingListeners == null) loadingListeners = new ArrayList<>();
                loadingListeners.add(new ListenerData(listener, code));
                return;
            }
            loadingEmoji[page][page2] = true;
            globalQueue.postRunnable(new Runnable() {
                @Override
                public void run() {
                    loadEmojiInternal(page, page2);
                    loadingEmoji[page][page2] = false;
                    uiThread.post(new Runnable() {
                        @Override
                        public void run() {
                            callListeners(code, listener);
                            if (listener != null) listener.onEmojiLoaded(new AXIOSEmoji(code));
                        }
                    });
                }
            });
        } else {
            if (listener != null) listener.onEmojiLoaded(new AXIOSEmoji(code));
        }
    }

    private static void callListeners(String code, EmojiLoaderListener listener) {
        if (loadingListeners != null && loadingListeners.size() > 0) {
            List<ListenerData> remove = new ArrayList<>();
            for (ListenerData data : loadingListeners) {
                if (data.code.equals(code) && data.listener != null && data.listener != listener) {
                    data.listener.onEmojiLoaded(new AXIOSEmoji(code));
                    remove.add(data);
                }
            }
            if (remove.size() > 0) {
                for (ListenerData removeData : remove) {
                    loadingListeners.remove(removeData);
                }
            }
        }
    }

    private static void loadEmojiInternal(final byte page, final short page2) {
        try {
            int imageResize = 1;
            if (context.getResources().getDisplayMetrics().density <= 1.0f) {
                imageResize = 2;
            }

            Bitmap bitmap = null;
            try {
                InputStream is = context.getAssets().open(emojiFolderName + "/" + String.format(Locale.US, "%d_%d.png", page, page2));
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = false;
                opts.inSampleSize = imageResize;
                bitmap = BitmapFactory.decodeStream(is, null, opts);
                is.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }

            final Bitmap finalBitmap = bitmap;
            emojiBmp[page][page2] = finalBitmap;
        } catch (Throwable x) {
            x.printStackTrace();
        }
    }

    public static String fixEmoji(String emoji) {
        char ch;
        int length = emoji.length();
        for (int a = 0; a < length; a++) {
            ch = emoji.charAt(a);
            if (ch >= 0xD83C && ch <= 0xD83E) {
                if (ch == 0xD83C && a < length - 1) {
                    ch = emoji.charAt(a + 1);
                    if (ch == 0xDE2F || ch == 0xDC04 || ch == 0xDE1A || ch == 0xDD7F) {
                        emoji = emoji.substring(0, a + 2) + "\uFE0F" + emoji.substring(a + 2);
                        length++;
                        a += 2;
                    } else {
                        a++;
                    }
                } else {
                    a++;
                }
            } else if (ch == 0x20E3) {
                return emoji;
            } else if (ch >= 0x203C && ch <= 0x3299) {
                if (EmojiData.emojiToFE0FMap.containsKey(ch)) {
                    emoji = emoji.substring(0, a + 1) + "\uFE0F" + emoji.substring(a + 1);
                    length++;
                    a++;
                }
            }
        }
        return emoji;
    }

    /**
     * @return emoji drawable
     */
    public static @Nullable EmojiDrawable getEmojiDrawable(CharSequence code) {
        DrawableInfo info = getDrawableInfo(code);
        if (info == null) {
            //No drawable for emoji + code
            return null;
        }
        EmojiDrawable ed = new EmojiDrawable(info);
        ed.setBounds(0, 0, drawImgSize, drawImgSize);
        return ed;
    }

    /**
     * @return emoji bitmap or null if emoji hasn't loaded yet (or it's invalid).
     */
    public static @Nullable Bitmap getEmojiBitmap(CharSequence code) {
        DrawableInfo info = getDrawableInfo(code);
        if (info == null) return null;
        return emojiBmp[info.page][info.page2];
    }

    private static DrawableInfo getDrawableInfo(CharSequence code) {
        DrawableInfo info = rects.get(code);
        if (info == null) {
            CharSequence newCode = EmojiData.emojiAliasMap.get(code);
            if (newCode != null) {
                info = AXIOSEmojiLoader.rects.get(newCode);
            }
        }
        return info;
    }

    /**
     * @return false if there is no emoji for this code
     */
    public static boolean isValidEmoji(CharSequence code) {
        DrawableInfo info = rects.get(code);
        if (info == null) {
            CharSequence newCode = EmojiData.emojiAliasMap.get(code);
            if (newCode != null) {
                info = AXIOSEmojiLoader.rects.get(newCode);
            }
        }
        return info != null;
    }

    /**
     * @return fullSize emoji drawable
     */
    public static Drawable getEmojiBigDrawable(String code) {
        EmojiDrawable ed = getEmojiDrawable(code);
        if (ed == null) {
            CharSequence newCode = EmojiData.emojiAliasMap.get(code);
            if (newCode != null) {
                ed = AXIOSEmojiLoader.getEmojiDrawable(newCode);
            }
        }
        if (ed == null) {
            return null;
        }
        ed.setBounds(0, 0, bigImgSize, bigImgSize);
        ed.fullSize = true;
        return ed;
    }

    /**
     * @return emoji drawable with custom bounds
     */
    public static Drawable getEmojiDrawable(String code, int size, boolean fullSize) {
        EmojiDrawable ed = getEmojiDrawable(code);
        if (ed == null) {
            CharSequence newCode = EmojiData.emojiAliasMap.get(code);
            if (newCode != null) {
                ed = AXIOSEmojiLoader.getEmojiDrawable(newCode);
            }
        }
        if (ed == null) {
            return null;
        }
        ed.setBounds(0, 0, size, size);
        ed.fullSize = fullSize;
        return ed;
    }

    private static class EmojiDrawable extends Drawable {
        private DrawableInfo info;
        private boolean fullSize = false;
        private static Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        private static Rect rect = new Rect();

        public EmojiDrawable(DrawableInfo i) {
            info = i;
        }

        public DrawableInfo getDrawableInfo() {
            return info;
        }

        public int getSize() {
            return (fullSize ? bigImgSize : drawImgSize);
        }

        public Rect getDrawRect() {
            Rect original = getBounds();
            int cX = original.centerX(), cY = original.centerY();
            rect.left = cX - getSize() / 2;
            rect.right = cX + getSize() / 2;
            rect.top = cY - getSize() / 2;
            rect.bottom = cY + getSize() / 2;
            return rect;
        }

        @Override
        public void draw(Canvas canvas) {
            /*if (useSystemEmoji) {
                //textPaint.setTextSize(getBounds().width());
                canvas.drawText(EmojiData.data[info.page][info.emojiIndex], getBounds().left, getBounds().bottom, textPaint);
                return;
            }*/
            if (emojiBmp[info.page][info.page2] == null) {
                loadEmoji(info.page, info.page2, this);
                canvas.drawRect(getBounds(), placeholderPaint);
                return;
            }

            Rect b;
            if (fullSize) {
                b = getDrawRect();
            } else {
                b = getBounds();
            }

            //if (!canvas.quickReject(b.left, b.top, b.right, b.bottom, Canvas.EdgeType.AA)) {
            canvas.drawBitmap(emojiBmp[info.page][info.page2], null, b, paint);
            //}
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSPARENT;
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter cf) {

        }
    }

    private static class DrawableInfo {
        public byte page;
        public short page2;
        public int emojiIndex;

        public DrawableInfo(byte p, short p2, int index) {
            page = p;
            page2 = p2;
            emojiIndex = index;
        }
    }

    public static CharSequence replaceEmoji(CharSequence cs, Paint.FontMetrics fontMetrics, int size, boolean createNew) {
        return replaceEmoji(cs, fontMetrics, size, createNew, null);
    }

    public static CharSequence replaceEmoji(CharSequence cs, Paint.FontMetrics fontMetrics, int size, boolean createNew, int[] emojiOnly) {
        if (cs == null || cs.length() == 0) {
            return cs;
        }
        Spannable s;
        if (!createNew && cs instanceof Spannable) {
            s = (Spannable) cs;
        } else {
            s = Spannable.Factory.getInstance().newSpannable(cs.toString());
        }
        long buf = 0;
        int emojiCount = 0;
        char c;
        int startIndex = -1;
        int startLength = 0;
        int previousGoodIndex = 0;
        StringBuilder emojiCode = new StringBuilder(16);
        StringBuilder addionalCode = new StringBuilder(2);
        boolean nextIsSkinTone;
        EmojiDrawable drawable;
        EmojiSpan span;
        int length = cs.length();
        boolean doneEmoji = false;
        int nextValidLength;
        boolean nextValid;
        //s.setSpansCount(emojiCount);

        try {
            for (int i = 0; i < length; i++) {
                c = cs.charAt(i);
                if (c >= 0xD83C && c <= 0xD83E || (buf != 0 && (buf & 0xFFFFFFFF00000000L) == 0 && (buf & 0xFFFF) == 0xD83C && (c >= 0xDDE6 && c <= 0xDDFF))) {
                    if (startIndex == -1) {
                        startIndex = i;
                    }
                    emojiCode.append(c);
                    startLength++;
                    buf <<= 16;
                    buf |= c;
                } else if (emojiCode.length() > 0 && (c == 0x2640 || c == 0x2642 || c == 0x2695)) {
                    emojiCode.append(c);
                    startLength++;
                    buf = 0;
                    doneEmoji = true;
                } else if (buf > 0 && (c & 0xF000) == 0xD000) {
                    emojiCode.append(c);
                    startLength++;
                    buf = 0;
                    doneEmoji = true;
                } else if (c == 0x20E3) {
                    if (i > 0) {
                        char c2 = cs.charAt(previousGoodIndex);
                        if ((c2 >= '0' && c2 <= '9') || c2 == '#' || c2 == '*') {
                            startIndex = previousGoodIndex;
                            startLength = i - previousGoodIndex + 1;
                            emojiCode.append(c2);
                            emojiCode.append(c);
                            doneEmoji = true;
                        }
                    }
                } else if ((c == 0x00A9 || c == 0x00AE || c >= 0x203C && c <= 0x3299) && EmojiData.dataCharsMap.containsKey(c)) {
                    if (startIndex == -1) {
                        startIndex = i;
                    }
                    startLength++;
                    emojiCode.append(c);
                    doneEmoji = true;
                } else if (startIndex != -1) {
                    emojiCode.setLength(0);
                    startIndex = -1;
                    startLength = 0;
                    doneEmoji = false;
                } else if (c != 0xfe0f) {
                    if (emojiOnly != null) {
                        emojiOnly[0] = 0;
                        emojiOnly = null;
                    }
                }
                if (doneEmoji && i + 2 < length) {
                    char next = cs.charAt(i + 1);
                    if (next == 0xD83C) {
                        next = cs.charAt(i + 2);
                        if (next >= 0xDFFB && next <= 0xDFFF) {
                            emojiCode.append(cs.subSequence(i + 1, i + 3));
                            startLength += 2;
                            i += 2;
                        }
                    } else if (emojiCode.length() >= 2 && emojiCode.charAt(0) == 0xD83C && emojiCode.charAt(1) == 0xDFF4 && next == 0xDB40) {
                        i++;
                        while (true) {
                            emojiCode.append(cs.subSequence(i, i + 2));
                            startLength += 2;
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
                                startLength++;
                                doneEmoji = false;
                            }
                        } else if (startIndex != -1 || prevCh == '*' || prevCh >= '1' && prevCh <= '9') {
                            if (c >= 0xFE00 && c <= 0xFE0F) {
                                i++;
                                startLength++;
                            }
                        }
                    }
                }
                if (doneEmoji && i + 2 < length && cs.charAt(i + 1) == 0xD83C) {
                    char next = cs.charAt(i + 2);
                    if (next >= 0xDFFB && next <= 0xDFFF) {
                        emojiCode.append(cs.subSequence(i + 1, i + 3));
                        startLength += 2;
                        i += 2;
                    }
                }
                if (doneEmoji) {
                    if (emojiOnly != null) {
                        emojiOnly[0]++;
                    }
                    CharSequence code = emojiCode.subSequence(0, emojiCode.length());
                    drawable = AXIOSEmojiLoader.getEmojiDrawable(code);
                    if (drawable != null) {
                        span = new EmojiSpan(drawable, DynamicDrawableSpan.ALIGN_BOTTOM, size, fontMetrics);
                        s.setSpan(span, startIndex, startIndex + startLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        emojiCount++;
                    }
                    startLength = 0;
                    startIndex = -1;
                    emojiCode.setLength(0);
                    doneEmoji = false;
                }
                if ((Build.VERSION.SDK_INT < 23 || Build.VERSION.SDK_INT >= 29) && emojiCount >= 50) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return cs;
        }
        if (emojiOnly != null && emojiCode.length() != 0) {
            emojiOnly[0] = 0;
        }
        return s;
    }

    public static List<SpanLocation> replaceEmoji2(CharSequence cs, Paint.FontMetrics fontMetrics, int size, boolean createNew, int[] emojiOnly) {
        if (cs == null || cs.length() == 0) {
            return new ArrayList<>();
        }
        Spannable s;
        if (!createNew && cs instanceof Spannable) {
            s = (Spannable) cs;
        } else {
            s = Spannable.Factory.getInstance().newSpannable(cs.toString());
        }
        long buf = 0;
        int emojiCount = 0;
        char c;
        int startIndex = -1;
        int startLength = 0;
        int previousGoodIndex = 0;
        StringBuilder emojiCode = new StringBuilder(16);
        StringBuilder addionalCode = new StringBuilder(2);
        boolean nextIsSkinTone;
        EmojiDrawable drawable;
        EmojiSpan span;
        int length = cs.length();
        boolean doneEmoji = false;
        int nextValidLength;
        boolean nextValid;
        List<SpanLocation> spans = new ArrayList<>();
        //s.setSpansCount(emojiCount);

        try {
            for (int i = 0; i < length; i++) {
                c = cs.charAt(i);
                if (c >= 0xD83C && c <= 0xD83E || (buf != 0 && (buf & 0xFFFFFFFF00000000L) == 0 && (buf & 0xFFFF) == 0xD83C && (c >= 0xDDE6 && c <= 0xDDFF))) {
                    if (startIndex == -1) {
                        startIndex = i;
                    }
                    emojiCode.append(c);
                    startLength++;
                    buf <<= 16;
                    buf |= c;
                } else if (emojiCode.length() > 0 && (c == 0x2640 || c == 0x2642 || c == 0x2695)) {
                    emojiCode.append(c);
                    startLength++;
                    buf = 0;
                    doneEmoji = true;
                } else if (buf > 0 && (c & 0xF000) == 0xD000) {
                    emojiCode.append(c);
                    startLength++;
                    buf = 0;
                    doneEmoji = true;
                } else if (c == 0x20E3) {
                    if (i > 0) {
                        char c2 = cs.charAt(previousGoodIndex);
                        if ((c2 >= '0' && c2 <= '9') || c2 == '#' || c2 == '*') {
                            startIndex = previousGoodIndex;
                            startLength = i - previousGoodIndex + 1;
                            emojiCode.append(c2);
                            emojiCode.append(c);
                            doneEmoji = true;
                        }
                    }
                } else if ((c == 0x00A9 || c == 0x00AE || c >= 0x203C && c <= 0x3299) && EmojiData.dataCharsMap.containsKey(c)) {
                    if (startIndex == -1) {
                        startIndex = i;
                    }
                    startLength++;
                    emojiCode.append(c);
                    doneEmoji = true;
                } else if (startIndex != -1) {
                    emojiCode.setLength(0);
                    startIndex = -1;
                    startLength = 0;
                    doneEmoji = false;
                } else if (c != 0xfe0f) {
                    if (emojiOnly != null) {
                        emojiOnly[0] = 0;
                        emojiOnly = null;
                    }
                }
                if (doneEmoji && i + 2 < length) {
                    char next = cs.charAt(i + 1);
                    if (next == 0xD83C) {
                        next = cs.charAt(i + 2);
                        if (next >= 0xDFFB && next <= 0xDFFF) {
                            emojiCode.append(cs.subSequence(i + 1, i + 3));
                            startLength += 2;
                            i += 2;
                        }
                    } else if (emojiCode.length() >= 2 && emojiCode.charAt(0) == 0xD83C && emojiCode.charAt(1) == 0xDFF4 && next == 0xDB40) {
                        i++;
                        while (true) {
                            emojiCode.append(cs.subSequence(i, i + 2));
                            startLength += 2;
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
                                startLength++;
                                doneEmoji = false;
                            }
                        } else if (startIndex != -1 || prevCh == '*' || prevCh >= '1' && prevCh <= '9') {
                            if (c >= 0xFE00 && c <= 0xFE0F) {
                                i++;
                                startLength++;
                            }
                        }
                    }
                }
                if (doneEmoji && i + 2 < length && cs.charAt(i + 1) == 0xD83C) {
                    char next = cs.charAt(i + 2);
                    if (next >= 0xDFFB && next <= 0xDFFF) {
                        emojiCode.append(cs.subSequence(i + 1, i + 3));
                        startLength += 2;
                        i += 2;
                    }
                }
                if (doneEmoji) {
                    if (emojiOnly != null) {
                        emojiOnly[0]++;
                    }
                    CharSequence code = emojiCode.subSequence(0, emojiCode.length());
                    drawable = AXIOSEmojiLoader.getEmojiDrawable(code);
                    if (drawable != null) {
                        span = new EmojiSpan(drawable, DynamicDrawableSpan.ALIGN_BOTTOM, size, fontMetrics);
                        //s.setSpan(span, startIndex, startIndex + startLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spans.add(new SpanLocation(span, startIndex, startIndex + startLength));
                        emojiCount++;
                    }
                    startLength = 0;
                    startIndex = -1;
                    emojiCode.setLength(0);
                    doneEmoji = false;
                }
                if ((Build.VERSION.SDK_INT < 23 || Build.VERSION.SDK_INT >= 29) && emojiCount >= 50) {
                    break;
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return new ArrayList<>();
        }
        if (emojiOnly != null && emojiCode.length() != 0) {
            emojiOnly[0] = 0;
        }
        return spans;
    }

    static class SpanLocation {
        public EmojiSpan span;
        public int start;
        public int end;

        public SpanLocation(EmojiSpan s, int start, int end) {
            span = s;
            this.start = start;
            this.end = end;
        }
    }

    static class EmojiSpan extends ImageSpan {
        private Paint.FontMetrics fontMetrics;
        private int size = Utils.dp(context, 20);

        public EmojiSpan(EmojiDrawable d, int verticalAlignment, int s, Paint.FontMetrics original) {
            super(d, verticalAlignment);
            fontMetrics = original;
            if (original != null) {
                size = (int) (Math.abs(fontMetrics.descent) + Math.abs(fontMetrics.ascent));
                if (size == 0) {
                    size = Utils.dp(context, 20);
                }
            }
            if (s > 0) {
                size = s;
            }
        }

        public void replaceFontMetrics(Paint.FontMetrics newMetrics, int newSize) {
            fontMetrics = newMetrics;
            size = newSize;
        }

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            if (fm == null) {
                fm = new Paint.FontMetricsInt();
            }

            if (fontMetrics == null) {
                int sz = super.getSize(paint, text, start, end, fm);

                int offset = Utils.dp(context, 8);
                int w = Utils.dp(context, 10);
                fm.top = -w - offset;
                fm.bottom = w - offset;
                fm.ascent = -w - offset;
                fm.leading = 0;
                fm.descent = w - offset;

                return sz;
            } else {
                if (fm != null) {
                    fm.ascent = (int) fontMetrics.ascent;
                    fm.descent = (int) fontMetrics.descent;

                    fm.top = (int) fontMetrics.top;
                    fm.bottom = (int) fontMetrics.bottom;
                }
                if (getDrawable() != null) {
                    getDrawable().setBounds(0, 0, size, size);
                }
                return size;
            }
        }
    }
}