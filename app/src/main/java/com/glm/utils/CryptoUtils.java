package com.glm.utils;


import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;


/**
 * A utility class that encrypts or decrypts a file.
 *
 * @author www.codejava.net
 */
public class CryptoUtils {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    public static void encrypt(String key, File inputFile, File outputFile) {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }

    public static void decrypt(String key, File inputFile, File outputFile) {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }

    private static void doCrypto(int cipherMode, String key, File inputFile,
                                 File outputFile) {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            Log.e(CryptoUtils.class.getClass().getCanonicalName(), "Error encrypting/decrypting file");
        }
    }

    public static byte[] crypt(String key, String toCrypt) {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] crypted = cipher.doFinal(toCrypt.getBytes());

            return crypted;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] decrypt(String key, byte[] toDecrypt) {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decrypted = cipher.doFinal(toDecrypt);

            return decrypted;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}