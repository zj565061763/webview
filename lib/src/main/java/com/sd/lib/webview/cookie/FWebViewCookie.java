package com.sd.lib.webview.cookie;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

/**
 * webview的cookie管理
 */
public class FWebViewCookie
{
    public static void init(Context context)
    {
        CookieSyncManager.createInstance(context);
    }

    //---------- get ----------

    public static String getCookie(String url)
    {
        return CookieManager.getInstance().getCookie(url);
    }

    public static List<HttpCookie> getCookieAsList(String url)
    {
        final String cookie = getCookie(url);
        if (TextUtils.isEmpty(cookie))
            return null;

        final String[] arrCookie = cookie.split(";");
        if (arrCookie == null || arrCookie.length <= 0)
            return null;

        final List<HttpCookie> listCookie = new ArrayList<>();
        for (String item : arrCookie)
        {
            final String[] arrPair = item.split("=");
            if (arrPair != null && arrPair.length == 2)
            {
                listCookie.add(new HttpCookie(arrPair[0], arrPair[1]));
            }
        }
        return listCookie;
    }

    //---------- set ----------

    public static void setCookie(String url, List<HttpCookie> listCookie)
    {
        if (listCookie == null || listCookie.isEmpty())
            return;

        for (HttpCookie item : listCookie)
        {
            setCookie(url, item);
        }
    }

    public static void setCookie(String url, HttpCookie cookie)
    {
        if (cookie == null)
            return;

        setCookie(url, cookie.getName() + "=" + cookie.getValue());
    }

    public static void setCookie(String url, String value)
    {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(value))
            return;

        CookieManager.getInstance().setCookie(url, value);
    }

    //---------- other ----------

    public static void removeSessionCookie()
    {
        CookieManager.getInstance().removeSessionCookie();
    }

    public static void removeAllCookie()
    {
        CookieManager.getInstance().removeAllCookie();
    }

    public static void flush(Context context)
    {
        if (Build.VERSION.SDK_INT >= 21)
        {
            CookieManager.getInstance().flush();
        } else
        {
            CookieSyncManager.getInstance().sync();
        }
    }
}
