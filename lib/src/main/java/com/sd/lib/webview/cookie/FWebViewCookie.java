package com.sd.lib.webview.cookie;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * webview的cookie管理
 */
public class FWebViewCookie
{
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

    public static boolean setCookie(String url, List<HttpCookie> listCookie)
    {
        if (listCookie == null || listCookie.isEmpty())
            return false;

        final URI uri = toURI(url);
        if (uri == null)
            return false;

        for (HttpCookie cookie : listCookie)
        {
            final String cookieString = cookie.getName() + "=" + cookie.getValue();
            if (!setCookieInternal(uri, cookieString))
                return false;
        }
        return true;
    }

    public static boolean setCookie(String url, HttpCookie cookie)
    {
        if (cookie == null)
            return false;

        final String cookieString = cookie.getName() + "=" + cookie.getValue();
        return setCookie(url, cookieString);
    }

    public static boolean setCookie(String url, String cookie)
    {
        return setCookieInternal(toURI(url), cookie);
    }

    private static URI toURI(String url)
    {
        if (TextUtils.isEmpty(url))
            return null;

        try
        {
            final URI uri = new URI(url);
            return uri;
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean setCookieInternal(URI uri, String cookie)
    {
        if (uri == null || TextUtils.isEmpty(cookie))
            return false;

        final String url = uri.getScheme() + "://" + uri.getHost();
        CookieManager.getInstance().setCookie(url, cookie);
        return true;
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
            CookieSyncManager.createInstance(context).sync();
        }
    }
}
