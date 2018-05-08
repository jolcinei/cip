package com.jaks.cip.visao;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jolcinei
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Auxiliar {

    private PublicKey publicKey;
    private PrivateKey privateKey;

    static final int TAMANHO_BUFFER = 2048;

    public Auxiliar() {
    }
/*   
    public Auxiliar(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public KeyPair genKeyPair() throws NoSuchAlgorithmException, IOException {
        String fileBase = "D:\\xml\\chave_gerada";
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(TAMANHO_BUFFER);
        KeyPair keyPair = generator.generateKeyPair();
        try (FileOutputStream out = new FileOutputStream(fileBase + ".key")) {
            out.write(keyPair.getPrivate().getEncoded());
        }

        try (FileOutputStream out = new FileOutputStream(fileBase + ".pub")) {
            out.write(keyPair.getPublic().getEncoded());
        }
        return keyPair;
    }

    public String criptografar(String string) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return new String(cipher.doFinal(string.getBytes()));
    }

    public String descriptografar(String string) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(string.getBytes()));
    }

    public void processarArquivo(Cipher cipher, String file, String fileOut) throws javax.crypto.IllegalBlockSizeException,
            javax.crypto.BadPaddingException,
            java.io.IOException {
        try (FileInputStream in = new FileInputStream(file);
                FileOutputStream out = new FileOutputStream(fileOut)) {
            processFile(cipher, in, out);
        }
    }

    public void processFile(Cipher ci, FileInputStream in, FileOutputStream out) throws javax.crypto.IllegalBlockSizeException,
            javax.crypto.BadPaddingException,
            java.io.IOException {

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
            System.out.println("Maked: " + out.toString());
        }
    }
*/
    public void compactarParaZip(String arqSaida, String arqEntrada) throws IOException {
        int cont;
        byte[] dados = new byte[TAMANHO_BUFFER];
        BufferedInputStream origem;
        FileInputStream streamDeEntrada;
        FileOutputStream destino;
        ZipOutputStream saida;
        ZipEntry entry;

        try {
            destino = new FileOutputStream(new File(arqSaida));
            saida = new ZipOutputStream(new BufferedOutputStream(destino));
            File file = new File(arqEntrada);
            streamDeEntrada = new FileInputStream(file);
            origem = new BufferedInputStream(streamDeEntrada, TAMANHO_BUFFER);
            entry = new ZipEntry(file.getName());
            saida.putNextEntry(entry);

            while ((cont = origem.read(dados, 0, TAMANHO_BUFFER)) != -1) {
                saida.write(dados, 0, cont);
            }
            origem.close();
            saida.close();
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    public void compactarParaGZip(String arqEntrada, String arqSaida) throws IOException {
        int cont;
        byte[] dados = new byte[TAMANHO_BUFFER];

        BufferedInputStream origem;
        FileInputStream streamDeEntrada;
        FileOutputStream destino;
        GZIPOutputStream saida;
        try {
            destino = new FileOutputStream(new File(arqSaida));
            saida = new GZIPOutputStream(destino);
            File file = new File(arqEntrada);
            streamDeEntrada = new FileInputStream(file);
            origem = new BufferedInputStream(streamDeEntrada, TAMANHO_BUFFER);
            while ((cont = streamDeEntrada.read(dados)) != -1) {
                saida.write(dados, 0, cont);
            }
            origem.close();
            saida.close();
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    private static void compressGzipFile(String file, String gzipFile) {
        try {
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(gzipFile);
            GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                gzipOS.write(buffer, 0, len);
            }
            gzipOS.close();
            fos.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
