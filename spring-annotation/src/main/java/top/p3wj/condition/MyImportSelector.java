package top.p3wj.condition;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.util.MultiValueMap;

import java.util.Set;

/**
 * @author Aaron
 * @description 自定义逻辑返回需要导入的组件
 * @date 2020/5/12 8:37 PM
 */
public class MyImportSelector implements ImportSelector {
    //返回值，就是导入到容器中的组件全类名
    //AnnotationMetadata：当前标注@Import注解的类的所有注解信息
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        //不要返回null
//        return null;
        return new String[]{"top.p3wj.bean.Blue","top.p3wj.bean.Yellow"};
    }
}
