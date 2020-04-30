package com.zahem.cloud.utils;

import com.zahem.cloud.config.AxiosResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.List;

/**
 * FTP工具类
 * upload   上传
 * downloadFile 下载
 * downFile 调用上面
 * @author zahem
 */
@Slf4j
@Component
public class FTPUtil {
    @Value("${ftp.ip}")
    private String ftpIp;
    @Value("${ftp.username}")
    private String ftpUser;
    @Value("${ftp.password}")
    private String ftpPasswd;
    @Value("${ftp.port}")
    private int ftpPort;
    @Value("${ftp.filepath}")
    private String basepath;

    public boolean upload(String fileName, InputStream input){
        boolean success = false;
        FTPClient ftpClient=new FTPClient();
        try{
            int reply;
            ftpClient.connect(ftpIp,ftpPort);
            ftpClient.login(ftpUser,ftpPasswd);
            reply = ftpClient.getReplyCode();
            if(!FTPReply.isPositiveCompletion(reply)){
                ftpClient.disconnect();
                return false;
            }
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.makeDirectory(basepath);
            ftpClient.changeWorkingDirectory(basepath);
            ftpClient.storeFile(fileName, input);
            input.close();
            ftpClient.logout();
            success = true;
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(ftpClient.isConnected()){
                try{
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    private InputStream downloadFile( String fileName) {
        InputStream result = null;
            FTPClient ftpClient = new FTPClient();
            try {
                int reply;
                ftpClient.connect(ftpIp,ftpPort);
                ftpClient.login(ftpUser,ftpPasswd);
                ftpClient.enterLocalPassiveMode();
                reply = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftpClient.disconnect();
                    return null;
                }
                // 转移到FTP服务器目录
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.changeWorkingDirectory(basepath);
                FTPFile[] fs = ftpClient.listFiles();
                // 下载文件是否存在
                boolean flag = false;
                for (FTPFile ff : fs) {
                    byte[] bytes = ff.getName().getBytes("iso-8859-1");
                    String name = new String(bytes, "GBK");
                    if (name.equals(fileName)) {
                        result = ftpClient.retrieveFileStream(ff.getName());
                        flag = true;
                    }
                }
                if (!flag) {
                    log.info("文件: " + fileName + "不存在 ！");
                } else {
                    log.info("下载成功 ！");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (ftpClient.isConnected()) {
                    try {
                        ftpClient.disconnect();
                    } catch (IOException ioe) {
                    }
                }
            }
        return result;
    }

    public InputStream downFile(String fileName) {
        InputStream result = downloadFile(fileName);
        return result;
    }












}
