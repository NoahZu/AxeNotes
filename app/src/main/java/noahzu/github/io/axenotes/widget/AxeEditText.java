package noahzu.github.io.axenotes.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.List;

import noahzu.github.io.axenotes.entity.AxePicture;

/**
 * Created by Administrator on 2016/3/8.
 */
public class AxeEditText extends EditText {


    public AxeEditText(Context context) {
        super(context);
    }

    public AxeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void insertBitmap(Bitmap bitmap,String imageHolder) {
        final SpannableString ss = new SpannableString(imageHolder);
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span, 0, imageHolder.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        append(ss);
    }
}
