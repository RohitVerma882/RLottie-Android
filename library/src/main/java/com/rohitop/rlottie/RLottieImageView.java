package com.rohitop.rlottie;

import android.content.Context;
import android.widget.ImageView;
import java.util.HashMap;
import android.util.AttributeSet;

public class RLottieImageView extends ImageView {

    private HashMap<String, Integer> layerColors;
    private RLottieDrawable drawable;
	
    private boolean autoRepeat;
    private boolean attachedToWindow;
    private boolean playing;
    private boolean startOnAttach;

    public RLottieImageView(Context context) {
        super(context);
    }

    public RLottieImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RLottieImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void clearLayerColors() {
        layerColors.clear();
    }

    public void setLayerColor(String layer, int color) {
        if (layerColors == null) {
            layerColors = new HashMap<>();
        }
		
        layerColors.put(layer, color);
		
        if (drawable != null) {
            drawable.setLayerColor(layer, color);
        }
    }

    public void replaceColors(int[] colors) {
        if (drawable != null) {
            drawable.replaceColors(colors);
        }
    }

    public void setAnimation(int resId, int w, int h) {
        setAnimation(resId, w, h, null);
    }

    public void setAnimation(int resId, int w, int h, int[] colorReplacement) {
        setAnimation(new RLottieDrawable(resId, "" + resId, RLottie.toPx(w), RLottie.toPx(h), false, colorReplacement));
    }

    public void setAnimation(RLottieDrawable lottieDrawable) {
        drawable = lottieDrawable;
		
        if (autoRepeat) {
            drawable.setAutoRepeat(1);
        }
        if (layerColors != null) {
            drawable.beginApplyLayerColors();
            for (HashMap.Entry<String, Integer> entry : layerColors.entrySet()) {
                drawable.setLayerColor(entry.getKey(), entry.getValue());
            }
            drawable.commitApplyLayerColors();
        }
        drawable.setAllowDecodeSingleFrame(true);
		
        setImageDrawable(drawable);
    }

    public void clearAnimationDrawable() {
        if (drawable != null) {
            drawable.stop();
        }
		
        drawable = null;
        setImageDrawable(null);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        attachedToWindow = true;
		
        if (drawable != null) {
            drawable.setCallback(this);
            if (playing) {
                drawable.start();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        attachedToWindow = false;
		
        if (drawable != null) {
            drawable.stop();
        }
    }

    public boolean isPlaying() {
        return drawable != null && drawable.isRunning();
    }

    public void setAutoRepeat(boolean repeat) {
        autoRepeat = repeat;
    }

    public void setProgress(float progress) {
        if (drawable == null) {
            return;
        }
		
        drawable.setProgress(progress);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        drawable = null;
    }

    public void playAnimation() {
        if (drawable == null) {
            return;
        }
		
        playing = true;
        if (attachedToWindow) {
            drawable.start();
        } else {
            startOnAttach = true;
        }
    }

    public void stopAnimation() {
        if (drawable == null) {
            return;
        }
		
        playing = false;
        if (attachedToWindow) {
            drawable.stop();
        } else {
            startOnAttach = false;
        }
    }

    public RLottieDrawable getAnimatedDrawable() {
        return drawable;
    }
}

