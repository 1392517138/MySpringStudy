package top.p3wj.conifg;

import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * @author Aaron
 * @description
 * @date 2020/5/12 3:47 PM
 */
public class MyTypeFilter implements TypeFilter {
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        /**
         * @param metadataReader the metadata reader for the target class 读取到到当前正在扫描到类信息
         * @param metadataReaderFactory a factory for obtaining metadata readers 获取到其他类到任何信息
         */
        //获取当前类注解信息
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        //获取当前正在扫描到类的类信息
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        //获取当前类资源（类路径）
        metadataReader.getResource();

        String className = classMetadata.getClassName();
        System.out.println("--->"+className);
        //true匹配成功，false匹配失败
        if (className.contains("er")){
            return true;
        }
        return false;
    }
}
