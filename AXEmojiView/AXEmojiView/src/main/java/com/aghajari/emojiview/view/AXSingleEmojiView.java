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

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.View;
import android.widget.EditText;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.AXEmojiUtils;
import com.aghajari.emojiview.adapters.AXSingleEmojiPageAdapter;
import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.listener.FindVariantListener;
import com.aghajari.emojiview.listener.OnEmojiActions;
import com.aghajari.emojiview.shared.RecentEmoji;
import com.aghajari.emojiview.shared.RecentEmojiManager;
import com.aghajari.emojiview.shared.VariantEmoji;
import com.aghajari.emojiview.shared.VariantEmojiManager;
import com.aghajari.emojiview.utils.Utils;
import com.aghajari.emojiview.variant.AXEmojiVariantPopup;

public class AXSingleEmojiView extends AXEmojiLayout implements FindVariantListener {
    public AXSingleEmojiView(Context context) {
        super(context);
        init();
    }

    AXCategoryViews categoryViews;
    AXEmojiSingleRecyclerView recyclerView;
    RecentEmoji recent;
    VariantEmoji variant;
    AXEmojiVariantPopup variantPopup;

    OnEmojiActions events = new OnEmojiActions() {
        @Override
        public void onClick(View view, Emoji emoji, boolean fromRecent, boolean fromVariant) {
            if (!fromVariant && variantPopup != null && variantPopup.isShowing()) return;
            if (!fromVariant) recent.addEmoji(emoji);
            if (editText != null) AXEmojiUtils.input(editText, emoji);

            variant.addVariant(emoji);
            if (variantPopup != null) variantPopup.dismiss();

            if (emojiActions != null) emojiActions.onClick(view, emoji, fromRecent, fromVariant);
        }

        @Override
        public boolean onLongClick(View view, Emoji emoji, boolean fromRecent, boolean fromVariant) {

            if (view!=null && (!fromRecent || AXEmojiManager.isRecentVariantEnabled()) && variantPopup != null) {
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
        return emojiActions;
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
        return events;
    }

    public void removeOnEmojiActionsListener() {
        emojiActions = null;
    }

    public VariantEmoji getVariantEmoji() {
        return variant;
    }

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        private int[] firstPositions;

        private boolean isShowing = true;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (recyclerView == null) {
                if (!AXEmojiManager.getEmojiViewTheme().isAlwaysShowDividerEnabled()) {
                    if (!isShowing) {
                        isShowing = true;
                        categoryViews.Divider.setVisibility(GONE);
                    }
                }
                return;
            }
            if (dy == 0) return;
            if (dy == 1) dy = 0;
            super.onScrolled(recyclerView, dx, dy);

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager == null) return;
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;

            if (!AXEmojiManager.getEmojiViewTheme().isAlwaysShowDividerEnabled()) {
                if (firstPositions == null) {
                    firstPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(firstPositions);
                int firstVisibleItemPosition = findMin(firstPositions);
                int visibleItemCount = layoutManager.getChildCount();
                if ((visibleItemCount > 0 && (firstVisibleItemPosition) == 0)) {
                    if (!isShowing) {
                        isShowing = true;
                        categoryViews.Divider.setVisibility(GONE);
                    }
                } else {
                    if (isShowing) {
                        isShowing = false;
                        categoryViews.Divider.setVisibility(VISIBLE);
                    }
                }
            }
            AXSingleEmojiPageAdapter adapter = (AXSingleEmojiPageAdapter) recyclerView.getAdapter();
            int[] firstCPositions = new int[staggeredGridLayoutManager.getSpanCount()];
            staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(firstCPositions);
            int firstCVisibleItemPosition = findMin(firstCPositions);
            for (int i = 0; i < adapter.titlesPosition.size(); i++) {
                int index = adapter.titlesPosition.get(i);
                if (firstCVisibleItemPosition >= index) {
                    if (adapter.titlesPosition.size() > i + 1 && firstCVisibleItemPosition < adapter.titlesPosition.get(i + 1)) {
                        categoryViews.setPageIndex(i + 1);
                        break;
                    } else if (adapter.titlesPosition.size() <= i + 1) {
                        categoryViews.setPageIndex(adapter.titlesPosition.size());
                        break;
                    }
                } else if (i - 1 >= 0 && firstCVisibleItemPosition > adapter.titlesPosition.get(i - 1)) {
                    categoryViews.setPageIndex(i - 1);
                    break;
                } else if (i - 1 == 0) {
                    categoryViews.setPageIndex(0);
                    break;
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        private int findMin(int[] firstPositions) {
            int min = firstPositions[0];
            for (int value : firstPositions) {
                if (value < min) {
                    min = value;
                }
            }
            return min;
        }
    };
    AXFooterParallax scrollListener2;


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

        variant = new VariantEmojiManager(getContext());

        recyclerView = new AXEmojiSingleRecyclerView(getContext(), this);
        recyclerView.setItemAnimator(null);
        this.addView(recyclerView, new LayoutParams(0, 0, -1, -1));
        recyclerView.setAdapter(new AXSingleEmojiPageAdapter(AXEmojiManager.getInstance().getCategories(), events, recent, variant));
        recyclerView.addOnScrollListener(scrollListener);

        categoryViews = new AXCategoryViews(getContext(), this, recent);
        this.addView(categoryViews, new LayoutParams(0, 0, -1, Utils.dpToPx(getContext(), 39)));

        this.setBackgroundColor(AXEmojiManager.getEmojiViewTheme().getBackgroundColor());

        scrollListener2 = new AXFooterParallax(categoryViews, -Utils.dpToPx(getContext(), 38), 50);
        scrollListener2.setDuration(Utils.dpToPx(getContext(), 38));
        scrollListener2.setIDLEHideSize(scrollListener2.getDuration() / 2);
        scrollListener2.setMinComputeScrollOffset(Utils.dpToPx(getContext(), 38));
        scrollListener2.setScrollSpeed((long) 1);
        scrollListener2.setChangeOnIDLEState(true);
        recyclerView.addOnScrollListener(scrollListener2);

    }

    public void setPageIndex(int index) {
        if (index == 0 && !categoryViews.recent) {
            recyclerView.scrollToPosition(0);
            if (!AXEmojiManager.getEmojiViewTheme().isAlwaysShowDividerEnabled())
                categoryViews.Divider.setVisibility(GONE);
        } else {
            int index2 = index - 1;
            if (categoryViews.recent) index2 = index;
            categoryViews.Divider.setVisibility(VISIBLE);
            int position = Math.max(0, ((AXSingleEmojiPageAdapter) recyclerView.getAdapter()).titlesPosition.get(index2) - 1);
            if (position > 0) {
                ((StaggeredGridLayoutManager) recyclerView.getLayoutManager())
                        .scrollToPositionWithOffset(position, -Utils.dp(this.getContext(), 6));
                categoryViews.Divider.setVisibility(VISIBLE);
            } else {
                recyclerView.scrollToPosition(0);
                if (!AXEmojiManager.getEmojiViewTheme().isAlwaysShowDividerEnabled())
                    categoryViews.Divider.setVisibility(GONE);
            }
        }
        categoryViews.setPageIndex(index);
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
        recyclerView.addOnScrollListener(listener);
    }

    @Override
    protected void refresh() {
        super.refresh();
        categoryViews.removeAllViews();
        categoryViews.init();
        ((AXSingleEmojiPageAdapter) recyclerView.getAdapter()).refresh();
        recyclerView.scrollToPosition(0);
        scrollListener2.show();
        if (!AXEmojiManager.getEmojiViewTheme().isAlwaysShowDividerEnabled())
            categoryViews.Divider.setVisibility(GONE);
        categoryViews.setPageIndex(0);
    }

    @Override
    public int getPageIndex() {
        return categoryViews.index;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public AXEmojiVariantPopup findVariant() {
        return variantPopup;
    }
}
