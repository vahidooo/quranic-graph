package server.util;

import java.text.MessageFormat;
import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: abolfazl
 * Date: 5/25/12
 * Time: 12:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class LocaleUtils {
    public static boolean isLocalRtl(Locale locale) {
        if (locale == null)
            return false;

        if (locale.getCountry().equalsIgnoreCase("iran")
                || locale.getCountry().equalsIgnoreCase("IR")
                || locale.getLanguage().equalsIgnoreCase("fa")
                || locale.getDisplayLanguage().equalsIgnoreCase("persian"))
            return true;

        return false;
    }

    public static List<String> getBundleKeys(String regexFilter) {
        List<String> results = new ArrayList<String>();

        Enumeration<String> keys = getBundle().getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (key.matches(regexFilter))
                results.add(key);
        }

        return results;
    }

    public static String getText(String key) {
        String message;

        try {
            message = getBundle().getString(key);
        } catch (MissingResourceException mre) {
            return "???" + key + "???";
        }

        return message;
    }

    public static String getText(String key, Object arg) {
        if (arg == null) {
            return getText(key);
        }

        try {
            MessageFormat form = new MessageFormat(getBundle().getString(key));

            if (arg instanceof String) {
                return form.format(new Object[]{arg});
            } else if (arg instanceof Object[]) {
                return form.format(arg);
            } else {
                return "";
            }
        } catch (MissingResourceException mre) {
            return "???" + key + "???";
        }
//        return "getTextFromBundle NOT IMPLEMENTED!";
    }

    private static ResourceBundle getBundle() {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        Locale locale = null;
//        try {
//            locale = ((HttpServletRequest)facesContext.getExternalContext().getRequest()).getLocale();
//        } finally {
//            if (locale == null) {
//                locale = new Locale("fa", "IR");
//            }
//        }
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ResourceBundle b = ResourceBundle.getBundle("ApplicationResources"/*facesContext.getApplication().getMessageBundle()*/, new Locale("fa", "IR"), classLoader);
        return b;
    }

    public static String correctKF(String s) {
        boolean needed = false;

        StringBuffer sb = new StringBuffer(s.length());
        char[] sc = s.toCharArray();
        for (int i = 0; i < sc.length; i++) {
            char cc = correctFarsiChar(sc[i]);
            sb.append(cc);
            if (cc != sc[i]) {
                needed = true;
            }
        }

        return needed ? sb.toString() : s;
//        return str.replaceAll("\u0638\u0679", "\u063a\u0152").replaceAll("\u0638\u0192", "\u0639\u00a9").toLowerCase();
    }

    private static char correctFarsiChar(char c) {
        int keyCode = (int) c;

        if (keyCode >= 1776 && keyCode <= 1785) {
            return (char) (keyCode - 1776 + (int) '0');
        } else if (keyCode == 1610) {    //bad ya
            return (char) 1740;    //good ya
        } else if (keyCode == 1603) {    //bad kaf
            return (char) 1705;    //good kaf
        }

        return c;
    }

    public static String correctKF_toArabic(String s) {
        boolean needed = false;

        StringBuffer sb = new StringBuffer(s.length());
        char[] sc = s.toCharArray();
        for (int i = 0; i < sc.length; i++) {
            char cc = correctFarsiChar_toArabic(sc[i]);
            sb.append(cc);
            if (cc != sc[i]) {
                needed = true;
            }
        }

        return needed ? sb.toString() : s;
    }

    private static char correctFarsiChar_toArabic(char c) {
        int keyCode = (int) c;

        if (keyCode >= 1776 && keyCode <= 1785) {
            return (char) (keyCode - 1776 + (int) '0');
        } else if (keyCode == 1740) {    //bad ya
            return (char) 1610;    //good ya
        } else if (keyCode == 1603) {    //bad kaf
            return (char) 1705;    //good kaf
        }

        return c;
    }
}
