package com.liteng1220.lyt.utility;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class TextConverter {

    public static String encode(String text) {
        if (text == null) {
            return null;
        }

        byte[] gzipBytes = compress(text);
        StringBuilder base64Str = new StringBuilder(Base64.encodeToString(gzipBytes, Base64.NO_WRAP));
        return insertChar(base64Str);
    }

    public static String decode(String text) {
        if (text == null) {
            return null;
        }

        String base64Str = filterChar(text);
        return decompress(Base64.decode(base64Str.getBytes(), Base64.NO_WRAP));
    }

    private static String decompress(byte[] decodeByteArray) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream inputStream = null;
        try {
            inputStream = new InflaterInputStream(new ByteArrayInputStream(decodeByteArray));
            byte[] buffer = new byte[8192];
            int len;
            while ((len = inputStream.read(buffer)) > 0)
                byteArrayOutputStream.write(buffer, 0, len);
            return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.close();
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String filterChar(String text) {
        int len = text.length();
        if (len > 5) {
            StringBuilder stringBuilder = new StringBuilder(text);
            stringBuilder.deleteCharAt((len - 4) - 1);
            stringBuilder.deleteCharAt((len - 4) / 2 + 1);
            stringBuilder.deleteCharAt((len - 4) / 2 - 1);
            stringBuilder.deleteCharAt(3);
            return stringBuilder.toString();
        }

        return text;
    }

    private static String insertChar(StringBuilder base64Str) {
        if (base64Str == null) {
            return null;
        }

        char word1 = (char) (new Random().nextInt(25) + 65);
        char word2 = (char) (new Random().nextInt(25) + 65);
        int length = base64Str.length();
        if (length > 1) {
            base64Str.insert(3, word1);
            base64Str.insert(length / 2 - 1, word1);
            base64Str.insert(length / 2 + 1, word2);
            base64Str.insert(length - 1, word2);
        }

        return base64Str.toString();
    }

    public static byte[] compress(String text) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStream outputStream = null;
        try {
            outputStream = new DeflaterOutputStream(byteArrayOutputStream);
            outputStream.write(text.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

}
