package org.openskope.util;

public class Uri {

    static public String ensureTerminalSlash(String url) {
        if (url == null || url.length() == 0) return "/";
        if (url.charAt(url.length() - 1) != '/') return url + "/";
        return url;
    }
}  