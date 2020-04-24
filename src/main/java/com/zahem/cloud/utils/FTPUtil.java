package com.zahem.cloud.utils;

import com.zahem.cloud.config.AxiosResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.List;

@Slf4j
@Component
public class FTPUtil {
    private static final String ftpIp="192.168.2.227";
    private static final String ftpUser = "wanfei";
    private static final String ftpPasswd="123456";
    private static final int ftpPort=21;
    private static final String basepath="/home/vsftpd";

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











}
