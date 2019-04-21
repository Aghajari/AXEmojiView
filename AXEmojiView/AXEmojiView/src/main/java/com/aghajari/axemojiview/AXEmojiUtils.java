package com.aghajari.axemojiview;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.aghajari.axemojiview.emoji.Emoji;
import com.aghajari.axemojiview.utils.EmojiRange;
import com.aghajari.axemojiview.utils.Utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AXEmojiUtils {
  private static final Pattern SPACE_REMOVAL = Pattern.compile("[\\s]");

  /** returns true when the string contains only emojis. Note that whitespace will be filtered out. */
  public static boolean isOnlyEmojis(@Nullable final CharSequence text) {
    if (!TextUtils.isEmpty(text)) {
      final String inputWithoutSpaces = SPACE_REMOVAL.matcher(text).replaceAll(Matcher.quoteReplacement(""));

      return AXEmojiManager.getInstance()
            .getEmojiRepetitivePattern()
            .matcher(inputWithoutSpaces)
            .matches();
    }

    return false;
  }

  /** returns the emojis that were found in the given text */
  @NonNull public static List<EmojiRange> getEmojis(@Nullable final CharSequence text) {
    return AXEmojiManager.getInstance().findAllEmojis(text);
  }

  /** returns the number of all emojis that were found in the given text */
  public static int getEmojisCount(@Nullable final CharSequence text) {
    return getEmojis(text).size();
  }

  public static SpannableStringBuilder replaceWithEmojis(Context context,Paint.FontMetrics fontMetrics, final CharSequence rawText,float emojiSize) {
      final CharSequence text = rawText == null ? "" : rawText;
      final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
      if (AXEmojiManager.isInstalled()) {
        final float defaultEmojiSize = fontMetrics.descent - fontMetrics.ascent;
        AXEmojiManager.getInstance().replaceWithImages(context, spannableStringBuilder, emojiSize, defaultEmojiSize);
      }
      return spannableStringBuilder;
  }

  public static SpannableStringBuilder replaceWithEmojis(Context context, final CharSequence rawText,float emojiSize,float defaultSize) {
    final CharSequence text = rawText == null ? "" : rawText;
    final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
    if (AXEmojiManager.isInstalled()) {
      AXEmojiManager.getInstance().replaceWithImages(context, spannableStringBuilder, emojiSize, defaultSize);
    }
    return spannableStringBuilder;
  }


  public static SpannableStringBuilder replaceWithEmojis(Context context, final CharSequence rawText,float emojiSize) {
    final CharSequence text = rawText == null ? "" : rawText;
    final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
    if (AXEmojiManager.isInstalled()) {
      AXEmojiManager.getInstance().replaceWithImages(context, spannableStringBuilder, Utils.dpToPx(context,emojiSize), Utils.dpToPx(context,emojiSize));
    }
    return spannableStringBuilder;
  }

  public static String getEmojiUnicode(@NonNull final int[] codePoints){
    return new Emoji(codePoints,0).getUnicode();
  }

  public static String getEmojiUnicode(@NonNull final int codePoint){
    return new Emoji(codePoint,0).getUnicode();
  }

  public static void backspace(@NonNull final EditText editText) {
    final KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
    editText.dispatchKeyEvent(event);
  }


  public static void input(@NonNull final EditText editText, @Nullable final Emoji emoji) {
    if (emoji != null) {
      final int start = editText.getSelectionStart();
      final int end = editText.getSelectionEnd();

      if (start < 0) {
        editText.append(emoji.getUnicode());
      } else {
        editText.getText().replace(Math.min(start, end), Math.max(start, end), emoji.getUnicode(), 0, emoji.getUnicode().length());
      }
    }
  }

  public static Emoji getEmojiFromUnicode (String unicode){
    return AXEmojiManager.getInstance().emojiMap.get(unicode);
  }
}
