package com.jky.qqbot.listener;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jky.qqbot.common.enums.UserType;
import com.jky.qqbot.domain.Message;
import com.jky.qqbot.entity.MdDictonary;
import com.jky.qqbot.entity.MdReplyMessage;
import com.jky.qqbot.entity.MdUser;
import com.jky.qqbot.event.BotStartedEvent;
import com.jky.qqbot.mapper.MdDictonaryMapper;
import com.jky.qqbot.mapper.MdReplyMessageMapper;
import com.jky.qqbot.mapper.MdUserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.*;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.event.events.MemberLeaveEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.SingleMessage;
import net.mamoe.mirai.network.BotAuthorizationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
@Slf4j
@AllArgsConstructor
@Component
@Scope("prototype")
public class BotProcessListener implements  Runnable{


    private static Set<String> keywords=new HashSet<>();
    private final MdDictonaryMapper dictonaryMapper;
    private MdUserMapper userMapper;
    private ApplicationContext applicationContext;
    private MdReplyMessageMapper mdReplyMessageMapper;
    private Bot bot;



    public static void main(String[] args) {
    }
    @Override
    public void run() {
        try {
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
                    List<MdReplyMessage> mdReplyMessages = mdReplyMessageMapper.selectList(Wrappers.lambdaQuery());
                    List<Message> messageList = toMessageList(mdReplyMessages);
                    for (Message message : messageList) {
                        message.send(group);
                    }
                    member.nudge();
                    member.sendMessage("您好，请发送您的技术栈给我，尔后我们将会为你定制推单");

                }
            });

            GlobalEventChannel.INSTANCE.subscribeAlways(GroupTempMessageEvent.class, this::process);
            GlobalEventChannel.INSTANCE.subscribeAlways(FriendMessageEvent.class, this::process);
            AtomicBoolean flag = new AtomicBoolean(false);
            GlobalEventChannel.INSTANCE.subscribeAlways(FriendMessageEvent.class, event -> {
                MessageChain message = event.getMessage();

                String code = message.serializeToMiraiCode();


                if (code.contains("image")) {

                    String imageId = code.substring(13, code.length() - 1);
                    if (flag.get()) {
                        MdReplyMessage entity = new MdReplyMessage();
                        entity.setMessage(imageId);
                        mdReplyMessageMapper.insert(entity);
                    }
//                    Image image = getImage(imageId);
                    log.info("接收到一条图片:{}", code);
//                    event.getSubject().sendMessage(image);
                } else {
                    log.info("接收到一条消息:{}", code);
                }
                if ("开始配置".equals(code)) {
                    flag.set(true);
                }
                if ("配置结束".equals(code)) {
                    flag.set(false);
                }


            });
            GlobalEventChannel.INSTANCE.subscribeAlways(MemberLeaveEvent.class,event->{
                Member member = event.getMember();
                member.sendMessage("为什么退群呢 是看群人数太少了吗？～ 工作室刚刚起步 需要您的加入呢");
                member.sendMessage("现在工作室单量还是有的,不用担心哈");
                member.nudge();
            });
        } catch (Exception e) {
            if (e instanceof BotAuthorizationException|| e instanceof IllegalStateException) {
                applicationContext.publishEvent(new BotStartedEvent("机器人重启中"));
            }
            log.error(e.toString());
        }
    }

    private void process(FriendMessageEvent event) {
        Iterator<SingleMessage> iterator = event.getMessage().iterator();

        while (iterator.hasNext()) {
            SingleMessage next = iterator.next();
            if (next.contentToString().contains("重载字典")) {
                initData();
                String keywordStr = keywords.stream().collect(Collectors.joining("\n"));
                event.getSubject().sendMessage("重载完成,当前字典信息如下:\n" + keywordStr);
            }
            for (String keyword : keywords) {
                String technologyStack = next.contentToString();

                if (technologyStack.toLowerCase().contains(keyword.toLowerCase())) {
                    Friend member = event.getSender();
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
                Friend member = event.getSender();
                String phone = next.contentToString();
                member.sendMessage("嗯嗯，我们已收到你的联系方式：" + phone);
                MdUser mdUser = userMapper.selectById(member.getId());
                mdUser.setPhone(phone);
                userMapper.updateById(mdUser);
            }
        }
    }
    private void process(GroupTempMessageEvent event) {
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
    }

    private List<Message> toMessageList(List<MdReplyMessage> mdReplyMessages) {
        List<Message> list = mdReplyMessages.stream().sorted(Comparator.comparing(MdReplyMessage::getSeq))
                .map(rm->
                {
                    Message message = new Message();
                    BeanUtils.copyProperties(rm, message);
                    return message;
            }).collect(Collectors.toList());
        return list;

    }


    @NotNull
    private static Image getImage(String imageId) {
        Image.Builder builder = Image.newBuilder(imageId);

        builder.setWidth(1080);
        builder.setHeight(1920);
        builder.setEmoji(false);
        Image image = builder.build();
        return image;
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
    private static MdUser toUser(String technologyStack, Friend member, long id) {
        return new MdUser().setUserId(id + "").setUserName(member.getNick()).setUserType(UserType.QQ.getVal()).setTechnologyStack(technologyStack);
    }


}
