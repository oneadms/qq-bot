package com.jky.qqbot.init;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jky.qqbot.common.enums.UserType;
import com.jky.qqbot.entity.MdDictonary;
import com.jky.qqbot.entity.MdUser;
import com.jky.qqbot.mapper.MdDictonaryMapper;
import com.jky.qqbot.mapper.MdUserMapper;
import com.jky.qqbot.service.IMdDictonaryService;
import com.jky.qqbot.service.md.MdCacheService;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.message.data.SingleMessage;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class QQBotInit implements ApplicationListener<ApplicationReadyEvent>  {

    private final Bot bot;

    private static Set<String> keywords=new HashSet<>();
    private final MdDictonaryMapper  dictonaryMapper;
    private  MdUserMapper userMapper;

    public QQBotInit(Bot bot, MdUserMapper userMapper, MdDictonaryMapper dictonaryMapper) {

        this.bot = bot;
        this.userMapper = userMapper;
        this.dictonaryMapper = dictonaryMapper;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent readyEvent) {
        initData();
        bot.login();

        ContactList<Group> groups = bot.getGroups();
        //获取当前管理的群
        List<Group> manageGroups = groups.stream().
                filter(g -> g.getBotPermission().getLevel() > 0).collect(Collectors.toList());
        List<Long> manageGroupIds = manageGroups.stream().map(Group::getId).collect(Collectors.toList());
        GlobalEventChannel.INSTANCE.subscribeAlways(MemberJoinEvent.class, event -> {
            Group group = event.getGroup();
            if (manageGroupIds.contains(group.getId())) {
                NormalMember member = event.getMember();
                String nickname = member.getNick();

                group.sendMessage("欢迎" + nickname + "加入本群,请扫码加入企业微信群");
                File companyWechatGroupFile = new File(System.getProperty("user.dir") + "/images" + "/companyWechatGroup.png");
                File wechatGroupFile = new File(System.getProperty("user.dir") + "/images" + "/wechatGroup.png");
                Contact.sendImage(group, companyWechatGroupFile);
                Contact.sendImage(group, wechatGroupFile);
                group.sendMessage("请先扫描第一个二维码加入企业后，扫描第二个二维码在微信上接收企业微信消息");
                member.nudge();//戳一戳
                member.sendMessage("您好，请发送您的技术栈给我，尔后我们将会为你定制推单");

            }
        });

        GlobalEventChannel.INSTANCE.subscribeAlways(GroupTempMessageEvent.class, event -> {
            Iterator<SingleMessage> iterator = event.getMessage().iterator();

            while (iterator.hasNext()) {
                SingleMessage next = iterator.next();

                    for (String keyword : keywords) {
                        String technologyStack = next.contentToString();
                        if (technologyStack.toLowerCase().contains(keyword.toLowerCase())) {
                            NormalMember member = event.getSender();
                            long id = member.getId();
                            MdUser toUser = toUser(technologyStack, member, id);
                            userMapper.insert(toUser);
                            member.sendMessage("好的，您的技术栈为" + technologyStack);
                            member.sendMessage("方便留下你的手机号码？留下电话号码接下来有符合你的单子将会优先联系你哟");
                            break;
                        }

                    }
              String regex = "^1[3456789]\\d{9}$";
                if (next.contentToString().matches(regex)) {
                    NormalMember member = event.getSender();
                    member.sendMessage("嗯嗯，我们已收到你的联系方式：" + next.contentToString());
                }
                }

        });
    }

    private void initData() {
        log.info("开始加载技术栈关键词");
        List<MdDictonary> keyWord = dictonaryMapper.selectList(Wrappers.lambdaQuery(MdDictonary.class)

                .eq(MdDictonary::getDicName, "KeyWord")
                .eq(MdDictonary::getIsDel, "0"));
        for (MdDictonary mdDictonary : keyWord) {
            String dicKey = mdDictonary.getDicKey();
            log.info("关键词:{}", dicKey);
            keywords.add(dicKey);
        }

    }

    private static MdUser toUser(String technologyStack, NormalMember member, long id) {
        return new MdUser().setUserId(id + "").setUserName(member.getNick()).setUserType(UserType.QQ.getVal()).setTechnologyStack(technologyStack);
    }


}
