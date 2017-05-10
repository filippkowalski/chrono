package com.chrono.src.ui.states.error;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Filip Kowalski on 10.05.17.
 */

public abstract class ErrorView extends LinearLayout {

	public ErrorView(Context context) {
		super(context);
	}

	public ErrorView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public ErrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public abstract void setError(@ErrorTypeGenerator.ErrorType int errorType);
}