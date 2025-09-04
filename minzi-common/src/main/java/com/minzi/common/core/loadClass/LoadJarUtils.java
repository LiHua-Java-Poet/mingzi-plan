package com.minzi.common.core.loadClass;

import com.minzi.common.exception.BaseRuntimeException;
import org.springframework.data.repository.init.ResourceReader;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态加载jar工具类-支持多个jar和类隔离
 *
 * @author MinKe
 */
public class LoadJarUtils {

    // 使用Map管理多个jar的类加载器
    private static final Map<String, IsolatedClassLoader> jarLoaders = new ConcurrentHashMap<>();

    // 默认的类加载器（用于向后兼容）
    private static IsolatedClassLoader defaultClassLoader;

    private static String password;

    /**
     * 动态加载本地jar，初始化类加载器
     */
    public static void loadJar(String jarPath) throws Exception {
        doInitPassword();
        // 先判断是否存在对应的加密包
        String jarPathToLoad = resolveJarPath(jarPath);

        File jarFile = new File(jarPathToLoad);
        if (!jarFile.exists()) {
            throw new BaseRuntimeException("Jar包不存在：" + jarPathToLoad);
        }

        String jarKey = jarFile.getAbsolutePath();

        // 如果已经加载过，先卸载旧的
        if (jarLoaders.containsKey(jarKey)) {
            unloadJar(jarKey);
        }

        // 创建类加载器
        IsolatedClassLoader classLoader;
        if (jarPathToLoad.endsWith(".enc")) {
            // 加密包走解密加载逻辑
            byte[] jarBytes = JarDecryptor.decryptJarToBytes(jarPathToLoad, password.toCharArray());
            classLoader = new IsolatedClassLoader(jarBytes);
        } else {
            // 普通 JAR
            classLoader = new IsolatedClassLoader(new URL[]{jarFile.toURI().toURL()});
        }

        jarLoaders.put(jarKey, classLoader);
        defaultClassLoader = classLoader;
    }

    /**
     * 检查是否存在对应的加密包，如果存在则返回加密包路径，否则返回原始路径
     */
    private static String resolveJarPath(String jarPath) {
        if (jarPath.endsWith(".jar")) {
            String encPath = jarPath.substring(0, jarPath.length() - 4) + ".enc";
            if (new File(encPath).exists()) {
                return encPath;
            }
        }
        return jarPath;
    }

    /**
     * 初始化一次密文
     */
    public static void doInitPassword() {
        if (!StringUtils.isEmpty(password)) return;
        // 获取类加载器
        ClassLoader classLoader = ResourceReader.class.getClassLoader();

        // 从 resources 读取文件
        try (InputStream inputStream = classLoader.getResourceAsStream("AccessKey.txt")) {
            if (inputStream == null) {
                throw new RuntimeException("文件不存在");
            }

            // 逐行读取
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            password = sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 从指定jar加载类
     *
     * @param jarKey    jar标识符
     * @param className 类名
     * @return Class对象
     */
    public static Class<?> loadClass(String jarKey, String className) throws Exception {
        IsolatedClassLoader classLoader = jarLoaders.get(jarKey);
        if (classLoader == null) {
            throw new BaseRuntimeException("未找到jar加载器：" + jarKey);
        }
        return classLoader.loadClass(className);
    }

    /**
     * 使用默认类加载器加载类（向后兼容）
     */
    public static Class<?> loadClass(String className) throws Exception {
        if (defaultClassLoader == null) {
            throw new BaseRuntimeException("未初始化任何jar加载器");
        }
        return defaultClassLoader.loadClass(className);
    }

    /**
     * 卸载指定jar
     */
    public static void unloadJar(String jarKey) throws IOException {
        IsolatedClassLoader classLoader = jarLoaders.remove(jarKey);
        if (classLoader != null) {
            classLoader.close();

            // 如果卸载的是默认加载器，需要重置
            if (classLoader == defaultClassLoader) {
                defaultClassLoader = null;
            }
        }
    }

    /**
     * 卸载当前jar（向后兼容）
     */
    public static void unloadJar() throws IOException {
        if (defaultClassLoader != null) {
            // 找到默认加载器对应的key并卸载
            for (Map.Entry<String, IsolatedClassLoader> entry : jarLoaders.entrySet()) {
                if (entry.getValue() == defaultClassLoader) {
                    unloadJar(entry.getKey());
                    break;
                }
            }
        }
    }

    /**
     * 获取指定jar的类加载器
     */
    public static IsolatedClassLoader getClassLoader(String jarKey) {
        IsolatedClassLoader classLoader = jarLoaders.get(jarKey);
        if (classLoader == null) {
            throw new BaseRuntimeException("未找到jar加载器：" + jarKey);
        }
        return classLoader;
    }

    /**
     * 获取当前的类加载器（向后兼容）
     */
    public static URLClassLoader getUrlClassLoader() {
        if (defaultClassLoader == null) {
            throw new BaseRuntimeException("动态加载jar可能出现了异常，导致无法获取UrlClassLoader");
        }
        return defaultClassLoader;
    }

    /**
     * 获取已加载的jar列表
     */
    public static String[] getLoadedJars() {
        return jarLoaders.keySet().toArray(new String[0]);
    }

    /**
     * 检查jar是否已加载
     */
    public static boolean isJarLoaded(String jarPath) {
        try {
            String jarKey = new File(jarPath).getAbsolutePath();
            return jarLoaders.containsKey(jarKey);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 创建类的实例
     */
    public static Object createInstance(String jarKey, String className) throws Exception {
        Class<?> clazz = loadClass(jarKey, className);
        return clazz.getDeclaredConstructor().newInstance();
    }

    /**
     * 使用默认加载器创建实例（向后兼容）
     */
    public static Object createInstance(String className) throws Exception {
        Class<?> clazz = loadClass(className);
        return clazz.getDeclaredConstructor().newInstance();
    }
}