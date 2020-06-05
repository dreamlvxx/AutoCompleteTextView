package com.dream.autocompletetextview;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;

public class CommonTokenizer implements MultiAutoCompleteTextView.Tokenizer {
    public final static String TAG = "xxx";

    private char mSpliteChar = ';';

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

        // 去掉原始匹配的数据的末尾空格
        while (i > 0 && text.charAt(i - 1) == ' ') {
            i--;
        }

        // // 判断原始匹配的数据去掉末尾空格后是否含有逗号，有则立即返回
        if (i > 0 && text.charAt(i - 1) == mSpliteChar) {
            return text;
        } else {
            // CharSequence类型的数据有可能是富文本SpannableString类型
            // 故需要进行判断
            if (text instanceof Spanned) {
                // 创建一个新的SpannableString，传进来的text会被退化成String，
                // 导致sp中丢失掉了text中的样式配置
                SpannableString sp = new SpannableString(text + String.valueOf(mSpliteChar) + " ");
                // 故需要借助TextUtils.copySpansFrom从text中复制原来的样式到新的sp中，
                // 以保持原先样式不变情况下添加一个逗号和空格
                TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
                        Object.class, sp, 0);
                return sp;
            } else {        // text为纯文本，直接加上逗号和空格
                return text + String.valueOf(mSpliteChar) + " ";
            }
        }
    }
}
