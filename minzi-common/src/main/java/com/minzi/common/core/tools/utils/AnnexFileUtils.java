package com.minzi.common.core.tools.utils;

import com.alibaba.fastjson.JSON;
import com.minzi.common.core.model.AnnexFile;
import com.minzi.common.core.model.enums.AnnexFileEnum;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AnnexFileUtils {

    /**
     * 将 VO 中的 List<AnnexFile> 转换并写入到 Entity 的 annexFile 字段
     *
     * @param vo     包含 List<AnnexFile> 属性的 VO
     * @param entity 目标实体类，必须有 setAnnexFile(String) 方法
     */
    public static <V, E> void fillAnnexFiles(V vo, E entity) {
        try {
            List<AnnexFile> annexFiles = null;

            // 1. 通过反射找到 VO 中第一个 List<AnnexFile> 类型的字段
            for (Field field : vo.getClass().getDeclaredFields()) {
                if (List.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    Object value = field.get(vo);

                    if (value instanceof List) {
                        List<?> list = (List<?>) value;
                        if (!list.isEmpty() && list.get(0) instanceof AnnexFile) {
                            @SuppressWarnings("unchecked")
                            List<AnnexFile> castList = (List<AnnexFile>) list;
                            annexFiles = castList;
                            break;
                        }
                    }
                }
            }

            if (annexFiles == null) {
                return; // VO 里没有符合条件的 List<AnnexFile>
            }

            // 2. 枚举映射（文件后缀 -> 类型 code）
            Map<String, Integer> annexFileEnumMap =
                    AnnexFileEnum.FileType.toMap(AnnexFileEnum.FileType::getName, AnnexFileEnum.FileType::getCode);

            // 3. 遍历转换
            Optional.ofNullable(annexFiles).ifPresent(files -> {
                files.forEach(item -> {
                    String fileSuffix = item.getFileSuffix();
                    Integer type = annexFileEnumMap.get(fileSuffix);
                    item.setFileType(type);
                });

                // 4. 找到 entity 的 setAnnexFile 方法并赋值
                try {
                    Method setAnnexFile = entity.getClass().getMethod("setAnnexFile", String.class);
                    setAnnexFile.invoke(entity, JSON.toJSONString(files));
                } catch (Exception e) {
                    throw new RuntimeException("Entity 缺少 setAnnexFile(String) 方法", e);
                }
            });

        } catch (Exception e) {
            throw new RuntimeException("fillAnnexFiles 执行失败", e);
        }
    }
}
