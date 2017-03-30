package com.example.se7en.slidingmenu;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

public class SlidingMenu extends HorizontalScrollView {
    /**
     * 菜单和屏幕右边的间距默认值
     */
    private final static int DEFAULT_RIGHT_PADDING = 50;
    /**
     * 菜单和屏幕右边的间距
     */
    private int mRightPadding;
    private FrameLayout mMenu, mContent;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlidingMenu, defStyleAttr, 0);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.SlidingMenu_rightPadding:
                    mRightPadding = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_RIGHT_PADDING, context.getResources().getDisplayMetrics()));
                    break;
            }
        }
        //回收
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        mMenu = (FrameLayout) findViewById(R.id.id_menu);
        mContent = (FrameLayout) findViewById(R.id.id_content);
        mMenu.getLayoutParams().width = sizeWidth - mRightPadding;
        mContent.getLayoutParams().width = sizeWidth;


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        scrollTo(mMenu.getWidth(), 0);
    }

    public void setMenu(FragmentTransaction fragmentTransaction, Fragment fragment) {
        fragmentTransaction.replace(R.id.id_menu, fragment);
        fragmentTransaction.commit();
    }

    public void setContent(FragmentTransaction fragmentTransaction, Fragment fragment) {
        fragmentTransaction.replace(R.id.id_content, fragment);
        fragmentTransaction.commit();
    }

    public void openMenu() {
        smoothScrollTo(0, 0);
        isMenuOpened = true;
    }

    public void closeMenu() {
        smoothScrollTo(mMenu.getWidth(), 0);
        isMenuOpened = false;
    }

    private boolean isMenuOpened = false;

    public void toogleMenu() {
        if (isMenuOpened) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                if (getScrollX() <= mMenu.getWidth() / 2) {
                    openMenu();
                } else {
                    closeMenu();
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = (float) l / mMenu.getWidth();//拖出菜单时1 -> 0

        float rightScale = 0.7f + 0.3f * scale;
        float leftScale = 1.0f - scale * 0.3f;
        float leftAlpha = 1.0f - scale * 0.7f;
        float leftTranslationX = mMenu.getWidth() * scale * 0.5f;

        ViewCompat.setPivotX(mContent, 0);
        ViewCompat.setPivotY(mContent, mContent.getHeight() / 2);
        ViewCompat.setScaleX(mContent, rightScale);
        ViewCompat.setScaleY(mContent, rightScale);

        ViewCompat.setAlpha(mMenu, leftAlpha);
        ViewCompat.setScaleX(mMenu, leftScale);
        ViewCompat.setScaleY(mMenu, leftScale);
        ViewCompat.setTranslationX(mMenu,leftTranslationX);
    }
}
