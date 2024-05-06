package com.jky.qqbot.common.config;

import com.jky.qqbot.bean.BotFactoryBean;
import com.jky.qqbot.common.holder.CaptchaHolder;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.auth.BotAuthorization;
import net.mamoe.mirai.auth.QRCodeLoginListener;
import net.mamoe.mirai.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@EnableConfigurationProperties(QQBotProperties.class)
@Slf4j
public class QQBotConfiguration   {

    @Bean
    public BotConfiguration botConfiguration(QQBotProperties qqBotProperties) {
        BotConfiguration botConfiguration = BotConfiguration.getDefault();
        botConfiguration.setProtocol(qqBotProperties.getType());
        botConfiguration.setDeviceInfo(bot -> DeviceInfo.random());
        botConfiguration.setLoginSolver(new LoginSolver() {
            @Nullable
            @Override
            public Object onSolvePicCaptcha(@NotNull Bot bot, @NotNull byte[] bytes, @NotNull Continuation<? super String> continuation) {
                return null;
            }

            @Nullable
            @Override
            public Object onSolveDeviceVerification(@NotNull Bot bot, @NotNull DeviceVerificationRequests requests, @NotNull Continuation<? super DeviceVerificationResult> $completion) {
                DeviceVerificationRequests.SmsRequest sms = requests.getSms();
                log.info("需要设备认证");

                sms.requestSms(new Continuation(){
                    @NotNull
                    @Override
                    public CoroutineContext getContext() {
                        return EmptyCoroutineContext.INSTANCE;
                    }

                    @Override
                    public void resumeWith(@NotNull Object o) {

                        log.info("短信发送完成:{}",o.toString());

                    }
                });
                return sms.solved(CaptchaHolder.getSmsCaptcha());
            }

            @NotNull
            @Override
            public QRCodeLoginListener createQRCodeLoginListener(@NotNull Bot bot) {
                return new StandardCharImageLoginSolver().createQRCodeLoginListener(bot);
            }

            @Nullable
            @Override
            public Object onSolveSliderCaptcha(@NotNull Bot bot, @NotNull String url, @NotNull Continuation<? super String> continuation) {
                log.info("需要验证码认证:{}", url);
                return CaptchaHolder.getTicket(url);
            }
        });
        return botConfiguration;
    }

    @Bean
    public BotFactoryBean botFactoryBean(QQBotProperties qqBotProperties,BotConfiguration botConfiguration) {
        log.info("QQ Bot Config:{}",qqBotProperties.toString());
        return new BotFactoryBean(qqBotProperties,botConfiguration);
    }



}
