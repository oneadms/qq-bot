package com.jky.qqbot.controller;

import com.jky.qqbot.bean.BotFactoryBean;
import com.jky.qqbot.common.enums.ProtocolType;
import com.jky.qqbot.common.holder.CaptchaHolder;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class QQBotController {
    @Resource
    BotConfiguration botConfiguration;
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



    @GetMapping("link/{type}")
    public void link(@PathVariable(name = "type") Integer type, HttpServletResponse response)  {
        try {
            if (type == 0) {
                 response.sendRedirect("http://weixin.qq.com/q/02r-Frwr_fae_1g0V2xC1v");
            }else if (type==1){
                response.sendRedirect("https://work.weixin.qq.com/m/5abecc2a8a04cda72e2b8527b1953286?is=174");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
