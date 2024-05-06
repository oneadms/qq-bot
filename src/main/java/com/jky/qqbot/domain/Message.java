package com.jky.qqbot.domain;

import com.jky.qqbot.entity.MdReplyMessage;
import lombok.ToString;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
@ToString
public class Message extends MdReplyMessage  {
    public void send(Contact contact) {
        String messageType = getMessageType();
        if ("image".equals(messageType)) {


            Contact.sendImage(contact, url2InputStream(getMessage()));
        }
        if ("text".equals(messageType)) {
            contact.sendMessage( getMessage());
        }
    }
    public void send(Group group) {
        String messageType = getMessageType();
        if ("image".equals(messageType)) {


            Contact.sendImage(group, url2InputStream(getMessage()));
        }
        if ("text".equals(messageType)) {
            group.sendMessage( getMessage());
        }
    }

    private InputStream url2InputStream(String imageUrl) {
        InputStream inputStream = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return inputStream;
    }





}
