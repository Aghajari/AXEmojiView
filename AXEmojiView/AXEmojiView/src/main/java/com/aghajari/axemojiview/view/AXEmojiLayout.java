package com.aghajari.axemojiview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class AXEmojiLayout extends  AXEmojiBase {
    public AXEmojiLayout(Context context) {
        super(context);
    }

    public AXEmojiLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AXEmojiLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    static boolean disableAccessibility = false;


    protected void onLayout(boolean changed, int l, int t, int r, int b) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() != 8) {
                    if ((child.getLayoutParams() instanceof LayoutParams)) {
                        LayoutParams lp =
                                (LayoutParams) child.getLayoutParams();
                        child.layout(lp.left, lp.top,
                                lp.left + child.getMeasuredWidth(),
                                lp.top + child.getMeasuredHeight());
                    } else {
                        child.layout(0, 0, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                    }
                }
            }
    }


        public void addChildrenForAccessibility(ArrayList<View> c) {
            if (disableAccessibility)
                return;
            super.addChildrenForAccessibility(c);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(resolveSize(this.getLayoutParams().width, widthMeasureSpec),
                    resolveSize(this.getLayoutParams().height, heightMeasureSpec));
        }

        public static class LayoutParams extends ViewGroup.LayoutParams {
            public int left;
            public int top;

            public LayoutParams(int left, int top, int width, int height) {
                super(width, height);
                this.width = width;
                this.height = height;
                this.left = left;
                this.top = top;
            }


            public LayoutParams() {
                super(-1, -1);
                this.left=0;
                this.top=0;
            }
        }

}
