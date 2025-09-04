package com.minzi.common.core.loadClass;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * 隔离类加载器 - 断开父类委托，实现类隔离
 */
public class IsolatedClassLoader extends URLClassLoader {

    private final Map<String, byte[]> memoryClasses = new HashMap<>();

    private final Map<String, byte[]> resourcesMap = new HashMap<>();


    public IsolatedClassLoader(URL[] urls) {
        super(urls, null); // 断开父类委托
    }

    /**
     * 构造器：直接从内存字节数组加载 JAR
     */
    public IsolatedClassLoader(byte[] jarBytes) throws IOException {
        super(new URL[0], null); // 不使用 URL
        loadJarBytes(jarBytes);
    }

    /**
     * 解析内存 JAR 并缓存 class bytes
     */
    private void loadJarBytes(byte[] jarBytes) throws IOException {
        try (JarInputStream jis = new JarInputStream(new ByteArrayInputStream(jarBytes))) {
            JarEntry entry;
            while ((entry = jis.getNextJarEntry()) != null) {
                if (entry.isDirectory()) continue;

                byte[] entryData = readAllBytes(jis);
                String entryName = entry.getName();

                if (entryName.endsWith(".class")) {
                    // 保存 class 数据
                    String className = entryName.replace('/', '.').replace(".class", "");
                    memoryClasses.put(className, entryData);
                } else {
                    // 保存资源文件
                    resourcesMap.put(entryName, entryData); // 新增 map：entryName -> 内容
                }
            }
        }

    }

    //一次全部读取
    private byte[] readAllBytes(JarInputStream jis) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int read;
        while ((read = jis.read(buffer)) != -1) {
            baos.write(buffer, 0, read);
        }
        return baos.toByteArray();
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // 对于系统核心类，必须委托给系统类加载器，否则会出现安全问题
        if (name.startsWith("java.") || name.startsWith("javax.") ||
                name.startsWith("sun.") || name.startsWith("com.sun.") ||
                name.startsWith("jdk.")) {
            return getSystemClassLoader().loadClass(name);
        }

        // 检查是否已经加载过
        Class<?> clazz = findLoadedClass(name);
        if (clazz == null) {
            // 内存 JAR 优先
            byte[] classBytes = memoryClasses.get(name);
            if (classBytes != null) {
                clazz = defineClass(name, classBytes, 0, classBytes.length);
            } else {
                try {
                    // 尝试 URL 加载
                    clazz = findClass(name);
                } catch (ClassNotFoundException e) {
                    // 最后尝试系统类加载器
                    clazz = getSystemClassLoader().loadClass(name);
                }
            }
        }

        if (resolve) {
            resolveClass(clazz);
        }

        return clazz;
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        // 1. 尝试系统 ClassLoader
        InputStream is = super.getResourceAsStream(name);
        if (is != null) return is;

        // 2. 尝试从解密后的字节流查找资源
        byte[] data = resourcesMap.get(name); // 你解密 JAR 时把非 class 文件放到 resourceMap
        if (data != null) return new ByteArrayInputStream(data);

        return null;
    }
}