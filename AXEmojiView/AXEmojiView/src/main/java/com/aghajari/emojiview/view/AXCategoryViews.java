package com.aghajari.emojiview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;

import android.view.View;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.AXEmojiUtils;
import com.aghajari.emojiview.R;
import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.emoji.EmojiCategory;
import com.aghajari.emojiview.shared.RecentEmoji;
import com.aghajari.emojiview.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressLint("ViewConstructor")
class AXCategoryViews extends AXEmojiLayout {
    public AXCategoryViews(Context context, AXEmojiBase view, RecentEmoji recentEmoji) {
        super(context);
        this.emojiView = view;
        this.recentEmoji = recentEmoji;
        init();
    }

    RecentEmoji recentEmoji;
    AXEmojiBase emojiView;
    View selection;
    View Divider;
    boolean recent;
    List<AppCompatImageView> icons;
    int index = 0;

    void init() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        icons = new ArrayList<>();

        int left = 0;
        List<EmojiCategory> categories = new ArrayList<>(Arrays.asList(AXEmojiManager.getInstance().getCategories()));
        recent = recentEmoji.isEmpty();
        if (!recent) {
            categories.add(0, new EmojiCategory() {
                @NonNull
                @Override
                public Emoji[] getEmojis() {
                    return null;
                }

                @Override
                public int getIcon() {
                    return R.drawable.emoji_recent;
                }

                @Override
                public CharSequence getTitle() {
                    return "";
                }
            });
        }

        boolean backspace = false;
        if (!AXEmojiManager.getEmojiViewTheme().isFooterEnabled() && AXEmojiManager.isBackspaceCategoryEnabled()) {
            backspace = true;
            categories.add(new EmojiCategory() {
                @NonNull
                @Override
                public Emoji[] getEmojis() {
                    return null;
                }

                @Override
                public int getIcon() {
                    return R.drawable.emoji_backspace;
                }

                @Override
                public CharSequence getTitle() {
                    return "";
                }
            });
        }

        int size = getContext().getResources().getDisplayMetrics().widthPixels / categories.size();
        int iconSize = Utils.dpToPx(getContext(), 22);

        for (int i = 0; i < categories.size(); i++) {
            AXEmojiLayout layout = new AXEmojiLayout(getContext());
            this.addView(layout, new LayoutParams(left, 0, size, -1));
            AppCompatImageView icon = new AppCompatImageView(getContext());
            layout.addView(icon, new LayoutParams
                    ((size / 2) - (iconSize / 2), Utils.dpToPx(getContext(), 9), iconSize, iconSize));

            Drawable dr = AppCompatResources.getDrawable(getContext(), categories.get(i).getIcon());
            icon.setTag(dr);

            if (i == 0) {
                setIconImage(icon, true);
            } else {
                setIconImage(icon, false);
            }
            if (backspace && i == categories.size() - 1) {
                icon.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (emojiView.getEditText() != null)
                            AXEmojiUtils.backspace(emojiView.getEditText());
                    }
                });
            } else {
                addClick(icon, i);
                addClick(layout, i);
            }
            Utils.setClickEffect(icon, true);
            left = left + size;
            icons.add(icon);
        }

        selection = new View(getContext());
        this.addView(selection, new LayoutParams(
                0, Utils.dpToPx(getContext(), 36), size, Utils.dpToPx(getContext(), 2)));
        selection.setBackgroundColor(AXEmojiManager.getEmojiViewTheme().getSelectionColor());

        Divider = new View(getContext());
        this.addView(Divider, new LayoutParams(
                0, Utils.dpToPx(getContext(), 38), getContext().getResources().getDisplayMetrics().widthPixels, Utils.dpToPx(getContext(), 1)));
        if (!AXEmojiManager.getEmojiViewTheme().isAlwaysShowDividerEnabled())
            Divider.setVisibility(GONE);
        Divider.setBackgroundColor(AXEmojiManager.getEmojiViewTheme().getDividerColor());

        this.setBackgroundColor(AXEmojiManager.getEmojiViewTheme().getCategoryColor());
    }


    public void setPageIndex(int index) {
        if (this.index == index) return;
        this.index = index;
        for (int i = 0; i < icons.size(); i++) {
            AppCompatImageView icon = icons.get(i);
            if (i == index) {
                setIconImage(icon, true);
                setSelectionPage((AXEmojiLayout) icon.getParent());
            } else {
                setIconImage(icon, false);
            }
        }
    }

    private void setIconImage(AppCompatImageView icon, boolean selected) {
        Drawable dr = ((Drawable) icon.getTag()).getConstantState().newDrawable();
        if (selected) {
            DrawableCompat.setTint(DrawableCompat.wrap(dr), AXEmojiManager.getEmojiViewTheme().getSelectedColor());
        } else {
            DrawableCompat.setTint(DrawableCompat.wrap(dr), AXEmojiManager.getEmojiViewTheme().getDefaultColor());
        }
        icon.setImageDrawable(dr);
    }

    private void setSelectionPage(AXEmojiLayout icon) {
        ((LayoutParams) selection.getLayoutParams()).leftMargin = ((LayoutParams) icon.getLayoutParams()).leftMargin;
        this.requestLayout();
    }

    private void addClick(final View icon, final int i) {
        icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (index == i) return;
                emojiView.setPageIndex(i);
            }
        });
    }
}
