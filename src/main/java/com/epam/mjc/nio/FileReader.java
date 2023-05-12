package com.epam.mjc.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;


public class FileReader {

    public Profile getDataFromFile(File file) {
        Profile profile = new Profile();
        try (RandomAccessFile aFile     = new RandomAccessFile(file, "r");
             FileChannel      inChannel = aFile.getChannel()) {
            ByteBuffer buf = ByteBuffer.allocate(48);
            byte[] data = new byte[(int) file.length()];

            int bytesRead = inChannel.read(buf);
            int index = 0;
            if (bytesRead != -1) {
                buf.flip();
                while(buf.hasRemaining()) {
                    data[index++] = buf.get();
                }
            }

            String str = new String(data, StandardCharsets.UTF_8);
            String[] lines = str.split("\n");

            for (String line : lines) {
                String[] parts = line.split(": ");
                parts[1] = parts[1].trim();
                switch (parts[0]) {
                    case "Name":
                        profile.setName(parts[1]);
                        break;
                    case "Age":
                        profile.setAge(Integer.parseInt(parts[1]));
                        break;
                    case "Email":
                        profile.setEmail(parts[1]);
                        break;
                    case "Phone":
                        profile.setPhone(Long.parseLong(parts[1]));
                        break;
                    default:
                        throw new IOException("wrong key in the given profile");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return profile;
    }
}
