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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.AXEmojiUtils;
import com.aghajari.emojiview.adapters.AXEmojiViewPagerAdapter;
import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.listener.FindVariantListener;
import com.aghajari.emojiview.listener.OnEmojiActions;
import com.aghajari.emojiview.shared.RecentEmoji;
import com.aghajari.emojiview.shared.RecentEmojiManager;
import com.aghajari.emojiview.shared.VariantEmoji;
import com.aghajari.emojiview.shared.VariantEmojiManager;
import com.aghajari.emojiview.utils.Utils;
import com.aghajari.emojiview.variant.AXEmojiVariantPopup;

public class AXEmojiView extends AXEmojiLayout implements FindVariantListener {
    public AXEmojiView(Context context) {
        super(context);
        init();
    }

    public AXEmojiView(Context context, OnEmojiActions ev) {
        super(context);
        this.events = ev;
        init();
    }

    AXCategoryViews categoryViews;
    ViewPager vp;
    RecentEmoji recent;
    VariantEmoji variant;
    AXEmojiVariantPopup variantPopup;

    OnEmojiActions events = new OnEmojiActions() {
        @Override
        public void onClick(View view, Emoji emoji, boolean fromRecent, boolean fromVariant) {
            if (!fromVariant && variantPopup != null && variantPopup.isShowing()) return;
            if (!fromVariant) recent.addEmoji(emoji);
            if (editText != null) AXEmojiUtils.input(editText, emoji);

            /**if (!fromRecent && ((AXEmojiViewPagerAdapter) vp.getAdapter()).add==1)
             ((AXEmojiViewPagerAdapter) vp.getAdapter()).recyclerViews.get(0).getAdapter().notifyDataSetChanged();*/

            variant.addVariant(emoji);
            if (variantPopup != null) variantPopup.dismiss();

            if (emojiActions != null) emojiActions.onClick(view, emoji, fromRecent, fromVariant);
        }

        @Override
        public boolean onLongClick(View view, Emoji emoji, boolean fromRecent, boolean fromVariant) {

            if (view!=null && variantPopup != null && (!fromRecent || AXEmojiManager.isRecentVariantEnabled())) {
                if (emoji.getBase().hasVariants())
                    variantPopup.show((AXEmojiImageView) view, emoji, fromRecent);
            }

            if (emojiActions != null)
                return emojiActions.onLongClick(view, emoji, fromRecent, fromVariant);
            return false;
        }
    };

    OnEmojiActions emojiActions = null;

    public OnEmojiActions getInnerEmojiActions() {
        return events;
    }

    /**
     * add emoji click and longClick listener
     *
     * @param listener
     */
    public void setOnEmojiActionsListener(OnEmojiActions listener) {
        emojiActions = listener;
    }

    public OnEmojiActions getOnEmojiActionsListener() {
        return emojiActions;
    }

    public VariantEmoji getVariantEmoji() {
        return variant;
    }

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

        private boolean isShowing = true;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (recyclerView == null) {
                if (!AXEmojiManager.getEmojiViewTheme().isAlwaysShowDividerEnabled()) {
                    if (!isShowing) {
                        isShowing = true;
                        if (categoryViews != null) categoryViews.Divider.setVisibility(GONE);
                    }
                }
                return;
            }
            if (dy == 0) return;
            if (dy == 1) dy = 0;
            super.onScrolled(recyclerView, dx, dy);
            if (scrollListener2 != null)
                scrollListener2.onScrolled(recyclerView, dx, dy);

            if (!AXEmojiManager.getEmojiViewTheme().isAlwaysShowDividerEnabled()) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager == null) return;
                int firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
                int visibleItemCount = layoutManager.getChildCount();
                if ((visibleItemCount > 0 && (firstVisibleItemPosition) == 0)) {
                    if (!isShowing) {
                        isShowing = true;
                        if (categoryViews != null) categoryViews.Divider.setVisibility(GONE);
                    }
                } else {
                    if (isShowing) {
                        isShowing = false;
                        if (categoryViews != null) categoryViews.Divider.setVisibility(VISIBLE);
                    }
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (scrollListener2 != null)
                scrollListener2.onScrollStateChanged(recyclerView, newState);
        }
    };

    RecyclerView.OnScrollListener scrollListener2 = null;

    private void init() {
        if (AXEmojiManager.getRecentEmoji() != null) {
            recent = AXEmojiManager.getRecentEmoji();
        } else {
            recent = new RecentEmojiManager(getContext());
        }
        if (AXEmojiManager.getVariantEmoji() != null) {
            variant = AXEmojiManager.getVariantEmoji();
        } else {
            variant = new VariantEmojiManager(getContext());
        }

        int top = 0;
        if (AXEmojiManager.getEmojiViewTheme().isCategoryEnabled())
            top = Utils.dpToPx(getContext(), 39);

        vp = new ViewPager(getContext());
        this.addView(vp, new AXEmojiLayout.LayoutParams(0, top, -1, -1));
        vp.setAdapter(new AXEmojiViewPagerAdapter(events, scrollListener, recent, variant, this));
        //vp.setPadding(0, 0, 0, top);

        if (AXEmojiManager.getEmojiViewTheme().isCategoryEnabled()) {
            categoryViews = new AXCategoryViews(getContext(), this, recent);
            this.addView(categoryViews, new AXEmojiLayout.LayoutParams(0, 0, -1, top));
        } else {
            categoryViews = null;
        }

        this.setBackgroundColor(AXEmojiManager.getEmojiViewTheme().getBackgroundColor());
        if (categoryViews != null)
            categoryViews.setBackgroundColor(AXEmojiManager.getEmojiViewTheme().getBackgroundColor());

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (pagerListener2 != null) pagerListener2.onPageScrolled(i, v, i1);
            }

            @Override
            public void onPageSelected(int i) {
                vp.setCurrentItem(i, true);
                if (((AXEmojiViewPagerAdapter) vp.getAdapter()).recyclerViews.size() > i) {
                    scrollListener.onScrolled(((AXEmojiViewPagerAdapter) vp.getAdapter()).recyclerViews.get(i), 0, 1);
                } else {
                    scrollListener.onScrolled(null, 0, 1);
                }
                if (categoryViews != null) categoryViews.setPageIndex(i);
                if (pagerListener2 != null) pagerListener2.onPageSelected(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (pagerListener2 != null) pagerListener2.onPageScrollStateChanged(i);
            }
        });

    }

    ViewPager.OnPageChangeListener pagerListener2 = null;

    @Override
    public void setPageIndex(int index) {
        vp.setCurrentItem(index, true);
        if (!AXEmojiManager.getEmojiViewTheme().isAlwaysShowDividerEnabled()) {
            if (((AXEmojiViewPagerAdapter) vp.getAdapter()).recyclerViews.size() > index) {
                scrollListener.onScrolled(((AXEmojiViewPagerAdapter) vp.getAdapter()).recyclerViews.get(index), 0, 1);
            } else {
                scrollListener.onScrolled(null, 0, 1);
            }
        }
        if (categoryViews != null) categoryViews.setPageIndex(index);
    }

    @Override
    public void dismiss() {
        if (variantPopup != null) variantPopup.dismiss();
        recent.persist();
        variant.persist();
    }

    @Override
    public void setEditText(EditText editText) {
        super.setEditText(editText);
        variantPopup = AXEmojiManager.getEmojiVariantCreatorListener().create(editText.getRootView(), events);
    }

    @Override
    protected void setScrollListener(RecyclerView.OnScrollListener listener) {
        super.setScrollListener(listener);
        scrollListener2 = listener;
    }

    @Override
    protected void setPageChanged(ViewPager.OnPageChangeListener listener) {
        super.setPageChanged(listener);
        pagerListener2 = listener;
    }

    @Override
    protected void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        ((AXEmojiViewPagerAdapter) vp.getAdapter()).itemDecoration = itemDecoration;
        for (int i = 0; i < ((AXEmojiViewPagerAdapter) vp.getAdapter()).recyclerViews.size(); i++) {
            ((AXEmojiViewPagerAdapter) vp.getAdapter()).recyclerViews.get(i).addItemDecoration(itemDecoration);
        }
    }

    @Override
    protected void refresh() {
        super.refresh();
        if (categoryViews != null) {
            categoryViews.removeAllViews();
            categoryViews.init();
        }
        vp.getAdapter().notifyDataSetChanged();
        vp.setCurrentItem(0, false);
        if (!AXEmojiManager.getEmojiViewTheme().isAlwaysShowDividerEnabled())
            scrollListener.onScrolled(null, 0, 1);
        if (categoryViews != null) categoryViews.setPageIndex(0);
    }

    @Override
    public int getPageIndex() {
        return vp.getCurrentItem();
    }

    public ViewPager getViewPager() {
        return vp;
    }

    @Override
    public AXEmojiVariantPopup findVariant() {
        return variantPopup;
    }
}
