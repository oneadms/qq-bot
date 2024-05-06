package com.jky.qqbot.controller;

import com.jky.qqbot.common.holder.CaptchaHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QQBotController {
    @GetMapping("/captcha")
    public String captcha() {
        return CaptchaHolder.getUrl();
    }

    @GetMapping("ticket")
    public String ticket(String ticket) {
        CaptchaHolder.setTicket(ticket);
        return "ok";
    }
    @GetMapping("smsCaptcha")
    public String smsCaptcha(String smsCaptcha) {
        CaptchaHolder.setSmsCaptcha(smsCaptcha);
        return "ok";
    }
}
