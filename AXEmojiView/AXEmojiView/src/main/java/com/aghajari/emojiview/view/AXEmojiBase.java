package com.aghajari.emojiview.view;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;

public class AXEmojiBase extends ViewGroup {

    public AXEmojiBase(Context context) {
        super(context);
    }

    public AXEmojiBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AXEmojiBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
    }

    EditText editText;
    public void setEditText (EditText editText){
        this.editText = editText;
    }

    public EditText getEditText() {
        return editText;
    }

    public void dismiss(){}

    protected void addItemDecoration (RecyclerView.ItemDecoration decoration){}
    protected void setScrollListener (RecyclerView.OnScrollListener listener){}
    protected void setPageChanged (ViewPager.OnPageChangeListener listener){}
    protected void refresh(){}
    public void setPageIndex(int Position){}
    public int getPageIndex(){return  0;}
    protected void onShow(){}
}
