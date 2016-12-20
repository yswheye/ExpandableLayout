package com.yswheye.expandable.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yswheye.expandable.R;


public class ExpandableLayout extends LinearLayout implements View.OnClickListener {
    private Context mContext;

    private static final int DEFAULT_ANIM_DURATION = 300;
    protected ImageView mArrowImg; // Button to expand/collapse
    private boolean mCollapsed = true; // Show short version as default.
    private boolean isCollapsed = true; // 是否折叠

    private Drawable mExpandDrawable;
    private Drawable mCollapseDrawable;

    private int mAnimationDuration;
    private boolean mAnimating;
    /**
     * custom
     */
    private float mTitleHeight = 50;//title height
    /* Listener for callback */
    private OnExpandStateChangeListener mListener;
    private int mHeight;//需要改变的高度
    private int mMinHeight;//最小距离
    private Drawable mTitleIcon;// title left icon
    private SettingView titleView;
    private boolean addFlag = false;
    private int childHeight;


    public ExpandableLayout(Context context) {
        this(context, null);
    }

    public ExpandableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExpandableLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    public void setOrientation(int orientation) {
        if (LinearLayout.HORIZONTAL == orientation) {
            throw new IllegalArgumentException("ExpandableTextView only supports Vertical Orientation.");
        }
        super.setOrientation(orientation);
    }

    @Override
    public void onClick(View view) {
        if (!mAnimating) {
            mCollapsed = !mCollapsed;
            setTitleArrow();
            mAnimating = true;
            Animation animation;
            if (mCollapsed) {
                //false
                animation = new ExpandCollapseAnimation(this, mMinHeight, mHeight);//kuo
            } else {
                animation = new ExpandCollapseAnimation(this, mHeight, mMinHeight);//suo
            }

            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    clearAnimation();
                    mAnimating = false;
                    if (mListener != null) {
                        mListener.onExpandStateChanged(mCollapsed);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            clearAnimation();
            startAnimation(animation);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mCollapsed && addFlag) {
            super.onMeasure(widthMeasureSpec, mHeight);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //如果展开
        if (mCollapsed) {
            mHeight = getMeasuredHeight();
        }
    }


    public void addView(View child, int index, boolean b) {
        if (b) {
            addFlag = true;
            child.measure(0, 0);
            childHeight = child.getMeasuredHeight();
            mHeight = mHeight + childHeight;

        }
        super.addView(child, index);
    }

    @Override
    public void removeViewAt(int index) {
        addFlag = true;
        mHeight = mHeight - childHeight;
        super.removeViewAt(index);
    }

    public void setOnExpandStateChangeListener(@Nullable OnExpandStateChangeListener listener) {
        mListener = listener;
    }


    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableLayout);
        mAnimationDuration = typedArray.getInt(R.styleable.ExpandableLayout_animDuration, DEFAULT_ANIM_DURATION);
        mExpandDrawable = typedArray.getDrawable(R.styleable.ExpandableLayout_expandDrawable);
        mCollapseDrawable = typedArray.getDrawable(R.styleable.ExpandableLayout_collapseDrawable);
        isCollapsed = typedArray.getBoolean(R.styleable.ExpandableLayout_isCollapse, isCollapsed);
        mTitleIcon = typedArray.getDrawable(R.styleable.ExpandableLayout_titleIcon);
        mTitleHeight = typedArray.getDimension(R.styleable.ExpandableLayout_titleHeight, mTitleHeight);
        String mTitleName = typedArray.getString(R.styleable.ExpandableLayout_titleName);
        int color = typedArray.getColor(R.styleable.ExpandableLayout_titleBgColor, Color.WHITE);

        setOrientation(LinearLayout.VERTICAL);
        titleView = new SettingView(context);
        titleView.setOnClickListener(this);
        titleView.setBackgroundColor(color);
        RelativeLayout.LayoutParams titleLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) mTitleHeight);
        titleView.setLayoutParams(titleLayoutParams);
        if (mTitleIcon != null) {
            titleView.setIcon(mTitleIcon);
        }
        titleView.setLeftText(mTitleName);
        titleView.setRightText(typedArray.getText(R.styleable.ExpandableLayout_mtextRight));
        titleView.setDisplayArrow(typedArray.getBoolean(R.styleable.ExpandableLayout_mdisplayArrow, false));
        titleView.setDisplaySwitch(typedArray.getBoolean(R.styleable.ExpandableLayout_mdisplaySwitch, false));
        titleView.getmTextLeft().setTextColor(typedArray.getColor(R.styleable.ExpandableLayout_titleNameColor, Color.BLACK));
        titleView.getmTextRight().setTextColor(typedArray.getColor(R.styleable.ExpandableLayout_mtextRightColor, Color.BLACK));
        titleView.getmTextLeft().setTextSize(TypedValue.COMPLEX_UNIT_SP, typedArray.getDimension(R.styleable.ExpandableLayout_titleNameSize, 16));
        titleView.getmTextRight().setTextSize(TypedValue.COMPLEX_UNIT_SP, typedArray.getDimension(R.styleable.ExpandableLayout_mtextRightSize, 14));
        titleView.getDivider().setVisibility(typedArray.getBoolean(R.styleable.ExpandableLayout_mdividerVisibility, true) ? VISIBLE : GONE);
        setTitleArrow();

        typedArray.recycle();
        addView(titleView);

        // 默认折叠
        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                mMinHeight = titleView.getMeasuredHeight();
                removeOnLayoutChangeListener(this);
                if (!isCollapsed) {
                    return;
                }
                // 折叠
                Animation animation;
                //Log.d("_haha", "mHeight = " + mHeight + ", mMinHeight = " + mMinHeight);
                animation = new ExpandCollapseAnimation(ExpandableLayout.this, mHeight, mMinHeight);//suo
                animation.setFillAfter(true);
                animation.setDuration(0);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mAnimating = false;
                        mCollapsed = false;
                        setTitleArrow();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                clearAnimation();
                ExpandableLayout.this.startAnimation(animation);
            }
        });
    }

    private void setTitleArrow() {
        if (mExpandDrawable != null && mCollapseDrawable != null) {
            titleView.setDisplayArrow(mCollapsed ? mExpandDrawable : mCollapseDrawable);
        }
    }

    private static boolean isPostHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    private static boolean isPostLolipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void applyAlphaAnimation(View view, float alpha, int duration) {
        if (isPostHoneycomb()) {
            view.setAlpha(1);
        } else {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1, alpha);
            // make it instant
            alphaAnimation.setDuration(duration);
            alphaAnimation.setFillAfter(true);
            view.startAnimation(alphaAnimation);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Drawable getDrawable(@NonNull Context context, @DrawableRes int resId) {
        Resources resources = context.getResources();
        if (isPostLolipop()) {
            return resources.getDrawable(resId, context.getTheme());
        } else {
            return resources.getDrawable(resId);
        }
    }


    class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;
        private final int mStartHeight;
        private final int mEndHeight;

        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
            mTargetView = view;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            setDuration(mAnimationDuration);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final int newHeight = (int) ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
            setMinimumHeight(newHeight);
            mTargetView.getLayoutParams().height = newHeight;
            mTargetView.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    public interface OnExpandStateChangeListener {
        /**
         * Called when the expand/collapse animation has been finished
         *
         * @param isExpanded - true if the TextView has been expanded
         */
        void onExpandStateChanged(boolean isExpanded);
    }

}