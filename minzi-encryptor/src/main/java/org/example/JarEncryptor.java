package org.example;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.security.CodeSource;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
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
        char[] password;
        Path targetPath;

        if (args.length == 1) {
            // 传入了密钥
            password = args[0].toCharArray();
            targetPath = getCurrentJarPath().getParent();
            System.out.println("使用命令行参数作为密钥");
        } else {
            // 从配置文件读取
            System.out.println("未提供命令行参数，读取配置文件...");
            Path currentDir = getCurrentJarPath().getParent();
            Path configPath = currentDir.resolve("AccessKey.txt");
            Map<String, String> config = loadConfig(configPath);

            String key = config.get("accessKey");
            String path = config.get("path");
            if (key == null || path == null) {
                throw new RuntimeException("配置文件缺少 accessKey 或 path");
            }
            password = key.toCharArray();
            targetPath = Paths.get(path);
            System.out.println("使用配置文件密钥: " + key);
            System.out.println("加密目录: " + path);
        }

        // 遍历并加密 jar
        Files.list(targetPath)
                .filter(p -> p.toString().endsWith(".jar"))
                .forEach(path -> {
                    try {
                        Path output = Paths.get(path.toString().replace(".jar", ".enc"));
                        System.out.println("加密: " + path + " -> " + output);
                        encryptJar(path, output, password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        System.out.println("加密完成!");
    }

    // 读取当前 JAR 路径
    private static Path getCurrentJarPath() throws URISyntaxException {
        CodeSource codeSource = JarEncryptor.class.getProtectionDomain().getCodeSource();
        if (codeSource != null) {
            return Paths.get(codeSource.getLocation().toURI());
        } else {
            throw new RuntimeException("无法获取当前 JAR 路径");
        }
    }

    // 加密 Jar 文件
    public static void encryptJar(Path inputJar, Path outputJar, char[] password) throws Exception {
        SecureRandom rnd = new SecureRandom();
        byte[] salt = new byte[SALT_LEN];
        rnd.nextBytes(salt);

        SecretKey key = deriveKey(password, salt);

        try (JarInputStream jis = new JarInputStream(Files.newInputStream(inputJar));
             JarOutputStream jos = new JarOutputStream(Files.newOutputStream(outputJar))) {

            // 写入 salt 元信息
            JarEntry saltEntry = new JarEntry("__meta/salt");
            jos.putNextEntry(saltEntry);
            jos.write(salt);
            jos.closeEntry();

            JarEntry entry;
            byte[] buffer = new byte[8192];
            while ((entry = jis.getNextJarEntry()) != null) {
                String name = entry.getName();

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
                    byte[] iv = new byte[GCM_IV_LENGTH];
                    rnd.nextBytes(iv);

                    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                    GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
                    cipher.init(Cipher.ENCRYPT_MODE, key, spec);
                    byte[] cipherText = cipher.doFinal(raw);

                    jos.write(iv);
                    jos.write(cipherText);
                } else {
                    jos.write(raw);
                }
                jos.closeEntry();
            }
        }
    }

    // 从密码派生 AES 密钥
    private static SecretKey deriveKey(char[] password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, salt, PBKDF2_ITER, AES_KEY_BITS);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = skf.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }

    // 加载配置文件
    private static Map<String, String> loadConfig(Path configPath) throws IOException {
        Map<String, String> config = new HashMap<>();
        try (BufferedReader br = Files.newBufferedReader(configPath)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    config.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
        return config;
    }
}
