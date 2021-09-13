package cn.schoolwow.quickapi.util;

import cn.schoolwow.quickapi.domain.APIController;
import cn.schoolwow.quickapi.domain.APIDocument;
import cn.schoolwow.quickapi.domain.APIEntity;
import cn.schoolwow.quickapi.domain.QuickAPIOption;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * JavaDoc工具类
 * */
public class JavaDocUtil {
    private static Logger logger = LoggerFactory.getLogger(JavaDocUtil.class);
    private static ClassDoc[] classDocs = new ClassDoc[0];

    public static class Doclet {
        public Doclet() {
        }
        public static boolean start(RootDoc root) {
            classDocs = root.classes();
            return true;
        }
    }

    /**
     * 获取JavaDoc注释
     * */
    public static ClassDoc[] getJavaDoc(APIDocument apiDocument, QuickAPIOption quickAPIOption){
        List<String> packageNameList = new ArrayList<>();
        for(APIController apiController:apiDocument.apiControllerList){
            String packageName = apiController.clazz.getPackage().getName();
            if(packageNameList.stream().noneMatch(packageName1->packageName1.startsWith(packageName))){
                packageNameList.add(packageName);
            }
        }
        for(APIEntity apiEntity:apiDocument.apiEntityMap.values()){
            String packageName = apiEntity.clazz.getPackage().getName();
            if(packageNameList.stream().noneMatch(packageName1->packageName1.startsWith(packageName))){
                packageNameList.add(packageName);
            }
        }
        if(packageNameList.isEmpty()){
            throw new IllegalArgumentException("控制器类包名列表为空!");
        }

        StringBuilder classPathBuilder = new StringBuilder();
        URLClassLoader urlClassLoader = new URLClassLoader(((URLClassLoader) ClassLoader.getSystemClassLoader()).getURLs());
        URL[] urls = urlClassLoader.getURLs();
        for(URL url:urls){
            classPathBuilder.append(url.getPath().substring(1)+";");
        }
        classPathBuilder.append(ClassLoader.getSystemClassLoader().getResource("").getPath().substring(1)+";");

        List<String> commandList = new ArrayList<>(Arrays.asList(
                "-doclet",
                Doclet.class.getName(),
                "-encoding",
                "utf-8",
                "-private",
                "-quiet",
                "-classpath",
                classPathBuilder.toString(),
                "-sourcepath",
                quickAPIOption.sourcePath
        ));
        Iterator<String> iterator = packageNameList.iterator();
        while(iterator.hasNext()){
            commandList.add("-subpackages");
            commandList.add(iterator.next());
        }

        logger.info("[读取JavaDoc注释]{}",commandList);
        com.sun.tools.javadoc.Main.execute(commandList.toArray(new String[0]));
        logger.info("[读取类注释个数]{}",classDocs.length);
        return classDocs;
    }
}