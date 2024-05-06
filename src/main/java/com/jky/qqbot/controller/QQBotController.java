package com.jky.qqbot.controller;

import com.jky.qqbot.bean.BotFactoryBean;
import com.jky.qqbot.common.enums.ProtocolType;
import com.jky.qqbot.common.holder.CaptchaHolder;
import net.mamoe.mirai.internal.network.protocol.LoginType;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    @GetMapping("switchLogin")
    public String switchLogin(@RequestParam(required = false) String type) {
        if (!StringUtils.isEmpty(type)) {
            botConfiguration.setProtocol(ProtocolType.of(type).getProtocol());
        }
        BotFactoryBean.flag = !BotFactoryBean.flag;
        return "ok";
    }
}
