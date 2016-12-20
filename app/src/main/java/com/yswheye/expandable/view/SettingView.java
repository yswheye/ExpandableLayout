package com.yswheye.expandable.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yswheye.expandable.R;
import com.yswheye.expandable.utils.DisplayUtil;


public class SettingView extends FrameLayout implements Checkable, OnCheckedChangeListener {
    private Context mContext;

    private ImageView mIcon;
    private TextView mTextLeft;
    private ImageView mImgArrow;
    public SwitchButton mSwitch;
    private TextView mTextRight;
    private View divider;
    private OnStateChangeListener mStateChangeListener;

    public SettingView(Context context) {
        this(context, null);
    }

    public SettingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.settingview, this, true);

        mIcon = (ImageView) findViewById(R.id.img_icon);
        mTextLeft = (TextView) findViewById(R.id.tv_primary);
        mTextRight = (TextView) findViewById(R.id.tv_right);
        mImgArrow = (ImageView) findViewById(R.id.img_arrow);
        divider = findViewById(R.id.view_divider);
        mSwitch = (SwitchButton) findViewById(R.id.cb_switch);
        mSwitch.setOnCheckedChangeListener(this);
        setClickable(true);

        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.SettingView);
        if (a == null) {
            throw new RuntimeException("SettingView get TypedArray is null.");
        }
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.SettingView_pic) {
                setIcon(a.getResourceId(attr, 0));
            } else if (attr == R.styleable.SettingView_textLeft) {
                setLeftText(a.getText(attr));
            } else if (attr == R.styleable.SettingView_textRight) {
                setRightText(a.getText(attr));
            } else if (attr == R.styleable.SettingView_displayArrow) {
                setDisplayArrow(a.getBoolean(attr, false));
            } else if (attr == R.styleable.SettingView_displaySwitch) {
                setDisplaySwitch(a.getBoolean(attr, false));
            } else if (attr == R.styleable.SettingView_textLeftColor) {
                mTextLeft.setTextColor(a.getColorStateList(R.styleable.SettingView_textLeftColor));
            } else if (attr == R.styleable.SettingView_textRightColor) {
                mTextRight.setTextColor(a.getColorStateList(R.styleable.SettingView_textRightColor));
            } else if (attr == R.styleable.SettingView_textLeftSize) {
                mTextLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimension(R.styleable.SettingView_textLeftSize, DisplayUtil.sp2px(mContext, 11)));
            } else if (attr == R.styleable.SettingView_textRightSize) {
                mTextRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimension(R.styleable.SettingView_textRightSize, DisplayUtil.sp2px(mContext, 11)));
            } else if (attr == R.styleable.SettingView_dividerVisibility) {
                divider.setVisibility(a.getBoolean(attr, true)? VISIBLE:GONE);
            }
        }
        a.recycle();
    }

    /**
     * Sets a drawable as the content of this icon ImageView.
     *
     * @param resId resId the resource identifier of the drawable
     */
    public void setIcon(int resId) {
        if (mIcon.getVisibility() != View.VISIBLE) {
            mIcon.setVisibility(View.VISIBLE);
        }
        mIcon.setImageResource(resId);
    }

    public void setIcon(Drawable drawable) {
        if (mIcon.getVisibility() != View.VISIBLE) {
            mIcon.setVisibility(View.VISIBLE);
        }
        mIcon.setImageDrawable(drawable);
    }

    /**
     * Sets the string value of the primary TextView.
     *
     * @param text
     */
    public final void setLeftText(CharSequence text) {
        if (text == null) {
            return;
        }
        if (mTextLeft.getVisibility() != View.VISIBLE) {
            mTextLeft.setVisibility(View.VISIBLE);
        }
        mTextLeft.setText(text);
    }

    /**
     * Sets the string value of the primary TextView.
     *
     * @param resid
     */
    public final void setLeftText(int resid) {
        if (mTextLeft.getVisibility() != View.VISIBLE) {
            mTextLeft.setVisibility(View.VISIBLE);
        }
        mTextLeft.setText(resid);
    }

    /**
     * Sets the string value of the right TextView.
     *
     * @param text
     */
    public final void setRightText(CharSequence text) {
        if (text == null) {
            return;
        }
        if (mTextRight.getVisibility() != View.VISIBLE) {
            mTextRight.setVisibility(View.VISIBLE);
        }
        mTextRight.setText(text);
    }

    /**
     * Sets the string value of the right TextView.
     *
     * @param resid
     */
    public final void setRightText(int resid) {
        if (mTextRight.getVisibility() != View.VISIBLE) {
            mTextRight.setVisibility(View.VISIBLE);
        }
        mTextRight.setText(resid);
    }

    public void setDisplayArrow(boolean diaplay) {
        if (diaplay) {
            mImgArrow.setVisibility(View.VISIBLE);
        } else {
            mImgArrow.setVisibility(View.GONE);
        }
    }

    public void setDisplayArrow(Drawable displayArrow) {
        mImgArrow.setVisibility(View.VISIBLE);
        mImgArrow.setImageDrawable(displayArrow);
    }

    public void setDisplaySwitch(boolean flag) {
        if (flag) {
            mSwitch.setVisibility(View.VISIBLE);
        } else {
            mSwitch.setVisibility(View.GONE);
        }
    }

    @Override
    public void setChecked(boolean checked) {
        mSwitch.setChecked(checked);
    }

    @Override
    public boolean isChecked() {
        return mSwitch.isChecked();
    }

    @Override
    public void toggle() {
        setChecked(!mSwitch.isChecked());
    }

    public void setOnStateChangeListener(OnStateChangeListener listener) {
        mStateChangeListener = listener;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (R.id.cb_switch == buttonView.getId()) {
            if (mStateChangeListener != null) {
                mStateChangeListener.onStateChanged(this, isChecked);
            }
        }
    }

    public static interface OnStateChangeListener {
        void onStateChanged(SettingView view, boolean state);
    }

    public TextView getmTextLeft() {
        return mTextLeft;
    }

    public TextView getmTextRight() {
        return mTextRight;
    }

    public View getDivider() {
        return divider;
    }
}
