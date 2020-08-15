package com.aghajari.emojiview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

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

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() != GONE) {
                    if ((child.getLayoutParams() instanceof LayoutParams)) {
                        LayoutParams lp =
                                (LayoutParams) child.getLayoutParams();
                        int left = lp.left + lp.leftMargin;
                        int top = lp.top;
                        int right = lp.left + child.getMeasuredWidth();
                        int bottom = lp.top + child.getMeasuredHeight();
                        if (lp.bottomMargin!=0){
                            top = b - child.getMeasuredHeight() - lp.bottomMargin;
                            bottom = top + child.getMeasuredWidth();
                        }
                        if (lp.rightMargin!=0){
                            left = r - child.getMeasuredWidth() - lp.rightMargin;
                            right = left + child.getMeasuredWidth();
                        }

                        child.layout(left,top, right, bottom);
                    } else {
                        child.layout(0, 0, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                    }
                }
            }
    }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(resolveSize(this.getLayoutParams().width, widthMeasureSpec),
                    resolveSize(this.getLayoutParams().height, heightMeasureSpec));
        }

        public static class LayoutParams extends ViewGroup.LayoutParams {
            public int left;
            public int top;
            public int bottomMargin = 0;
            public int rightMargin = 0;
            public int leftMargin = 0;

            public LayoutParams(int left, int top, int width, int height) {
                super(width, height);
                this.width = width;
                this.height = height;
                this.left = left;
                this.top = top;
            }

            public LayoutParams(int width, int height) {
                super(width, height);
                this.width = width;
                this.height = height;
                this.left = 0;
                this.top = 0;
            }


            public LayoutParams() {
                super(-1, -1);
                this.left=0;
                this.top=0;
            }
        }

}
