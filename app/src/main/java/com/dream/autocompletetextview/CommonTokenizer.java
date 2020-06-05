package com.dream.autocompletetextview;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;

public class CommonTokenizer implements MultiAutoCompleteTextView.Tokenizer {
    public final static String TAG = "xxx";

    private char mSpliteChar;

    public CommonTokenizer(char mSpliteChar) {
        this.mSpliteChar = mSpliteChar;
    }

    // 用于查找当前光标位置之前的分隔符的位置并返回，向前查询
    // text - 用户已经输入的文本内容
    // cursor - 当前光标的位置，在文本内容后面
    public int findTokenStart(CharSequence text, int cursor) {
        int i = cursor;

        Log.e(TAG, "findTokenStart: cursor = " + cursor);
        // 查找当前光标的前一个位置非','的字符位置
        while (i > 0 && text.charAt(i - 1) != mSpliteChar) {
            i--;
        }
        Log.e(TAG, "findTokenStart: after1 i = " + i);
        // 查找','后面非空格的字符位置
        while (i < cursor && text.charAt(i) == ' ') {
            i++;
        }
        Log.e(TAG, "findTokenStart: return i = " + i);

        return i;   // 返回一个要加分隔符的字符串的开始位置
    }

    // 用于查找当前光标位置之后的分隔符的位置并返回，向后查询
    // text - 用户已经输入的文本内容
    // cursor - 当前光标的位置，在文本内容之间
    public int findTokenEnd(CharSequence text, int cursor) {
        int i = cursor;
        int len = text.length();

        Log.e(TAG, "findTokenEnd: cursor = " + cursor);
        // 向后查找','字符，若找到则直接返回其所在位置
        while (i < len) {
            if (text.charAt(i) == mSpliteChar) {
                Log.e(TAG, "findTokenEnd: final i = " + i);
                return i;
            } else {
                i++;
            }
        }
        return len;
    }

    // 用于返回提示信息加上分隔符后的文本内容
    // text - 提示信息中的文本内容
    public CharSequence terminateToken(CharSequence text) {
        int i = text.length();

        // 找到第一个不是空格的字符
        while (i > 0 && text.charAt(i - 1) == ' ') {
            i--;
        }

        // 判断第一个不是空格的字符是否等于分隔符,如果是就直接返回，如果不是加一个分隔符
        if (i > 0 && text.charAt(i - 1) == mSpliteChar) {
            return text;
        } else {
            //如果匹配返回的文本属于富文本，需要还原样式(传进来的text会被退化成String)或者自己定义一些样式,然后加分隔符
            if (text instanceof Spanned) {
                SpannableString sp = new SpannableString(text + String.valueOf(mSpliteChar) + " ");
                BackgroundColorSpan colorSpan = new BackgroundColorSpan(Color.parseColor("#AC00FF30"));
                sp.setSpan(colorSpan, 9, sp.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
                        Object.class, sp, 0);
                return sp;
            } else {
                //在这里其实我们可以给文本定义一些样式
                SpannableString sp = new SpannableString(text + String.valueOf(mSpliteChar) + " ");
                BackgroundColorSpan colorSpan = new BackgroundColorSpan(Color.parseColor("#AC00FF30"));
                sp.setSpan(colorSpan, 0, sp.length() - 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                // text为纯文本，直接加上分隔符
                return sp;
            }
        }
    }
}
