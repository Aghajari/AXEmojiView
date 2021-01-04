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


package com.aghajari.emojiview.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.AXEmojiTheme;
import com.aghajari.emojiview.R;
import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.listener.FindVariantListener;
import com.aghajari.emojiview.listener.OnEmojiActions;
import com.aghajari.emojiview.shared.VariantEmoji;
import com.aghajari.emojiview.utils.Utils;
import com.aghajari.emojiview.view.AXEmojiBase;
import com.aghajari.emojiview.view.AXEmojiEditText;
import com.aghajari.emojiview.view.AXEmojiImageView;
import com.aghajari.emojiview.view.AXEmojiRecyclerView;
import com.aghajari.emojiview.view.AXEmojiView;
import com.aghajari.emojiview.view.AXSearchViewInterface;
import com.aghajari.emojiview.view.AXSingleEmojiView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ViewConstructor")
public class AXEmojiSearchView extends FrameLayout implements AXSearchViewInterface {

    AXEmojiTheme theme;
    protected AXEmojiBase base;
    boolean showing;
    AXDataAdapter<Emoji> dataAdapter;
    protected RecyclerView recyclerView;
    protected AppCompatEditText editText;
    protected AppCompatImageView backButton;
    protected AppCompatTextView noResultTextView;

    public AXEmojiSearchView(Context context, AXEmojiBase emojiView) {
        super(context);
        if (!AXEmojiManager.isAXEmojiView(emojiView))
            throw new RuntimeException("EmojiView must be an instance of AXEmojiView or AXSingleEmojiView.");
        this.theme = AXEmojiManager.getEmojiViewTheme();
        this.base = emojiView;
        setDataAdapter(new AXSimpleEmojiDataAdapter(context));
    }

    public @NonNull AXDataAdapter<Emoji> getDataAdapter() {
        return dataAdapter;
    }

    public void setDataAdapter(@NonNull AXDataAdapter<Emoji> dataAdapter) {
        if (this.dataAdapter != null) this.dataAdapter.destroy();
        this.dataAdapter = dataAdapter;
        this.dataAdapter.init();
    }

    public AXEmojiTheme getTheme() {
        return theme;
    }

    /**
     * Theme needed properties :
     * - CategoryColor (BackgroundColor)
     * - TitleTypeface (EditTextTypeface)
     * - TitleColor (EditTextTextColor)
     * - DefaultColor (EditTextHintColor)
     */
    public void setTheme(AXEmojiTheme theme) {
        this.theme = theme;
        this.setBackgroundColor(theme.getCategoryColor());
        if (editText!=null && editText.getParent()!=null) {
            editText.setTypeface(theme.getTitleTypeface());
            editText.setHintTextColor(theme.getDefaultColor());
            editText.setTextColor(theme.getTitleColor());
            editText.setBackgroundColor(Color.TRANSPARENT);
            noResultTextView.setTextColor(theme.getDefaultColor());
            noResultTextView.setTypeface(theme.getTitleTypeface());

            Drawable dr = AppCompatResources.getDrawable(getContext(), R.drawable.arrow_back);
            DrawableCompat.setTint(DrawableCompat.wrap(dr), AXEmojiManager.getEmojiViewTheme().getDefaultColor());
            backButton.setImageDrawable(dr);
        }
    }

    @Override
    public int getSearchViewHeight() {
        return Utils.dp(getContext(),98);
    }

    @Override
    public void show() {
        if (showing) return;
        showing = true;
        init();
    }

    @Override
    public void hide() {
        showing = false;
        removeAllViews();
    }

    @Override
    public boolean isShowing() {
        return showing;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public EditText getSearchTextField() {
        return editText;
    }

    protected void searched (boolean hasResult){
        if (noResultTextView!=null)
        noResultTextView.setVisibility(hasResult?GONE:VISIBLE);
    }

    protected void init(){
        removeAllViews();
        this.setBackgroundColor(theme.getCategoryColor());

        recyclerView = new AXEmojiRecyclerView(getContext(), (FindVariantListener) base,
                new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        FrameLayout.LayoutParams lp_rv = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,Utils.dp(getContext(),38));
        lp_rv.topMargin = Utils.dp(getContext(),8);
        this.addView(recyclerView,lp_rv);
        recyclerView.setAdapter(createAdapter());
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position == 0) {
                    outRect.left = Utils.dp(parent.getContext(),16);
                } else {
                    outRect.left = Utils.dp(parent.getContext(),6);
                }
                if (position == parent.getAdapter().getItemCount()-1) outRect.right = Utils.dp(parent.getContext(),16);
            }
        });

        editText = new CustomEditText(getContext());
        editText.setHint("Search");
        editText.setTypeface(theme.getTitleTypeface());
        editText.setTextSize(18);
        editText.setHintTextColor(theme.getDefaultColor());
        editText.setTextColor(theme.getTitleColor());
        editText.setSingleLine();
        editText.setBackgroundColor(Color.TRANSPARENT);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-1,-1);
        lp.leftMargin = Utils.dp(getContext(),48);
        lp.rightMargin = Utils.dp(getContext(),48);
        lp.bottomMargin = Utils.dp(getContext(),0);
        lp.topMargin = Utils.dp(getContext(),46);
        lp.gravity = Gravity.BOTTOM;
        this.addView(editText,lp);

        final TextWatcher textWatcherSearchListener = new TextWatcher() {
            final android.os.Handler handler = new android.os.Handler();
            Runnable runnable;

            public void onTextChanged(final CharSequence s, int start, final int before, int count) {
                handler.removeCallbacks(runnable);
            }

            @Override
            public void afterTextChanged(final Editable s) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        searchFor(s.toString());
                    }
                };
                handler.postDelayed(runnable, 100);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        };
        editText.addTextChangedListener(textWatcherSearchListener);

        noResultTextView = new AppCompatTextView(getContext());
        this.addView(noResultTextView,lp_rv);
        noResultTextView.setVisibility(GONE);
        noResultTextView.setTextSize(18);
        noResultTextView.setText("No emoji found");
        noResultTextView.setTypeface(theme.getTitleTypeface());
        noResultTextView.setGravity(Gravity.CENTER);
        noResultTextView.setTextColor(theme.getDefaultColor());

        backButton = new AppCompatImageView(getContext());
        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(Utils.dp(getContext(),26),-1);
        lp2.leftMargin = Utils.dp(getContext(),13);
        lp2.bottomMargin = Utils.dp(getContext(),8);
        lp2.topMargin = Utils.dp(getContext(),54);
        lp2.gravity = Gravity.BOTTOM;
        this.addView(backButton,lp2);
        Utils.setClickEffect(backButton,true);

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText instanceof CustomEditText){
                    ((CustomEditText) editText).reload();
                }else{
                    base.getPopupInterface().reload();
                }
                base.getPopupInterface().show();
            }
        });

        Drawable dr = AppCompatResources.getDrawable(getContext(), R.drawable.arrow_back);
        DrawableCompat.setTint(DrawableCompat.wrap(dr), AXEmojiManager.getEmojiViewTheme().getDefaultColor());
        backButton.setImageDrawable(dr);
    }

    protected void searchFor (String value){
        if (recyclerView.getAdapter() == null) return;
        ((AXUISearchAdapter) recyclerView.getAdapter()).searchFor(value);
    }

    protected OnEmojiActions findActions(){
        if (base instanceof AXEmojiView){
            return ((AXEmojiView) base).getInnerEmojiActions();
        } else if (base instanceof AXSingleEmojiView){
            return ((AXSingleEmojiView) base).getInnerEmojiActions();
        }
        return null;
    }

    protected VariantEmoji findVariant(){
        if (base instanceof AXEmojiView){
            return ((AXEmojiView) base).getVariantEmoji();
        } else if (base instanceof AXSingleEmojiView){
            return ((AXSingleEmojiView) base).getVariantEmoji();
        }
        return AXEmojiManager.getVariantEmoji();
    }

    private RecyclerView.Adapter<?> createAdapter(){
        return new SearchAdapter(this);
    }


    protected static class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements AXUISearchAdapter {
        AXEmojiSearchView searchView;
        List<Emoji> list;

        public SearchAdapter(AXEmojiSearchView searchView){
            super();
            this.searchView = searchView;
            list = new ArrayList<>();
            searchFor("");
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            FrameLayout frameLayout = new FrameLayout(parent.getContext());
            AXEmojiImageView emojiView = new AXEmojiImageView(parent.getContext());
            int cw = Utils.dp(parent.getContext(),38);
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams(cw, cw));
            frameLayout.addView(emojiView);

            int dp6 = Utils.dp(parent.getContext(), 6);
            emojiView.setPadding(dp6, dp6, dp6, dp6);
            return new ViewHolder(frameLayout);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            FrameLayout frameLayout = (FrameLayout) viewHolder.itemView;
            final AXEmojiImageView emojiView = (AXEmojiImageView) frameLayout.getChildAt(0);

            Emoji emoji = list.get(position);
            emojiView.setEmoji(searchView.findVariant().getVariant(emoji));
            emojiView.setOnEmojiActions(searchView.findActions(), true);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void searchFor(String value) {
            list.clear();
            if (value.trim().isEmpty()){
                list.addAll(AXEmojiManager.getRecentEmoji().getRecentEmojis());
            }else {
                list.addAll(searchView.getDataAdapter().searchFor(value));
            }
            searchView.searched(!list.isEmpty());
            notifyDataSetChanged();
        }

        protected static class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

    protected static class CustomEditText extends AXEmojiEditText {

        boolean req = false;

        public CustomEditText(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.clearFocus();
        }

        @Override
        protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
            if (focused) req = false;
            if (!req && !focused && getParent()!=null) reload();
        }

        public void reload(){
            ((AXEmojiSearchView) getParent()).base.getPopupInterface().reload();
            req = true;
        }

        @Override
        public boolean onKeyPreIme(int keyCode, KeyEvent event) {
            if (getParent() != null && !req && keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && hasFocus()) {
                req = true;
                InputMethodManager mgr = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (mgr != null) {
                    mgr.hideSoftInputFromWindow(this.getWindowToken(), 0);
                }
                if (((AXEmojiSearchView) getParent()).base.getPopupInterface() != null) {
                    ((AXEmojiSearchView) getParent()).base.getPopupInterface().reload();
                }
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_BACK && req) return true;
            return super.onKeyPreIme(keyCode,event);
        }
    }

}
