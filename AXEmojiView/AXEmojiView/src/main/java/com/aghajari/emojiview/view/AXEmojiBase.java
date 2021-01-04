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


package com.aghajari.emojiview.view;

import android.content.Context;

import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.aghajari.emojiview.utils.Utils;

public class AXEmojiBase extends FrameLayout {

    public AXEmojiBase(Context context) {
        super(context);
        Utils.forceLTR(this);
    }

    public AXEmojiBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        Utils.forceLTR(this);
    }

    public AXEmojiBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Utils.forceLTR(this);
    }

    EditText editText;
    AXPopupInterface popupInterface;

    public void setEditText(EditText editText) {
        this.editText = editText;

        if (popupInterface!=null && editText instanceof AXEmojiEditText){
            ((AXEmojiEditText) editText).popupInterface = popupInterface;
        }
    }

    public EditText getEditText() {
        return editText;
    }

    public void setPopupInterface(AXPopupInterface popupInterface) {
        this.popupInterface = popupInterface;

        if (editText!=null && editText instanceof AXEmojiEditText){
            ((AXEmojiEditText) editText).popupInterface = popupInterface;
        }
    }

    public AXPopupInterface getPopupInterface() {
        return popupInterface;
    }

    public void dismiss() {
    }

    protected void addItemDecoration(RecyclerView.ItemDecoration decoration) {
    }

    protected void setScrollListener(RecyclerView.OnScrollListener listener) {
    }

    protected void setPageChanged(ViewPager.OnPageChangeListener listener) {
    }

    protected void refresh() {
    }

    public void setPageIndex(int Position) {
    }

    public int getPageIndex() {
        return 0;
    }

    protected void onShow() {
    }

}
