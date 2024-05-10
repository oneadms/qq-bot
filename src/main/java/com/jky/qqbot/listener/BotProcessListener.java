package com.jky.qqbot.listener;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jky.qqbot.common.enums.UserType;
import com.jky.qqbot.domain.Message;
import com.jky.qqbot.entity.MdBlackList;
import com.jky.qqbot.entity.MdDictonary;
import com.jky.qqbot.entity.MdReplyMessage;
import com.jky.qqbot.entity.MdUser;
import com.jky.qqbot.event.BotStartedEvent;
import com.jky.qqbot.mapper.MdBlackListMapper;
import com.jky.qqbot.mapper.MdDictonaryMapper;
import com.jky.qqbot.mapper.MdReplyMessageMapper;
import com.jky.qqbot.mapper.MdUserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.SingleMessage;
import net.mamoe.mirai.network.BotAuthorizationException;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.*;
import java.util.List;
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
    private MdBlackListMapper blackListMapper;



    public static void main(String[] args) {
        String msg = "[mirai:at:721767431]  拉黑 装逼";
        String memberId = msg.substring(msg.indexOf("mirai:at") + "mirai:at".length()+1, msg.indexOf(']') );
        System.out.println(memberId);
        String reason = msg.substring(msg.lastIndexOf("拉黑") + "拉黑".length()+1).trim();
        System.out.println(reason);

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
            for (Group manageGroup : manageGroups) {
                ContactList<NormalMember> members = manageGroup.getMembers();
                for (NormalMember member : members) {
                    long id = member.getId();
                    MdBlackList blackList = blackListMapper.selectOne(Wrappers.lambdaQuery(MdBlackList.class).eq(MdBlackList::getUserId, id + ""));
                    if (blackList != null) {
                        String reason = blackList.getReason();
                        manageGroup.sendMessage("哦豁,"+  member.getNick()+"发现你被拉黑了呢 拉黑理由如下:"+ reason);
                        manageGroup.sendMessage("再见👋");
                        member.kick(reason);
                        return;
                    }
                }
            }
            List<Long> manageGroupIds = manageGroups.stream().map(Group::getId).collect(Collectors.toList());
            GlobalEventChannel.INSTANCE.subscribeAlways(MessageEvent.class,this::process);
            GlobalEventChannel.INSTANCE.subscribeAlways(MemberJoinEvent.class, event -> {
                Group group = event.getGroup();
                if (manageGroupIds.contains(group.getId())) {
                    NormalMember member = event.getMember();
                    long id = member.getId();

                    MdBlackList blackList = blackListMapper.selectOne(Wrappers.lambdaQuery(MdBlackList.class).eq(MdBlackList::getUserId, id + ""));
                    if (blackList != null) {
                        String reason = blackList.getReason();
                        group.sendMessage("哦豁 发现你被拉黑了呢 拉黑理由如下:"+ reason);
                        group.sendMessage("再见👋");
                        member.kick(reason);
                        return;
                    }
                    String nickname = member.getNick();
                    group.sendMessage("欢迎" + nickname + "加入本群,请扫码加入企业微信群");
                    List<MdReplyMessage> mdReplyMessages = mdReplyMessageMapper.selectList(Wrappers.lambdaQuery());
                    List<Message> messageList = toMessageList(mdReplyMessages);
                    for (Message message : messageList) {
                        message.send(group);
                    }
                    member.nudge();
                    member.sendMessage("您好，请发送您的技术栈给我，未来我们将会为你定制推单");

                }
            });
            GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, this::process);            GlobalEventChannel.INSTANCE.subscribeAlways(GroupTempMessageEvent.class, this::process);
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

        } catch (Exception e) {
            if (e instanceof BotAuthorizationException|| e instanceof IllegalStateException) {
                applicationContext.publishEvent(new BotStartedEvent("机器人重启中"));
            }
            log.error(e.toString());
        }
    }

    private void process(MessageEvent messageEvent) {
        String msg = messageEvent.getMessage().serializeToMiraiCode();
        if ("黑名单".equals(msg)) {
            List<MdBlackList> blackLists = blackListMapper.selectList(Wrappers.lambdaQuery());
            if (CollectionUtils.isEmpty(blackLists)) {
                return;
            }

            String blackListMessage = blackLists.stream().map(blackList -> "QQ: " + blackList.getUserId() + " 原因：" + blackList.getReason()
            ).collect(Collectors.joining("\n"));
            messageEvent.getSubject().sendMessage(blackListMessage);
        }
    }

    private void process(GroupMessageEvent groupMessageEvent) {
        MessageChain message = groupMessageEvent.getMessage();
        String msg = message.serializeToMiraiCode();
        Group group = groupMessageEvent.getGroup();
        log.info("收到一条群聊消息:{}", msg);
        boolean isManage = groupMessageEvent.getSender().getPermission().getLevel() > 0;
        if (msg.contains("mirai:at") && msg.contains("拉黑")&&isManage) {
            String blackUser = msg.substring(msg.indexOf("mirai:at") + "mirai:at".length() + 1, msg.indexOf(']'));

            ContactList<NormalMember> members = group.getMembers();
            for (NormalMember member : members) {
                long id = member.getId();

                if (Objects.equals(id + "", blackUser)) {
                    String reason = msg.substring(msg.lastIndexOf("拉黑") + "拉黑".length()+1).trim();
                    member.kick(reason, true);
                    MdBlackList blackList = blackListMapper.selectOne(
                            Wrappers.lambdaQuery(MdBlackList.class).eq(MdBlackList::getUserId, blackUser)
                    );
                    if (blackList == null) {
                        MdBlackList entity = new MdBlackList();
                        entity.setUserId(blackUser);
                        entity.setReason(reason);
                        blackListMapper.insert(entity);
                    }
                    break;
                }
            }
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
                MdUser mdUser = userMapper.selectOne(Wrappers.lambdaQuery(MdUser.class)
                        .eq(MdUser::getUserId,member.getId()));
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
                    MdUser mdUser = userMapper.selectOne(Wrappers.lambdaQuery(MdUser.class)
                            .eq(MdUser::getUserId, id));
                    if (mdUser != null) {
                        return; //用户已经存在
                    }
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
                String phone = next.contentToString();
                member.sendMessage("嗯嗯，我们已收到你的联系方式：" + phone);
                MdUser mdUser = userMapper.selectOne(Wrappers.lambdaQuery(MdUser.class)
                        .eq(MdUser::getUserId,member.getId()));
                mdUser.setPhone(phone);
                userMapper.updateById(mdUser);
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
