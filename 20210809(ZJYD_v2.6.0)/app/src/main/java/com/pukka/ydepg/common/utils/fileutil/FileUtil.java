package com.pukka.ydepg.common.utils.fileutil;

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;

import org.apache.commons.io.LineIterator;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtil {
    private static final String TAG = FileUtil.class.getSimpleName();

    public static boolean isExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static String getContent(InputStream is) {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;

        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            SuperLog.error(TAG, e);
        }
        return sb.toString();
    }

    public static void saveContentToFile(String filePath, String content) {
        SuperLog.info2SDSecurity(TAG,"File save path="+filePath+" Content size="+content.length());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePathFilter(filePath));
            fos.write(content.getBytes());
            SuperLog.info2SDSecurity(TAG, "File stream save finished");
        } catch (Exception e) {
            SuperLog.error(TAG, e);
        } finally {
            closeOutputStream(fos);
            File file = new File(filePathFilter(filePath));
            SuperLog.info2SDSecurity(TAG, "File size = " + file.length());
        }
    }

    /**
     * 安全关闭reader
     *
     * @param reader
     */
    public static void closeReader(Reader reader) {
        if (null == reader) {
            return;
        }
        try {
            reader.close();
        } catch (Exception e1) {
            SuperLog.error(TAG,e1);
        }
    }

    /**
     * 安全关闭流
     *
     * @param outputStream 输出流
     */
    public static void closeOutputStream(OutputStream outputStream) {
        if (null == outputStream) {
            return;
        }
        try {
            outputStream.flush();
        } catch (Exception e1) {
            SuperLog.error(TAG,e1);
        }
        try {
            outputStream.close();
        } catch (Exception e1) {
            SuperLog.error(TAG,e1);
        }
    }

    /**
     * 安全关闭流
     *
     * @param inputStream 输入流
     */
    public static void closeInputStream(InputStream inputStream) {
        if (null == inputStream) {
            return;
        }
        try {
            inputStream.close();
        } catch (Exception e1) {
            SuperLog.error(TAG,e1);
        }
    }


    public static String readFileByLines(String fileName) {
        StringBuffer result = null;
        BufferedReader reader = null;

        String line;
        try {
            File e = new File(filePathFilter(fileName));
            if (e.exists()) {
                reader = new BufferedReader(new FileReader(e));
                line = null;
                result = new StringBuffer();
                LineIterator lineIterator = new LineIterator(reader);

                while (lineIterator.hasNext()) {
                    line = lineIterator.next();
                    result.append(line);
                }

                return result.length() > 0 ? result.toString() : null;
            }

            SuperLog.debug(TAG, e.getName() + " is not exist.");
            return null;
        } catch (IOException var9) {
            SuperLog.error(TAG, var9);
            line = null;
        } finally {
            closeReader(reader);
        }

        return line;
    }

    public static void deleteFile(String fileName) {
        File file = new File(fileName);
        if (isExist(fileName)) {
            boolean result = file.delete();
            if (!result) {
                SuperLog.error(TAG, "Fail to delete file, fileName=" + file.getName());
            }
        }

    }


    /**
     * 文件读取缓冲区大小
     */
    private static final int CACHE_SIZE = 1024;

    /**
     * 解压压缩包
     *
     * @param zipFilePath 压缩文件路径
     * @param destDir     解压目录
     */
    public static boolean unZip(String zipFilePath, String destDir) {
        boolean result = true;
        ZipFile zipFile = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        try {
            zipFile = new ZipFile(zipFilePath);
            SuperLog.info2SDSecurity(TAG, "unzip zipFilePath = " + zipFilePath);
            if (null != zipFile) {
                Enumeration enumeration = zipFile.entries();
                if (null != enumeration) {
                    File file, parentFile;
                    ZipEntry entry;
                    byte[] cache = new byte[CACHE_SIZE];
                    while (enumeration.hasMoreElements()) {
                        entry = (ZipEntry) enumeration.nextElement();
                        if (entry.isDirectory()) {
                            String path = destDir + File.separator + entry.getName();
                            new File(filePathFilter(path)).mkdirs();
                            continue;
                        }
                        try {
                            bis = new BufferedInputStream(zipFile.getInputStream(entry));
                            String path = destDir + File.separator + entry.getName();
                            file = new File(filePathFilter(path));
                            parentFile = file.getParentFile();
                            if (parentFile != null && (!parentFile.exists())) {
                                parentFile.mkdirs();
                            }
                            fos = new FileOutputStream(file.getPath());
                            int readIndex;
                            while ((readIndex = bis.read(cache, 0, CACHE_SIZE)) != -1) {
                                fos.write(cache, 0, readIndex);
                            }
                        } catch (Exception e){
                            SuperLog.error(TAG, e);
                            result = false;
                        } finally {
                            closeOutputStream(fos);
                            closeInputStream(bis);
                        }
                    }
                }
            }
        } catch (Exception e) {
            SuperLog.error(TAG, e);
            result = false;
        } finally {
            try {
                if (null != zipFile) {
                    zipFile.close();
                }
            } catch (IOException e) {
                SuperLog.error(TAG, e);
                result = false;
            }
        }
        return result;
    }

    public static boolean deleteDir(String path) {
        File file = new File(path);
        if (!file.exists()) {//判断是否待删除目录是否存在
            System.err.println("The dir are not exists!");
            return false;
        }

        String[] content = file.list();//取得当前目录下所有文件和文件夹
        if (null == content || content.length == 0) {
            fileDelete(file);
        } else {
            for (String name : content) {
                File temp = new File(path, name);
                if (temp.isDirectory()) {//判断是否是目录
                    deleteDir(FileUtil.getCanonicalPath(temp));//递归调用，删除目录里的内容
                    fileDelete(temp);//删除空目录
                } else {
                    if (!temp.delete()) {//直接删除文件
                        System.err.println("Failed to delete " + name);
                    }
                }
            }
            fileDelete(file);
        }
        return true;
    }

    public static void fileDelete(File file){
        if(!file.delete()){
            SuperLog.error(TAG,file.getName() + " delete failed.");
        }
    }

    public static void fileCreate(File file){
        try{
            if(!file.createNewFile()){
                SuperLog.error(TAG,file.getName() + " create failed.");
            }
        } catch (Exception e){
            SuperLog.error(TAG,e);
        }
    }

    public static String getJsonStrFromUrl(String url) {
        String str = "";
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream       = new FileInputStream(url);
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            bufferedReader    = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            SuperLog.info2SDSecurity(TAG, stringBuilder.toString());
            str = stringBuilder.toString();
        } catch (Exception e) {
            SuperLog.error(TAG, e);
        } finally {
            closeReader(bufferedReader);
            closeReader(inputStreamReader);
            closeInputStream(inputStream);
        }
        return str;
    }

    //匹配 : 其中任意一个字符[ \ : * ? " < > | ]以及[连续两个以上的.]以及[一行最后的.]
    private final static String PathRegex = "[\\\\:*?\"<>|]|[.]{2,}|[/]{2,}|[.]$";

    //路径遍历 漏洞修复
    private static String filePathFilter(String path) {
        //将符合匹配规则的字符删除
        SuperLog.debug(TAG,"InputPath  = "+path);
        String securityPath = path.replaceAll(PathRegex,"");
        SuperLog.debug(TAG,"OutputPath = "+securityPath);
        return securityPath;
    }

    public static String getCanonicalPath(File file){
        String path = "";
        if (null != file) {
            try {
                path = file.getCanonicalPath();
            } catch (IOException e) {
                SuperLog.error(TAG,e);
            }
        }
        return path;
    }

    //使用FileChannel复制文件
    public static void copyFile(File source, File dest){
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel  = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (IOException e) {
            SuperLog.error(TAG, e);
        } finally {
            try{
                if(inputChannel != null){
                    inputChannel.close();
                }
                if (outputChannel != null) {
                    outputChannel.close();
                }
            } catch (Exception e){
                SuperLog.error(TAG, e);
            }
        }
    }
}