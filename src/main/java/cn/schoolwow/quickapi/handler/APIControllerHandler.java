package cn.schoolwow.quickapi.handler;

import cn.schoolwow.quickapi.domain.APIController;
import cn.schoolwow.quickapi.domain.APIDocument;
import cn.schoolwow.quickapi.domain.QuickAPIOption;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 控制器处理器
 * <p>获取项目中所有的控制器类</p>
 * */
public class APIControllerHandler extends AbstractHandler{

    public APIControllerHandler(QuickAPIOption option) {
        super(option);
    }

    @Override
    public Handler handle(APIDocument apiDocument) throws Exception{
        List<APIController> apiControllerList = new ArrayList<>();
        List<String> classNameList = getAllClassNameList();
        for(String className:classNameList){
            Class clazz = ClassLoader.getSystemClassLoader().loadClass(className);
            if(null==clazz.getAnnotation(Controller.class)
                    &&null==clazz.getAnnotation(RestController.class)
            ){
                continue;
            }
            APIController apiController = new APIController();
            apiController.className = className;
            apiController.clazz = clazz;
            if(null!=apiController.clazz.getAnnotation(Deprecated.class)){
                apiController.deprecated = true;
            }
            apiControllerList.add(apiController);
        }
        apiDocument.apiControllerList = apiControllerList;
        return new APIHandler(option);
    }

    /**
     * 获取项目中所有的类
     * */
    private List<String> getAllClassNameList() throws Exception{
        List<String> classNameList = new ArrayList<>(1024);
        Enumeration<URL> urlEnumeration = ClassLoader.getSystemClassLoader().getResources("");
        while(urlEnumeration.hasMoreElements()){
            URL url = urlEnumeration.nextElement();
            if(url==null){
                continue;
            }
            switch (url.getProtocol()) {
                case "file": {
                    File directoryFile = new File(url.getFile());
                    //TODO 对于有空格或者中文路径会无法识别
                    if (!directoryFile.isDirectory()) {
                        throw new IllegalArgumentException("包名不是合法的文件夹!" + url.getFile());
                    }
                    Files.walkFileTree(directoryFile.toPath(),new SimpleFileVisitor<Path>(){
                        @Override
                        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                            File file = path.toFile();
                            if(file.getName().endsWith(".class")){
                                String className = file.getAbsolutePath().substring(0,file.getAbsolutePath().length()-6).substring(directoryFile.getAbsolutePath().length()+1).replace('/','.').replace('\\','.');
                                classNameList.add(className);
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    });
                }
                break;
                case "jar": {
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    if (null != jarURLConnection) {
                        JarFile jarFile = jarURLConnection.getJarFile();
                        if (null != jarFile) {
                            Enumeration<JarEntry> jarEntries = jarFile.entries();
                            while (jarEntries.hasMoreElements()) {
                                JarEntry jarEntry = jarEntries.nextElement();
                                String jarEntryName = jarEntry.getName();
                                if(jarEntryName.endsWith(".class")){
                                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                    classNameList.add(className);
                                }
                            }
                        }
                    }
                }break;
                default:{
                    throw new UnsupportedOperationException("不支持的url协议!当前url协议:"+url.getProtocol());
                }
            }
        }
        return classNameList;
    }
}
