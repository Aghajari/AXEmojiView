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


package com.aghajari.emojiview.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.aghajari.emojiview.AXEmojiUtils;
import com.aghajari.emojiview.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class Utils {

    public static void setClickEffect(View View, boolean Borderless) {
        int[] attrs;
        if (Borderless) {
            attrs = new int[]{android.R.attr.selectableItemBackgroundBorderless};
        } else {
            attrs = new int[]{android.R.attr.selectableItemBackground};
        }
        TypedArray typedArray = View.getContext().obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        View.setBackgroundResource(backgroundResource);
    }

    public static void setForegroundClickEffect(View View, boolean Borderless) {
        int[] attrs;
        if (Borderless) {
            attrs = new int[]{android.R.attr.selectableItemBackgroundBorderless};
        } else {
            attrs = new int[]{android.R.attr.selectableItemBackground};
        }
        TypedArray typedArray = View.getContext().obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        if (SDK_INT >= Build.VERSION_CODES.M) {
            View.setForeground(View.getContext().getDrawable(backgroundResource));
        }
    }

    public static int getGridCount(Context context) {
        int w = context.getResources().getDisplayMetrics().widthPixels;
        int c_w = getColumnWidth(context);
        return (int) w / c_w;
    }

    public static int getColumnWidth(Context context) {
        return (int) context.getResources().getDimension(R.dimen.emoji_grid_view_column_width);
    }

    public static int getStickerGridCount(Context context) {
        int w = context.getResources().getDisplayMetrics().widthPixels;
        int c_w = getStickerColumnWidth(context);
        return (int) w / c_w;
    }

    public static int getStickerColumnWidth(Context context) {
        return (int) context.getResources().getDimension(R.dimen.sticker_grid_view_column_width);
    }

    static final int DONT_UPDATE_FLAG = -1;

    public static int dpToPx(@NonNull final Context context, final float dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics()) + 0.5f);
    }

    public static int getOrientation(final Context context) {
        return context.getResources().getConfiguration().orientation;
    }

    public static int getProperHeight(final Activity activity) {
        return Utils.windowVisibleDisplayFrame(activity).bottom;
    }

    public static int getProperWidth(final Activity activity) {
        final Rect rect = Utils.windowVisibleDisplayFrame(activity);
        return Utils.getOrientation(activity) == Configuration.ORIENTATION_PORTRAIT ? rect.right : getScreenWidth(activity);
    }


    public static boolean shouldOverrideRegularCondition(@NonNull final Context context, final EditText editText) {
        if ((editText.getImeOptions() & EditorInfo.IME_FLAG_NO_EXTRACT_UI) == 0) {
            return getOrientation(context) == Configuration.ORIENTATION_LANDSCAPE;
        }

        return false;
    }


    public static int getInputMethodHeight(final Context context, final View rootView) {
        try {
            final InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            final Class<?> inputMethodManagerClass = imm.getClass();
            final Method visibleHeightMethod = inputMethodManagerClass.getDeclaredMethod("getInputMethodWindowVisibleHeight");
            visibleHeightMethod.setAccessible(true);
            return (int) visibleHeightMethod.invoke(imm);
        } catch (NoSuchMethodException exception) {
            exception.printStackTrace();
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        } catch (InvocationTargetException exception) {
            exception.printStackTrace();
        }

        return alternativeInputMethodHeight(rootView);
    }

    public @TargetApi(LOLLIPOP)
    static int getViewBottomInset(final View rootView) {
        try {
            final Field attachInfoField = View.class.getDeclaredField("mAttachInfo");
            attachInfoField.setAccessible(true);
            final Object attachInfo = attachInfoField.get(rootView);
            if (attachInfo != null) {
                final Field stableInsetsField = attachInfo.getClass().getDeclaredField("mStableInsets");
                stableInsetsField.setAccessible(true);
                return ((Rect) stableInsetsField.get(attachInfo)).bottom;
            }
        } catch (NoSuchFieldException noSuchFieldException) {
            noSuchFieldException.printStackTrace();
        } catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        }
        return 0;
    }

    public static int alternativeInputMethodHeight(final View rootView) {
        int viewInset = 0;
        if (SDK_INT >= LOLLIPOP) {
            viewInset = getViewBottomInset(rootView);
        }

        final Rect rect = new Rect();
        rootView.getWindowVisibleDisplayFrame(rect);

        final int availableHeight = rootView.getHeight() - viewInset - rect.top;
        return availableHeight - (rect.bottom - rect.top);
    }

    public static int getScreenWidth(@NonNull final Activity context) {
        return dpToPx(context, context.getResources().getConfiguration().screenWidthDp);
    }

    public static int getScreenHeight(@NonNull final Activity context) {
        return dpToPx(context, context.getResources().getConfiguration().screenHeightDp);
    }

    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getApplicationContext().getSystemService("input_method");
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    public @NonNull
    static Point locationOnScreen(@NonNull final View view) {
        final int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new Point(location[0], location[1]);
    }

    public @NonNull
    static Rect windowVisibleDisplayFrame(@NonNull final Activity context) {
        final Rect result = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(result);
        return result;
    }

    public static Activity asActivity(@NonNull final Context context) {
        Context result = context;

        while (result instanceof ContextWrapper) {
            if (result instanceof Activity) {
                return (Activity) result;
            }

            result = ((ContextWrapper) result).getBaseContext();
        }

        throw new IllegalArgumentException("The passed Context is not an Activity.");
    }

    public static void fixPopupLocation(@NonNull final PopupWindow popupWindow, @NonNull final Point desiredLocation) {
        popupWindow.getContentView().post(new Runnable() {
            @Override
            public void run() {
                final Point actualLocation = locationOnScreen(popupWindow.getContentView());

                if (!(actualLocation.x == desiredLocation.x && actualLocation.y == desiredLocation.y)) {
                    final int differenceX = actualLocation.x - desiredLocation.x;
                    final int differenceY = actualLocation.y - desiredLocation.y;

                    final int fixedOffsetX;
                    final int fixedOffsetY;

                    if (actualLocation.x > desiredLocation.x) {
                        fixedOffsetX = desiredLocation.x - differenceX;
                    } else {
                        fixedOffsetX = desiredLocation.x + differenceX;
                    }

                    if (actualLocation.y > desiredLocation.y) {
                        fixedOffsetY = desiredLocation.y - differenceY;
                    } else {
                        fixedOffsetY = desiredLocation.y + differenceY;
                    }

                    popupWindow.update(fixedOffsetX, fixedOffsetY, DONT_UPDATE_FLAG, DONT_UPDATE_FLAG);
                }
            }
        });
    }


    public static int dp(Context context, float value) {
        if (value == 0) return 0;
        return (int) Math.ceil(context.getResources().getDisplayMetrics().density * value);
    }

    public static float dpf2(Context context, float value) {
        if (value == 0) return 0;
        return (context.getResources().getDisplayMetrics().density * value);
    }

    public static float getPixelsInCM(Context context, float cm, boolean isX) {
        return (cm / 2.54f) * (isX ? context.getResources().getDisplayMetrics().xdpi : context.getResources().getDisplayMetrics().ydpi);
    }

    public static boolean isTablet() {
        return false;
    }

    static Point displaySize = new Point();

    public static void checkDisplaySize(Context context) {
        try {
            float density = context.getResources().getDisplayMetrics().density;
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            Configuration configuration = context.getResources().getConfiguration();


            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (manager != null) {
                Display display = manager.getDefaultDisplay();
                if (display != null) {
                    display.getMetrics(displayMetrics);
                    display.getSize(displaySize);
                }
            }
            if (configuration.screenWidthDp != Configuration.SCREEN_WIDTH_DP_UNDEFINED) {
                int newSize = (int) Math.ceil(configuration.screenWidthDp * density);
                if (Math.abs(displaySize.x - newSize) > 3) {
                    displaySize.x = newSize;
                }
            }
            if (configuration.screenHeightDp != Configuration.SCREEN_HEIGHT_DP_UNDEFINED) {
                int newSize = (int) Math.ceil(configuration.screenHeightDp * density);
                if (Math.abs(displaySize.y - newSize) > 3) {
                    displaySize.y = newSize;
                }
            }
        } catch (Exception e) {
        }
    }

    private static String orientation(Context context) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? "landscape" : "portrait");
    }

    public static int getKeyboardHeight(Context context, int def) {
        return context.getSharedPreferences("emoji-preference-manager", Context.MODE_PRIVATE)
                .getInt("keyboard_height_" + orientation(context), def);
    }

    public static void updateKeyboardHeight(Context context, int value) {
        context.getSharedPreferences("emoji-preference-manager", Context.MODE_PRIVATE)
                .edit().putInt("keyboard_height_" + orientation(context), value).apply();
    }

    public static RecyclerView.ItemDecoration getRVLastRowBottomMarginDecoration(final int bottomMargin) {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                int max = parent.getAdapter().getItemCount();
                int spanCount = 1;
                if (parent.getLayoutManager() instanceof GridLayoutManager) {
                    spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();

                    if (max % spanCount == 0) {
                        if ((parent.getAdapter().getItemCount() - position) > spanCount) {
                            outRect.bottom = 0;
                        } else {
                            outRect.bottom = bottomMargin;
                        }
                    } else if (max % spanCount >= max - position) {
                        if ((parent.getAdapter().getItemCount() - position) > spanCount) {
                            outRect.bottom = 0;
                        } else {
                            outRect.bottom = bottomMargin;
                        }
                    }
                } else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
                    if (position == max - 1) {
                        outRect.bottom = bottomMargin;
                    } else {
                        outRect.bottom = 0;
                    }
                }
            }
        };
    }

    public static void enableBackspaceTouch(View backspaceView, EditText editText) {
        backspaceView.setOnTouchListener(new BackspaceTouchListener(backspaceView, editText));
    }

    static class BackspaceTouchListener implements View.OnTouchListener {
        private boolean backspacePressed;
        private boolean backspaceOnce;
        private View backSpace;
        private EditText editText;

        BackspaceTouchListener(View backspaceView, EditText editText) {
            this.backSpace = backspaceView;
            this.editText = editText;
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                backspacePressed = true;
                backspaceOnce = false;
                postBackspaceRunnable(350);
            } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
                backspacePressed = false;
                if (!backspaceOnce) {
                    AXEmojiUtils.backspace(editText);
                    backSpace.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                }
            }
            return true;
        }

        private void postBackspaceRunnable(final int time) {
            backSpace.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!backspacePressed) return;

                    AXEmojiUtils.backspace(editText);
                    backSpace.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);

                    backspaceOnce = true;
                    postBackspaceRunnable(Math.max(50, time - 100));
                }
            }, time);
        }
    }

    public static float getDefaultEmojiSize(Paint.FontMetrics fontMetrics){
        return fontMetrics.descent - fontMetrics.ascent;
    }

    public static void forceLTR(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
    }
}
