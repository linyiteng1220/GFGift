package com.liteng1220.lyt.utility;

import android.annotation.SuppressLint;
import android.util.Base64;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Random;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class LytEncryption {
    private final static byte[] TAG_KEY = {-77, -76, 45, -93, -75, 59, 56, 48, 54, 62, 50, -69, -66, -65, -74, -35}; // zlt_istudytagkey

    private final static byte[][] AES_KEY_POOL = {
            {-77, 48, 49, -104, 28, 55, 54, 29, 19, 16, 24, -66, -73, -61, -52, -90},
            {29, -65, -101, 19, -77, -79, 56, 51, 48, -71, -68, -69, 48, 49, 80, 11},
            {52, -72, -65, -70, 50, 29, 28, 26, 27, 58, -77, 20, 17, 52, -71, 102},
            {-105, 22, 76, 51, 63, -67, 60, 57, -67, -73, 52, 19, 19, -69, -79, -56},
            {-62, 56, 21, -98, -100, -100, -79, 59, 53, -69, -80, -76, -73, -80, -71, -100},
            {57, 53, -109, 61, -110, -66, 57, -72, 58, 53, 52, 61, -109, 29, -74, 22},
            {20, 60, 48, 62, -67, 59, 26, -108, 23, -72, -108, -78, -71, -67, 24, 43},
            {22, 26, 59, 63, -78, -67, -111, 26, 57, 49, 51, 58, -102, 17, -65, -121},
            {-66, 55, -72, -76, -106, 19, -72, -73, 24, 21, 63, 18, -107, 60, 53, 95},
            {24, -109, 17, 18, 17, 19, 17, 22, 58, -72, -78, -66, -79, -79, 53, 2},
            {49, 54, -75, -67, -74, -79, -74, 55, 61, 63, 21, 17, 57, 61, 52, 26},
            {62, 18, 20, -111, 24, 55, -67, 55, 48, -65, -73, -71, 49, 53, 30, -67},
            {49, 59, -109, -98, -78, -74, 51, -79, 30, -107, -78, -77, 49, -104, 26, -101},
            {-79, -77, 50, 48, -77, -73, 57, 50, -66, -72, 55, -107, -100, -72, -80, -44},
            {-66, 49, -67, -101, -107, 62, 18, 27, -77, 19, 26, -73, 62, 57, 16, 125},
            {-18, -106, 23, -97, 52, -69, -78, 58, -77, -111, 20, -75, 52, -79, -73, -126},
            {16, 50, 28, 18, -67, 23, 28, 51, -65, 56, -73, 50, -65, -75, 62, -115},
            {-65, 60, 52, 54, -69, -76, 63, -74, 52, -76, 60, -71, -71, 52, -66, -43},
            {57, -68, 60, 54, -75, -105, -112, -71, -106, -68, -71, 23, 22, -99, -65, 30},
            {68, 49, -69, -73, 59, -65, 19, -69, -108, 53, 53, 53, -72, -71, 60, -37},
            {49, -80, 22, -98, -79, -65, 55, 24, -69, -100, 50, -68, 22, -98, -71, 27},
            {56, -80, -67, -76, 26, 16, -68, 52, -77, 16, 59, 58, -101, 24, -107, 17},
            {-69, -76, -68, 48, -101, 26, 56, -80, -65, 53, 60, 61, -67, -97, 23, 81},
            {-111, 17, 51, 48, 51, 25, -108, 57, -71, -68, -65, -67, 19, -102, -78, 65},
            {-78, 55, -71, 50, -98, 27, -69, 24, 56, 30, 57, 57, 53, -71, -72, 80},
            {16, -67, 58, 59, -78, -66, -65, 51, -77, 53, 53, 62, -101, -105, -77, -115},
            {-69, 63, -97, -107, -67, -65, 62, -73, 50, -66, -77, -80, 17, 18, -107, -7},
            {24, -77, 55, 51, -73, 52, 20, 21, -69, -107, 16, 51, -103, 18, 62, -127},
            {-70, -106, -108, -73, 55, -77, 30, 20, -107, 59, 27, -72, 54, 62, 57, -41},
            {48, 54, -110, -105, 63, -112, -72, -103, -76, -66, 50, -103, 29, -79, 58, 23},
            {-78, 52, -61, 18, 100, 60, -80, 51, 48, 57, 51, -69, -72, 49, -66, -43},
            {59, 27, 27, -74, 55, 68, 51, -69, -72, -75, -76, 57, -79, -72, -75, 20}
    };

    private static final String CHARS = "0123456789abcdefghijklmnopqrxtuvwxyzABCDEFGHIJKLMNOPQRXTUVWXYZ";
    private static final byte[] IV = {-104, -101, -26, -19, -109, 24, -101, -104, -24, -98, 31, -25, -103, 30, 88, 39};

    private LytEncryption() {
    }

    public static String encryptJsonData(String jsonData) {
        StringBuilder stringBuilder = new StringBuilder();
        int len = CHARS.length();
        int indexSum = 0;
        for (int k = 0; k < len; k++) {
            int randomIndex = new Random().nextInt(len);
            char detail = CHARS.charAt(randomIndex);
            if (k == 4 || k == 9 || k == 16 || k == 25) {
                indexSum += detail;
            }
            stringBuilder.append(detail);
        }

        int index = indexSum % 32;
        final byte[] AES_KEY = AES_KEY_POOL[index];

        try {
            SecretKey keyForTag = new SecretKeySpec(decodeBytesToString(TAG_KEY).getBytes(), decodeBytesToString(new byte[]{-70, -77, 76}));
            byte[] encryptTag = encrypt(stringBuilder.toString().getBytes(), keyForTag);
            String finalTag = new String(Base64.encode(encryptTag, Base64.NO_WRAP));

            SecretKey keyForJson = new SecretKeySpec(decodeBytesToString(AES_KEY).getBytes(), decodeBytesToString(new byte[]{-70, -77, 76}));
            byte[] encryptValue = encrypt(compress(jsonData), keyForJson);
            String finalValue = new String(Base64.encode(encryptValue, Base64.NO_WRAP));

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(finalTag, finalValue);

            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String decryptJsonData(String jsonData) {
        try {
            String jsonKey = null;
            String jsonValue = null;

            JSONObject jsonObject = new JSONObject(jsonData);
            Iterator<String> it = jsonObject.keys();
            if (it.hasNext()) {
                jsonKey = it.next();
            }

            if (jsonKey != null) {
                jsonValue = jsonObject.optString(jsonKey);
            }

            if (jsonKey == null || jsonValue == null) {
                return null;
            }

            SecretKey aesKey = new SecretKeySpec(decodeBytesToString(TAG_KEY).getBytes(), decodeBytesToString(new byte[]{-70, -77, 76}));
            String decryptedJsonKey = new String(decrypt(Base64.decode(jsonKey, Base64.NO_WRAP), aesKey));

            int indexSum = 0;
            int len = decryptedJsonKey.length();
            for (int k = 0; k < len; k++) {
                if (k == 4 || k == 9 || k == 16 || k == 25) {
                    char detail = decryptedJsonKey.charAt(k);
                    indexSum += detail;
                }
            }
            int index = indexSum % 32;
            final byte[] AES_KEY = AES_KEY_POOL[index];

            SecretKey finalAesKey = new SecretKeySpec(decodeBytesToString(AES_KEY).getBytes(), decodeBytesToString(new byte[]{-70, -77, 76}));
            return decompress(decrypt(Base64.decode(jsonValue, Base64.NO_WRAP), finalAesKey));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static byte[] encodeString(String string) {
        byte[] byteArray = string.getBytes();
        byte[] outputArray = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            if (i == byteArray.length - 1)
                outputArray[i] = rotateLeftByte((byte) (byteArray[i] ^ outputArray[0] ^ 0x71), 7);
            else
                outputArray[i] = rotateLeftByte((byte) (byteArray[i] ^ byteArray[i + 1] ^ 0x71), 7);
        }

        return outputArray;
    }

    private static String decodeBytesToString(byte[] byteArray) {
        byte[] tempArray = new byte[byteArray.length];
        byte[] outputArray = new byte[byteArray.length];
        for (int i = byteArray.length - 1; i >= 0; i--)
            tempArray[i] = byteArray[i];

        for (int i = byteArray.length - 1; i >= 0; i--) {
            tempArray[i] = rotateRightByte(byteArray[i], 7);
            if (i == tempArray.length - 1)
                outputArray[i] = (byte) (tempArray[i] ^ tempArray[0] ^ 0x71);
            else
                outputArray[i] = (byte) (tempArray[i] ^ outputArray[i + 1] ^ 0x71);
        }
        return new String(outputArray);
    }

    @SuppressLint("TrulyRandom")
    private static byte[] encrypt(byte[] bytes, SecretKey aesKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(decodeBytesToString(new byte[]{-70, -77, -122, -114, 56, 56, -114, 7, 53, -68, -80, -117, 10, 32, 58, -72, 62, 59, 60, 86})); // AES/CBC/PKCS5Padding
        IvParameterSpec ivSpec = new IvParameterSpec(decodeBytesToString(IV).getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);
        return cipher.doFinal(bytes);
    }

    private static byte[] decrypt(byte[] bytes, SecretKey aesKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(decodeBytesToString(new byte[]{-70, -77, -122, -114, 56, 56, -114, 7, 53, -68, -80, -117, 10, 32, 58, -72, 62, 59, 60, 86})); // AES/CBC/PKCS5Padding
        IvParameterSpec ivSpec = new IvParameterSpec(decodeBytesToString(IV).getBytes());
        cipher.init(Cipher.DECRYPT_MODE, aesKey, ivSpec);
        return cipher.doFinal(bytes);
    }

    private static byte rotateLeftByte(byte bt, int n) {
        int temp = bt & 0xFF;
        return (byte) (temp << n | temp >>> (8 - n));
    }

    private static byte rotateRightByte(byte bt, int n) {
        int temp = bt & 0xFF;
        return (byte) (temp >>> n | temp << (8 - n));
    }

    private static byte[] compress(String text) {
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

    private static String decompress(byte[] textByteArray) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream inputStream = null;
        try {
            inputStream = new InflaterInputStream(new ByteArrayInputStream(textByteArray));
            byte[] buffer = new byte[8192];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
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
}
