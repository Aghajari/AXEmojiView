package com.aghajari.emojiview;

import android.content.Context;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import android.text.Spannable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.aghajari.emojiview.emoji.AXEmojiLoader;
import com.aghajari.emojiview.emoji.Emoji;
import com.aghajari.emojiview.emoji.EmojiCategory;
import com.aghajari.emojiview.emoji.EmojiProvider;
import com.aghajari.emojiview.listener.EditTextInputListener;
import com.aghajari.emojiview.listener.EmojiVariantCreatorListener;
import com.aghajari.emojiview.listener.OnEmojiActions;
import com.aghajari.emojiview.listener.StickerViewCreatorListener;
import com.aghajari.emojiview.shared.RecentEmoji;
import com.aghajari.emojiview.shared.RecentEmojiManager;
import com.aghajari.emojiview.shared.VariantEmoji;
import com.aghajari.emojiview.sticker.RecentSticker;
import com.aghajari.emojiview.sticker.RecentStickerManager;
import com.aghajari.emojiview.sticker.Sticker;
import com.aghajari.emojiview.sticker.StickerCategory;
import com.aghajari.emojiview.utils.EmojiRange;
import com.aghajari.emojiview.utils.EmojiReplacer;
import com.aghajari.emojiview.utils.EmojiSpan;
import com.aghajari.emojiview.utils.Utils;
import com.aghajari.emojiview.variant.AXEmojiVariantPopup;
import com.aghajari.emojiview.variant.AXSimpleEmojiVariantPopup;
import com.aghajari.emojiview.variant.AXTouchEmojiVariantPopup;
import com.aghajari.emojiview.view.AXEmojiBase;
import com.aghajari.emojiview.view.AXEmojiView;
import com.aghajari.emojiview.view.AXSingleEmojiView;
import com.aghajari.emojiview.view.AXStickerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AXEmojiManager {

    static boolean ripple = true;

	static EditTextInputListener defaultInputListener = new EditTextInputListener() {
		@Override
		public void input(@NonNull final EditText editText, @Nullable final Emoji emoji) {
		    if (emoji != null) {
		        final int start = editText.getSelectionStart();
		        final int end = editText.getSelectionEnd();

		        if (start < 0) {
		          editText.append(emoji.getUnicode());
		        } else {
		          editText.getText().replace(Math.min(start, end), Math.max(start, end), emoji.getUnicode(), 0, emoji.getUnicode().length());
		        }
		      }
		}
	};

	static StickerViewCreatorListener defaultStickerCreator = new StickerViewCreatorListener() {
        @Override
        public View onCreateStickerView(@NonNull Context context, @Nullable StickerCategory category, boolean isRecent) {
            return new AppCompatImageView(context);
        }

        @Override
        public View onCreateCategoryView(@NonNull Context context) {
            return new AppCompatImageView(context);
        }
    };

	static EmojiVariantCreatorListener defaultVariantCreatorListener = new EmojiVariantCreatorListener(){
        @Override
        public AXEmojiVariantPopup create(@NonNull View rootView, @Nullable OnEmojiActions listener) {
            return new AXTouchEmojiVariantPopup(rootView,listener);
        }
    };
	
	private static EditTextInputListener inputListener;
	private static StickerViewCreatorListener stickerViewCreatorListener;
	private static EmojiVariantCreatorListener emojiVariantCreatorListener;

    private AXEmojiManager(){}

    private static final Comparator<String> STRING_LENGTH_COMPARATOR = new Comparator<String>() {
        @Override public int compare(final String first, final String second) {
            final int firstLength = first.length();
            final int secondLength = second.length();

            return firstLength < secondLength ? 1 : firstLength == secondLength ? 0 : -1;
        }
    };

        private static final int GUESSED_UNICODE_AMOUNT = 3000;
        private static final int GUESSED_TOTAL_PATTERN_LENGTH = GUESSED_UNICODE_AMOUNT * 4;


        private static final EmojiReplacer DEFAULT_EMOJI_REPLACER = new EmojiReplacer() {
            @Override
            public void replaceWithImages(final Context context, final Spannable text, final float emojiSize, final Paint.FontMetrics fontMetrics, final float defaultEmojiSize, final EmojiReplacer fallback) {
                if (text.length()==0) return;
                final AXEmojiManager emojiManager = AXEmojiManager.getInstance();
                final EmojiSpan[] existingSpans = text.getSpans(0, text.length(), EmojiSpan.class);
                final List<Integer> existingSpanPositions = new ArrayList<>(existingSpans.length);

                final int size = existingSpans.length;
                //noinspection ForLoopReplaceableByForEach
                for (int i = 0; i < size; i++) {
                    existingSpanPositions.add(text.getSpanStart(existingSpans[i]));
                }

                final List<EmojiRange> findAllEmojis = emojiManager.findAllEmojis(text);

                //noinspection ForLoopReplaceableByForEach
                for (int i = 0; i < findAllEmojis.size(); i++) {
                    final EmojiRange location = findAllEmojis.get(i);

                    if (!existingSpanPositions.contains(location.start)) {
                        text.setSpan(new EmojiSpan(context, location.emoji, emojiSize),
                                location.start, location.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        };

        final Map<String, Emoji> emojiMap = new LinkedHashMap<>(GUESSED_UNICODE_AMOUNT);
        private EmojiCategory[] categories;
        private Pattern emojiPattern;
        private Pattern emojiRepetitivePattern;
        static EmojiReplacer emojiReplacer;


        static AXEmojiManager INSTANCE = null;
        public static AXEmojiManager getInstance() {
            return INSTANCE;
        }

        public static boolean isInstalled(){
            return INSTANCE!=null;
        }

        /**
         * Installs the given EmojiProvider.
         *
         * @param provider the provider that should be installed.
         */
        public static void install(Context context,final EmojiProvider provider) {
            INSTANCE = new AXEmojiManager();
            mEmojiTheme = new AXEmojiTheme();
            mStickerTheme = new AXEmojiTheme();
            recentEmoji=null;
            recentSticker=null;
            variantEmoji=null;
            emojiLoader=null;

            setMaxRecentSize(48);
            setMaxStickerRecentSize(Utils.getStickerGridCount(context)*3);
            INSTANCE.categories = provider.getCategories();
            INSTANCE.emojiMap.clear();
            emojiReplacer = provider instanceof EmojiReplacer ? (EmojiReplacer) provider : DEFAULT_EMOJI_REPLACER;
            inputListener = defaultInputListener;
            stickerViewCreatorListener = defaultStickerCreator;
            emojiVariantCreatorListener = defaultVariantCreatorListener;

            final List<String> unicodesForPattern = new ArrayList<>(GUESSED_UNICODE_AMOUNT);

            final int categoriesSize = INSTANCE.categories.length;
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < categoriesSize; i++) {
                final Emoji[] emojis = INSTANCE.categories[i].getEmojis();

                final int emojisSize = emojis.length;
                //noinspection ForLoopReplaceableByForEach
                for (int j = 0; j < emojisSize; j++) {
                    final Emoji emoji = emojis[j];
                    final String unicode = emoji.getUnicode();
                    final List<Emoji> variants = emoji.getVariants();

                    INSTANCE.emojiMap.put(unicode, emoji);
                    unicodesForPattern.add(unicode);

                    //noinspection ForLoopReplaceableByForEach
                    for (int k = 0; k < variants.size(); k++) {
                        final Emoji variant = variants.get(k);
                        final String variantUnicode = variant.getUnicode();

                        INSTANCE.emojiMap.put(variantUnicode, variant);
                        unicodesForPattern.add(variantUnicode);
                    }
                }
            }

            if (unicodesForPattern.isEmpty()) {
                throw new IllegalArgumentException("Your EmojiProvider must at least have one category with at least one emoji.");
            }

            // We need to sort the unicodes by length so the longest one gets matched first.
            Collections.sort(unicodesForPattern, STRING_LENGTH_COMPARATOR);

            final StringBuilder patternBuilder = new StringBuilder(GUESSED_TOTAL_PATTERN_LENGTH);

            final int unicodesForPatternSize = unicodesForPattern.size();
            for (int i = 0; i < unicodesForPatternSize; i++) {
                patternBuilder.append(Pattern.quote(unicodesForPattern.get(i))).append('|');
            }

            final String regex = patternBuilder.deleteCharAt(patternBuilder.length() - 1).toString();
            INSTANCE.emojiPattern = Pattern.compile(regex);
            INSTANCE.emojiRepetitivePattern = Pattern.compile('(' + regex + ")+");
        }

        public static void destroy() {
            if (isInstalled()) {
                INSTANCE.emojiMap.clear();
                INSTANCE.categories = null;
                INSTANCE.emojiPattern = null;
                INSTANCE.emojiRepetitivePattern = null;
                INSTANCE = null;
            }
        }

        public void replaceWithImages(final Context context, final Spannable text, final float emojiSize, final Paint.FontMetrics fontMetrics, final float defaultEmojiSize) {
            if (INSTANCE==null) return;
            emojiReplacer.replaceWithImages(context, text, emojiSize, fontMetrics,defaultEmojiSize, DEFAULT_EMOJI_REPLACER);
        }

    public EmojiCategory[] getCategories() {
            return categories;
    }

        Pattern getEmojiRepetitivePattern() {
            return emojiRepetitivePattern;
        }

    public List<EmojiRange> findAllEmojis(final CharSequence text) {
            final List<EmojiRange> result = new ArrayList<>();

            if (!TextUtils.isEmpty(text)) {
                final Matcher matcher = emojiPattern.matcher(text);

                while (matcher.find()) {
                    final Emoji found = findEmoji(text.subSequence(matcher.start(), matcher.end()));

                    if (found != null) {
                        result.add(new EmojiRange(matcher.start(), matcher.end(), found));
                    }
                }
            }

            return result;
        }

        public Emoji findEmoji(final CharSequence candidate) {
            // We need to call toString on the candidate, since the emojiMap may not find the requested entry otherwise, because
            // the type is different.
            return emojiMap.get(candidate.toString());
        }

        static AXEmojiTheme mEmojiTheme;

    /**
     * set AXEmojiView theme settings
     */
    public static void setEmojiViewTheme(AXEmojiTheme theme){
            mEmojiTheme = theme;
        }

    /**
     * @return AXEmojiView theme settings
     */
    public static AXEmojiTheme getEmojiViewTheme(){
            return mEmojiTheme;
        }

    /**
     * use AXEmojiManager.getEmojiViewTheme() instead.
     * @deprecated
     */
    public static AXEmojiTheme getTheme(){
        return getEmojiViewTheme();
    }

    static AXEmojiTheme mStickerTheme;

    /**
     * set AXStickerView theme settings
     */
    public static void setStickerViewTheme(AXEmojiTheme theme){
        mStickerTheme = theme;
    }

    /**
     * @return AXStickerView theme settings
     */
    public static AXEmojiTheme getStickerViewTheme(){
        return mStickerTheme;
    }

        static boolean footer = true;

    /**
     * AXEmojiPager footer view. backspace will add on footer right icon.
     * @param footer
     */
    public static void setFooterEnabled(boolean footer) {
        AXEmojiManager.footer = footer;
    }

    public static boolean isFooterEnabled() {
        return footer;
    }

    /**
     * set max emoji recent sizes in default RecentEmojiManager
     * @see RecentEmojiManager
     */
    public static void setMaxRecentSize(int size){
        RecentEmojiManager.MAX_RECENT = size;
    }

    public static int getMaxRecentSize(){
        return RecentEmojiManager.MAX_RECENT ;
    }

    /**
     * fill recent history with default values (if recent was empty)
     * default is true
     * @see RecentEmojiManager
     */
    public static void setFillRecentHistoryEnabled(boolean enabled){
        RecentEmojiManager.FILL_DEFAULT_HISTORY = enabled;
    }

    /**
     * fill recent history with this values if recent was empty.
     * @see RecentEmojiManager
     */
    public static void setFillRecentHistoryData(String[] newRecent){
        RecentEmojiManager.FILL_DEFAULT_RECENT_DATA = newRecent;
    }

    public static String[] getFillRecentHistoryData(){
        return RecentEmojiManager.FILL_DEFAULT_RECENT_DATA;
    }

    /**
     * set max sticker recent sizes in default RecentStickerManager
     * @see RecentStickerManager
     */
    public static void setMaxStickerRecentSize(int size){
        RecentStickerManager.MAX_RECENTS = size;
    }

    public static int getMaxStickerRecentSize(){
        return RecentStickerManager.MAX_RECENTS ;
    }

    static boolean recentVariant = true;

    /**
     * show emoji variants in recent tab
     */
    public static void setRecentVariantEnabled(boolean enabled){
        recentVariant = enabled;
    }

    public static boolean isRecentVariantEnabled(){
        return recentVariant;
    }

    static boolean showEmptyRecent=false;

    /**
     * Show Recent Tab while there is no data to show
     * you can manage this with isEmptyA() method in RecentManagers
     */
    public static void setShowEmptyRecentEnabled(boolean value){
        showEmptyRecent = value;
    }

    public static boolean isShowingEmptyRecentEnabled(){
        return  showEmptyRecent;
    }

    static boolean asyncLoad = true;

    public static boolean isAsyncLoadEnabled() {
        return asyncLoad;
    }


    /**
     * load emojis with an async task
     * default is true;
     */
    public static void setAsyncLoadEnabled(boolean asyncLoad) {
        AXEmojiManager.asyncLoad = asyncLoad;
    }

    static RecentEmoji recentEmoji;
    static RecentSticker recentSticker;
    static VariantEmoji variantEmoji;


    /**
     * set AXEmojiView EmojiRecentManager
     */
    public static void setRecentEmoji(RecentEmoji recentEmoji) {
        AXEmojiManager.recentEmoji = recentEmoji;
    }

    /**
     * set AXEmojiView StickerRecentManager
     */
    public static void setRecentSticker(RecentSticker recentSticker) {
        AXEmojiManager.recentSticker = recentSticker;
    }

    /**
     * set AXEmojiView VariantManager
     */
    public static void setVariantEmoji(VariantEmoji variantEmoji) {
        AXEmojiManager.variantEmoji = variantEmoji;
    }

    public static RecentEmoji getRecentEmoji() {
        return recentEmoji;
    }

    public static RecentSticker getRecentSticker() {
        return recentSticker;
    }

    public static VariantEmoji getVariantEmoji() {
        return variantEmoji;
    }

    /**
     * check AXEmojiBase is instance of AXEmojiView or AXSingleEmojiView
     */
    public static boolean isAXEmojiView (AXEmojiBase base){
        return base instanceof AXEmojiView || base instanceof AXSingleEmojiView;
    }

    /**
     * check AXEmojiBase is instance of AXStickerView
     */
    public static boolean isAXStickerView (AXEmojiBase base){
        return base instanceof AXStickerView;
    }

    static AXEmojiLoader emojiLoader;

    /**
     * set AXEmojiView EmojiLoader
     */
    public static void setEmojiLoader(AXEmojiLoader emojiLoader) {
        AXEmojiManager.emojiLoader = emojiLoader;
    }

    /**
     * set Emoji replacer
     */
    public static void setEmojiReplacer(EmojiReplacer emojiReplacer) {
        AXEmojiManager.emojiReplacer = emojiReplacer;
        if (emojiReplacer==null) {
        	AXEmojiManager.emojiReplacer = AXEmojiManager.DEFAULT_EMOJI_REPLACER;
        }
    }
    
    public static void setEditTextInputListener(EditTextInputListener listener) {
        AXEmojiManager.inputListener = listener;
        if (listener==null) {
        	AXEmojiManager.inputListener = AXEmojiManager.defaultInputListener;
        }
    }

    public static void setStickerViewCreatorListener(StickerViewCreatorListener listener) {
        AXEmojiManager.stickerViewCreatorListener = listener;
        if (listener==null) {
            AXEmojiManager.stickerViewCreatorListener = AXEmojiManager.defaultStickerCreator;
        }
    }

    public EditTextInputListener getEditTextInputListener(){
        return inputListener;
    }

    public StickerViewCreatorListener getStickerViewCreatorListener(){
        return  stickerViewCreatorListener;
    }

    public static void setEmojiVariantCreatorListener(EmojiVariantCreatorListener listener) {
        AXEmojiManager.emojiVariantCreatorListener = listener;
        if (listener==null) {
            AXEmojiManager.emojiVariantCreatorListener = AXEmojiManager.defaultVariantCreatorListener;
        }
    }

    public EmojiVariantCreatorListener getEmojiVariantCreatorListener(){
        return  emojiVariantCreatorListener;
    }

    public AXEmojiLoader getEmojiLoader() {
        return emojiLoader;
    }

    public static void disableRecentManagers(){
        recentSticker =  new RecentSticker() {
            @SuppressWarnings("rawtypes")
			@NonNull
            @Override
            public Collection<Sticker> getRecentStickers() {
                return Arrays.asList(new Sticker[0]);
            }
            @Override
            public void addSticker(@SuppressWarnings("rawtypes") @NonNull Sticker sticker) { }
            @Override
            public void persist() {}
            @Override
            public boolean isEmpty() {
                return true;
            }
        };

        recentEmoji =  new RecentEmoji() {
            @NonNull
            @Override
            public Collection<Emoji> getRecentEmojis() {
                return Arrays.asList(new Emoji[0]);
            }
            @Override
            public void addEmoji(@NonNull Emoji emoji) { }
            @Override
            public void persist() {}
            @Override
            public boolean isEmpty() {
                return true;
            }
            @Override
            public void reload(){}
        };
    }

    public static void enableRecentManagers(){
        recentEmoji = null;
        recentSticker = null;
    }

    public static boolean isRippleEnabled(){
        return ripple;
    }

    public static void setRippleEnabled(boolean enabled){
        ripple = enabled;
    }

    static boolean isUsingPopupWindow = false;

    public static void setUsingPopupWindow(boolean isUsingPopupWindow) {
        AXEmojiManager.isUsingPopupWindow = isUsingPopupWindow;
    }

    public static boolean isUsingPopupWindow(){
        return isUsingPopupWindow;
    }

    static boolean backspaceCategoryEnabled = true;

    public static boolean isBackspaceCategoryEnabled(){
        return backspaceCategoryEnabled;
    }

    public static void setBackspaceCategoryEnabled(boolean enabled){
        backspaceCategoryEnabled = enabled;
    }

}
