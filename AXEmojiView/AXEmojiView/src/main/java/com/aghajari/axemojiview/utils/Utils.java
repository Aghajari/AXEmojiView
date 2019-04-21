package com.aghajari.axemojiview.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.aghajari.axemojiview.R;
import com.aghajari.axemojiview.emoji.Emoji;
import com.aghajari.axemojiview.view.AXEmojiEditText;
import com.aghajari.axemojiview.view.AXEmojiMultiAutoCompleteTextView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class Utils {
  public static final int MIN_KEYBOARD_HEIGHT = 50;


  public static int screenHeight(@NonNull final Activity context) {
    /**final Point size = new Point();
    context.getWindowManager().getDefaultDisplay().getSize(size);
    return size.y;*/
    return  context.getResources().getDisplayMetrics().heightPixels;
  }

  public static int[] getKeyboardSize (@NonNull final  Activity context){
    final Rect rect = windowVisibleDisplayFrame(context);
    final int height = screenHeight(context) - rect.bottom;

    if (height > dpToPx(context,MIN_KEYBOARD_HEIGHT)){
      return new int[] {height,rect.right};
    }else{
      return new int[] {dpToPx(context,MIN_KEYBOARD_HEIGHT),rect.right};
    }
  }

    public static void setClickEffect(View View, boolean Borderless) {
        int[] attrs;
        if (Borderless) {
            attrs = new int[] { android.R.attr.selectableItemBackgroundBorderless };
        } else {
            attrs = new int[] { android.R.attr.selectableItemBackground };
        }
        TypedArray typedArray = View.getContext().obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        View.setBackgroundResource(backgroundResource);
    }

    public static int getGridCount(Context context){
      int w = context.getResources().getDisplayMetrics().widthPixels;
      int c_w = getColumnWidth(context);
      return  (int) w/c_w;
    }

    public static int getColumnWidth(Context context){
        return (int) context.getResources().getDimension(R.dimen.emoji_grid_view_column_width);
    }

    public static int getStickerGridCount(Context context){
        int w = context.getResources().getDisplayMetrics().widthPixels;
        int c_w = getStickerColumnWidth(context);
        return  (int) w/c_w;
    }

    public static int getStickerColumnWidth(Context context){
        return (int) context.getResources().getDimension(R.dimen.sticker_grid_view_column_width);
    }

    @TargetApi(JELLY_BEAN)
    public static void removeOnGlobalLayoutListener(final View v, final ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (SDK_INT < JELLY_BEAN) {
            //noinspection deprecation
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }


    static final int DONT_UPDATE_FLAG = -1;
    public static int dpToPx(@NonNull final Context context, final float dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics()) + 0.5f);
    }

    public  static int getOrientation(final Context context) {
        return context.getResources().getConfiguration().orientation;
    }

    public static boolean shouldOverrideRegularCondition(@NonNull final Context context, final EditText editText) {
        if ((editText.getImeOptions() & EditorInfo.IME_FLAG_NO_EXTRACT_UI) == 0) {
            return getOrientation(context) == Configuration.ORIENTATION_LANDSCAPE;
        }

        return false;
    }

   public  @SuppressWarnings({"unchecked", "JavaReflectionMemberAccess"}) static int getInputMethodHeight(final Context context, final View rootView) {
        try {
            final InputMethodManager imm = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            final Class inputMethodManagerClass = imm.getClass();
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

    public  @SuppressWarnings("JavaReflectionMemberAccess") @TargetApi(LOLLIPOP) static int getViewBottomInset(final View rootView) {
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

    public @NonNull static Point locationOnScreen(@NonNull final View view) {
        final int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new Point(location[0], location[1]);
    }

    public @NonNull static Rect windowVisibleDisplayFrame(@NonNull final Activity context) {
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
            @Override public void run() {
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

}
