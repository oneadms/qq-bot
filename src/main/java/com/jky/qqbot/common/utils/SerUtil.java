package com.jky.qqbot.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class SerUtil {
    public static void serializableObj(Map memberTechnologyStackMap, File file) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(memberTechnologyStackMap);
            objectOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
