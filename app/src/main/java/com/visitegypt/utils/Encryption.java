package com.visitegypt.utils;

import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

public class Encryption {
    private static String accessToken;
    private static String salt = "ASsadads52dsfds4fds2fsd@";
    public Encryption(String accessToken) {
        this.accessToken = accessToken;
    }

    public Encryption() {
    }

    public void getAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }

    public static SecretKey generateKey(String password)
    {
        SecretKeyFactory factory = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey originalKey = null;
        try {
            originalKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return originalKey;
    }
    public static SecretKey generateKey()
          {
              SecretKeyFactory factory = null;
              try {
                  factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
              } catch (NoSuchAlgorithmException e) {
                  e.printStackTrace();
              }
              KeySpec spec = new PBEKeySpec(accessToken.toCharArray(), salt.getBytes(), 65536, 256);
              SecretKey originalKey = null;
              try {
                  originalKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
              } catch (InvalidKeySpecException e) {
                  e.printStackTrace();
              }

              return originalKey;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt(SecretKey secretKey) throws BadPaddingException, IllegalBlockSizeException {
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        Cipher cipher = null;
        String password = null;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

            return Base64
                    .getEncoder()
                    .encodeToString(cipher.doFinal(accessToken.getBytes(StandardCharsets.UTF_8)));

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertSecretKeyToString(SecretKey secretKey) {
        byte[] rawData = secretKey.getEncoded();
        String encodedKey = Base64.getEncoder().encodeToString(rawData);
        return encodedKey;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static SecretKey convertStringToSecretKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        return originalKey;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static  String decrypt(SecretKey secretKey , String strToDecrypt) throws BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = null;
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE,secretKey,ivspec);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    }

}
