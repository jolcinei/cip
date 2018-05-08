/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaks.cip.visao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Jolcinei
 */
public class CriptografiaAES {

    static SecureRandom srandom = new SecureRandom();

    static private void processFile(Cipher ci, InputStream in, OutputStream out) throws IllegalBlockSizeException, BadPaddingException, IOException {
        byte[] ibuf = new byte[1024];
        int len;
        while ((len = in.read(ibuf)) != -1) {
            byte[] obuf = ci.update(ibuf, 0, len);
            if (obuf != null) {
                out.write(obuf);
            }
        }
        byte[] obuf = ci.doFinal();
        if (obuf != null) {
            out.write(obuf);
        }
    }

    static private void processFile(Cipher ci, String inFile, String outFile) throws IllegalBlockSizeException, BadPaddingException, IOException {
        try (FileInputStream in = new FileInputStream(inFile);
                FileOutputStream out = new FileOutputStream(outFile)) {
            processFile(ci, in, out);
        }
    }

    //Encriptar arquivos menores, como mensagens por exemplo
    public void criptografar(PrivateKey pvt, String file) throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, BadPaddingException, InvalidKeyException, IllegalBlockSizeException, IOException {

        String inputFile = file;
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pvt);
        processFile(cipher, inputFile, inputFile + ".spb");
    }

    public void descriptografar(PublicKey pubKey, String file) throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, BadPaddingException, InvalidKeyException, IllegalBlockSizeException, IOException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        processFile(cipher, file, file + ".dec");
    }

    public void criptografarRSAcomAES(PrivateKey pvt, String file) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException,
            IOException {

        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        SecretKey skey = kgen.generateKey();

        byte[] iv = new byte[128 / 8];
        srandom.nextBytes(iv);
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        try (FileOutputStream out = new FileOutputStream(file + ".spb")) {
            {
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.ENCRYPT_MODE, pvt);
                byte[] b = cipher.doFinal(skey.getEncoded());
                out.write(b);
            }

            out.write(iv);

            Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
            ci.init(Cipher.ENCRYPT_MODE, skey, ivspec);
            try (FileInputStream in = new FileInputStream(file)) {
                processFile(ci, in, out);
            }
        }
    }

    public void descriptografarRSAcomAES(PublicKey pubKey, String file) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, IOException {

        try (FileInputStream in = new FileInputStream(file)) {
            SecretKeySpec skey = null;
            {
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.DECRYPT_MODE, pubKey);
                byte[] b = new byte[128];
                in.read(b);
                byte[] keyb = cipher.doFinal(b);
                skey = new SecretKeySpec(keyb, "AES");
            }

            byte[] iv = new byte[128 / 8];
            in.read(iv);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
            ci.init(Cipher.DECRYPT_MODE, skey, ivspec);

            try (FileOutputStream out = new FileOutputStream(file + ".dec")) {
                processFile(ci, in, out);
            }
        }
    }

}
