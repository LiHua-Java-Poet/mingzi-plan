package org.example;// org.example.JarEncryptor.java

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

public class JarEncryptor {
    private static final int AES_KEY_BITS = 128;
    private static final int GCM_IV_LENGTH = 12; // bytes
    private static final int GCM_TAG_LENGTH = 128; // bits
    private static final int PBKDF2_ITER = 100_000;
    private static final int SALT_LEN = 16;

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Usage: java org.example.JarEncryptor <input.jar> <output.encrypted.jar> <password>");
            return;
        }
        Path in = Paths.get(args[0]);
        Path out = Paths.get(args[1]);
        char[] password = args[2].toCharArray();
        encryptJar(in, out, password);
        System.out.println("加密成功");
    }

    public static void encryptJar(Path inputJar, Path outputJar, char[] password) throws Exception {
        SecureRandom rnd = new SecureRandom();
        byte[] salt = new byte[SALT_LEN];
        rnd.nextBytes(salt);

        SecretKey key = deriveKey(password, salt);

        try (JarInputStream jis = new JarInputStream(Files.newInputStream(inputJar));
             JarOutputStream jos = new JarOutputStream(Files.newOutputStream(outputJar))) {

            // write salt as special entry so loader can derive key
            JarEntry saltEntry = new JarEntry("__meta/salt");
            jos.putNextEntry(saltEntry);
            jos.write(salt);
            jos.closeEntry();

            JarEntry entry;
            byte[] buffer = new byte[8192];
            while ((entry = jis.getNextJarEntry()) != null) {
                String name = entry.getName();
                // skip directory entries and meta entry if any
                if (entry.isDirectory()) {
                    JarEntry d = new JarEntry(name);
                    jos.putNextEntry(d);
                    jos.closeEntry();
                    continue;
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int read;
                while ((read = jis.read(buffer)) != -1) {
                    baos.write(buffer, 0, read);
                }
                byte[] raw = baos.toByteArray();

                JarEntry outEntry = new JarEntry(name);
                jos.putNextEntry(outEntry);

                if (name.endsWith(".class")) {
                    // encrypt class bytes: write [IV][ciphertext+tag]
                    byte[] iv = new byte[GCM_IV_LENGTH];
                    rnd.nextBytes(iv);

                    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                    GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
                    cipher.init(Cipher.ENCRYPT_MODE, key, spec);
                    byte[] cipherText = cipher.doFinal(raw);

                    jos.write(iv); // 12 bytes
                    jos.write(cipherText);
                } else {
                    // copy resources as-is
                    jos.write(raw);
                }
                jos.closeEntry();
            }
        }
    }

    private static SecretKey deriveKey(char[] password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, salt, PBKDF2_ITER, AES_KEY_BITS);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = skf.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }
}
