package com.aghajari.axemojiview.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;

import com.aghajari.axemojiview.emoji.Emoji;
import java.lang.ref.WeakReference;

final class ImageLoadingTask extends AsyncTask<Emoji, Void, Drawable> {
  private final WeakReference<AppCompatImageView> imageViewReference;
  private final WeakReference<Context> contextReference;

  ImageLoadingTask(final AppCompatImageView imageView) {
    imageViewReference = new WeakReference<>(imageView);
    contextReference = new WeakReference<>(imageView.getContext());
  }

  @Override protected Drawable doInBackground(final Emoji... emoji) {
    final Context context = contextReference.get();

    if (context != null && !isCancelled()) {
      return emoji[0].getDrawable(context);
    }

    return null;
  }

  @Override protected void onPostExecute(@Nullable final Drawable drawable) {
    if (!isCancelled() && drawable != null) {
      final AppCompatImageView imageView = imageViewReference.get();

      if (imageView != null) {
        imageView.setImageDrawable(drawable);
      }
    }
  }
}
