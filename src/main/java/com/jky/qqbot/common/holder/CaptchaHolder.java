package com.jky.qqbot.common.holder;

public class CaptchaHolder {

    private static String url;
    private static String ticket;
    private static String smsCaptcha;

    public static void setSmsCaptcha(String smsCaptcha) {
        CaptchaHolder.smsCaptcha = smsCaptcha;
        synchronized (CaptchaHolder.class) {
            CaptchaHolder.class.notify();
        }
    }

    public static void setTicket(String ticket) {
        CaptchaHolder.ticket = ticket;
        synchronized (CaptchaHolder.class) {
            CaptchaHolder.class.notify();
        }
    }

    public static String getUrl() {
        return url;
    }

    public static Object getTicket(String url) {

        CaptchaHolder.url = url;
        try {
            synchronized (CaptchaHolder.class) {
                CaptchaHolder.class.wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ticket;
    }

    public static String getSmsCaptcha() {
        try {
            synchronized (CaptchaHolder.class) {
                CaptchaHolder.class.wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return smsCaptcha;
    }

    public static void clear() {
        CaptchaHolder.setSmsCaptcha(null);
        CaptchaHolder.setTicket(null);
    }
}
