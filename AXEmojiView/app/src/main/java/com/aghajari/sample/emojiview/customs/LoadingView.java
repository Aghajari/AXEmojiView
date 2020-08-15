package com.aghajari.sample.emojiview.customs;

import android.content.Context;
import android.widget.ProgressBar;

import com.aghajari.emojiview.utils.Utils;
import com.aghajari.emojiview.view.AXEmojiLayout;

public class LoadingView extends AXEmojiLayout {

    ProgressBar progressBar;
    public LoadingView(Context context) {
        super(context);
        progressBar = new ProgressBar(context);
        this.addView(progressBar,new LayoutParams(0,0, Utils.dpToPx(getContext(),44),Utils.dpToPx(getContext(),44)));
        progressBar.setIndeterminate(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ((LayoutParams)progressBar.getLayoutParams()).left = w/2-Utils.dpToPx(getContext(),22);
        ((LayoutParams)progressBar.getLayoutParams()).top = h/2-Utils.dpToPx(getContext(),44);
        this.requestLayout();
    }
}
