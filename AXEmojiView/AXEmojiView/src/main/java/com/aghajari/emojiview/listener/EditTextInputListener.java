package com.aghajari.emojiview.listener;

import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aghajari.emojiview.emoji.Emoji;

public interface EditTextInputListener {
		void input(@NonNull final EditText editText, @Nullable final Emoji emoji);
}
