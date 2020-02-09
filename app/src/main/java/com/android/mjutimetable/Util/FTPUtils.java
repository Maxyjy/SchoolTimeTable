package com.android.mjutimetable.Util;


import java.io.File;

import java.io.FileInputStream;

import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;

import java.net.SocketException;



import org.apache.commons.net.ftp.FTP;

import org.apache.commons.net.ftp.FTPClient;

import org.apache.commons.net.ftp.FTPFile;

import org.apache.commons.net.ftp.FTPReply;



import android.util.Log;



/**

 * 2013年09月20日21:38:04

 *

 * 用于Android和FTP服务器进行交互的工具类

 *

 * @author xiaoyaomeng

 *

 */

public class FTPUtils {

    private FTPClient ftpClient = null;

    private static FTPUtils ftpUtilsInstance = null;

    private String FTPUrl;

    private int FTPPort;

    private String UserName;

    private String UserPassword;



    private FTPUtils() {

        ftpClient = new FTPClient();

    }



    /*

     * 得到类对象实例（因为只能有一个这样的类对象，所以用单例模式）

     */

    public static FTPUtils getInstance() {

        if (ftpUtilsInstance == null) {

            ftpUtilsInstance = new FTPUtils();

        }

        return ftpUtilsInstance;

    }




    /**

     * 设置FTP服务器

     *

     * @param FTPUrl

     *            FTP服务器ip地址

     * @param FTPPort

     *            FTP服务器端口号

     * @param UserName

     *            登陆FTP服务器的账号

     * @param UserPassword

     *            登陆FTP服务器的密码

     * @return

     */

    public boolean initFTPSetting(String FTPUrl, int FTPPort, String UserName,

                                  String UserPassword) {

        this.FTPUrl = FTPUrl;

        this.FTPPort = FTPPort;

        this.UserName = UserName;

        this.UserPassword = UserPassword;



        int reply;



        try {

            // 1.要连接的FTP服务器Url,Port

            ftpClient.connect(FTPUrl, FTPPort);



            // 2.登陆FTP服务器

            ftpClient.login(UserName, UserPassword);



            // 3.看返回的值是不是230，如果是，表示登陆成功

            reply = ftpClient.getReplyCode();



            if (!FTPReply.isPositiveCompletion(reply)) {

                // 断开

                ftpClient.disconnect();

                return false;

            }

            Log.e("FTPUtils", "登陆成功");



            return true;



        } catch (SocketException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

            return false;

        } catch (IOException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

            return false;

        }

    }



    /**

     * 上传文件

     *

     * @param FilePath

     *            要上传文件所在SDCard的路径

     * @param FileName

     *            要上传的文件的文件名(如：Sim唯一标识码)

     * @return true为成功，false为失败

     */

    public boolean uploadUserFile(String id,String FilePath, String FileName) {



        if (!ftpClient.isConnected()) {

            if (!initFTPSetting(FTPUrl, FTPPort, UserName, UserPassword)) {

                return false;

            }

        }



        try {



            // 设置存储路径



            ftpClient.changeWorkingDirectory("/ftpaccount/web/timetableuser/");



            // 设置上传文件需要的一些基本信息

            ftpClient.setBufferSize(1024);

            ftpClient.setControlEncoding("UTF-8");

            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);



            // 文件上传吧～

            FileInputStream fileInputStream = new FileInputStream(FilePath);

            ftpClient.storeFile(id+FileName, fileInputStream);



            // 关闭文件流

            fileInputStream.close();

            Log.e("FTPUtils", "上传成功");



            // 退出登陆FTP，关闭ftpCLient的连接

            ftpClient.logout();

            ftpClient.disconnect();



        } catch (IOException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

            return false;

        }

        return true;

    }



    /**

     * 下载文件

     *

     * @param FilePath

     *            要存放的文件的路径

     * @param FileName

     *            远程FTP服务器上的那个文件的名字

     * @return true为成功，false为失败

     */

    public boolean downLoadFile(String FilePath, String FileName,String Phonenum) {



        if (!ftpClient.isConnected()) {

            if (!initFTPSetting(FTPUrl, FTPPort, UserName, UserPassword)) {

                return false;

            }

        }



        try {

            // 转到指定下载目录

            ftpClient.changeWorkingDirectory("/ftpaccount/web/user/"+Phonenum);



            // 列出该目录下所有文件

            FTPFile[] files = ftpClient.listFiles();


            // 遍历所有文件，找到指定的文件
            System.out.print(FileName);
            for (FTPFile file : files) {
                     System.out.print(file.getName());
                if (file.getName().equals(FileName)) {

                    // 根据绝对路径初始化文件

                    File localFile = new File(FilePath);



                    // 输出流

                    OutputStream outputStream = new FileOutputStream(localFile);



                    // 下载文件

                    ftpClient.retrieveFile(file.getName(), outputStream);



                    // 关闭流

                    outputStream.close();

                    Log.e("FTPUtils", "下载成功");

                    ftpClient.logout();

                    ftpClient.disconnect();

                    return true;

                }

            }



            // 退出登陆FTP，关闭ftpCLient的连接





        } catch (IOException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }



        return false;

    }
    /**

     * 下载文件

     *

     * @param FilePath

     *            要存放的文件的路径

     * @param FileName

     *            远程FTP服务器上的那个文件的名字

     * @return true为成功，false为失败

     */

    public boolean downLoadDatabaseFile(String FilePath, String FileName) {



        if (!ftpClient.isConnected()) {

            if (!initFTPSetting(FTPUrl, FTPPort, UserName, UserPassword)) {

                return false;

            }

        }



        try {

            // 转到指定下载目录

            ftpClient.changeWorkingDirectory("/ftpaccount/web/housedatabase");



            // 列出该目录下所有文件



            // 遍历所有文件，找到指定的文件



                    // 根据绝对路径初始化文件

                    File localFile = new File(FilePath);


                    // 输出流

                    OutputStream outputStream = new FileOutputStream(localFile);


                    // 下载文件
                    String filename = "house.db";

                    ftpClient.enterLocalPassiveMode();
                    ftpClient.retrieveFile(filename, outputStream);


                    // 关闭流

                    outputStream.close();





            // 退出登陆FTP，关闭ftpCLient的连接

            ftpClient.logout();

            ftpClient.disconnect();



        } catch (IOException e) {
            Log.e("FTPUtils", "失败");
            // TODO Auto-generated catch block

            e.printStackTrace();

        }



        return true;

    }
public boolean isFileExist(String filename){
    ftpClient.enterLocalActiveMode();
    // 设置文件类型为二进制，与ASCII有区别

    try {
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    } catch (IOException e) {
        e.printStackTrace();
    }

    // 设置编码格式
    ftpClient.setControlEncoding("GBK");

    // 提取绝对地址的目录以及文件名

    // 进入文件所在目录，注意编码格式，以能够正确识别中文目录

    try {
        ftpClient.changeWorkingDirectory("/ftpaccount/web/user");
    } catch (IOException e) {
        e.printStackTrace();
    }


    // 检验文件是否存在
    InputStream is = null;

    try {
        is = ftpClient.retrieveFileStream("/ftpaccount/web/user/"+filename+".db");
    } catch (IOException e) {
        e.printStackTrace();
    }

    if(is == null || ftpClient.getReplyCode() == FTPReply.FILE_UNAVAILABLE){

        return false;
    }

    return true;
}

   public boolean makeDir(String path){
       Boolean makeDirsucess=false;
        try {

        makeDirsucess= ftpClient.makeDirectory(path);

        } catch (IOException e) {
           e.printStackTrace();
       }
        return makeDirsucess;
   }


    /**

     * 上传文件

     *

     * @param FilePaths

     *            要上传文件所在SDCard的路径

     * @param FileNames

     *            要上传的文件的文件名(如：Sim唯一标识码)

     * @return true为成功，false为失败

     */

    public boolean uploadPublishFile(String[] FilePaths, String[] FileNames,String Phonenum,String StorePath) {



        if (!ftpClient.isConnected()) {

            if (!initFTPSetting(FTPUrl, FTPPort, UserName, UserPassword)) {

                return false;

            }

        }



        try {



            // 设置存储路径



            ftpClient.changeWorkingDirectory(StorePath);



            // 设置上传文件需要的一些基本信息

            ftpClient.setBufferSize(1024);

            ftpClient.setControlEncoding("UTF-8");

            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // 文件上传吧～
            for (int i=0;i<FileNames.length;i++) {
                if(FilePaths[i]!=null){
                FileInputStream fileInputStream = new FileInputStream(FilePaths[i]);

                ftpClient.storeFile(i+".jpg", fileInputStream);

                fileInputStream.close();}
            }


            FileInputStream publishhousedata = new FileInputStream("/data/data/com.android.xiaoyuanzu/databases/publishhouse.db");

            ftpClient.storeFile("publishhouse.db", publishhousedata);

            publishhousedata.close();



            ftpClient.changeWorkingDirectory("/ftpaccount/web/user/"+Phonenum);

            FileInputStream fileInputStream = new FileInputStream("/data/data/com.android.xiaoyuanzu/databases/" + Phonenum + ".db");

            ftpClient.storeFile(Phonenum+".db", fileInputStream);

            fileInputStream.close();

            // 关闭文件流

            File sucessfile=new File("/data/data/com.android.xiaoyuanzu/databases/sucess.txt");
            sucessfile.createNewFile();

            ftpClient.changeWorkingDirectory(StorePath);
            FileInputStream sucesscodefile = new FileInputStream("/data/data/com.android.xiaoyuanzu/databases/sucess.txt");
            ftpClient.storeFile("sucess.txt", sucesscodefile);
            sucesscodefile.close();

            // 关闭文件流
            File file=new File("/data/data/com.android.xiaoyuanzu/databases/publishhouse.db");
            file.delete();

            Log.e("FTPUtils", "上传成功");



            // 退出登陆FTP，关闭ftpCLient的连接

            ftpClient.logout();

            ftpClient.disconnect();



        } catch (IOException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

            return false;

        }

        return true;

    }

    /**

     * 下载文件

     *

     * @param Phonenum

     *            远程FTP服务器上的那个文件的名字

     * @return true为成功，false为失败

     */

    public boolean downLoadMyPublishDatabaseFile(String Phonenum,int Publish) {
//
//
//
//        if (!ftpClient.isConnected()) {
//
//            if (!initFTPSetting(FTPUrl, FTPPort, UserName, UserPassword)) {
//
//                return false;
//
//            }
//
//        }
//
//
//
//        try {
//            Log.e("FTPUtils", "APublishfor循环进入");
//            // 转到指定下载目录
//            for(int i=0;i<Publish;i++){
//                Log.e("FTPUtils", "BPublishfor循环进入");
//
//                ftpClient.changeWorkingDirectory("/ftpaccount/web/user/"+Phonenum+"/publish"+i);
//
// ///                                                       /ftpaccount/web/user/15659190597/publish0
//
//                File localFile = new File("/data/data/com.android.xiaoyuanzu/databases/publish"+i+".db");
//
//
//                // 输出流
//
//                OutputStream outputStream = new FileOutputStream(localFile);
//
//
//                // 下载文件
//                String filename = "publish"+i+".db";
//
//                ftpClient.enterLocalPassiveMode();
//                ftpClient.retrieveFile(filename, outputStream);
//
//
//                // 关闭流
//
//                outputStream.close();
//
//            }
//
//
//            // 退出登陆FTP，关闭ftpCLient的连接
//
//            ftpClient.logout();
//
//            ftpClient.disconnect();
//
//
//
//        } catch (IOException e) {
//            Log.e("FTPUtils", "失败");
//            // TODO Auto-generated catch block
//
//            e.printStackTrace();
//
//        }
//
//
//
//        return true;





        if (!ftpClient.isConnected()) {

            if (!initFTPSetting(FTPUrl, FTPPort, UserName, UserPassword)) {

                return false;

            }

        }



        try {

            // 转到指定下载目录

            for(int i=0;i<Publish;i++){
                Log.e("i=",""+i);
                ftpClient.changeWorkingDirectory("/ftpaccount/web/user/"+Phonenum+"/publish"+i);
                // 列出该目录下所有文件
                FTPFile[] files = ftpClient.listFiles();
                // 遍历所有文件，找到指定的文件
                for (FTPFile file : files) {
                    if (file.getName().equals("publishhouse.db")) {
                        // 根据绝对路径初始化文件
                        File localFile = new File("/data/data/com.android.xiaoyuanzu/databases/publish"+i+".db");
                        // 输出流
                        OutputStream outputStream = new FileOutputStream(localFile);
                        // 下载文件
                        ftpClient.retrieveFile(file.getName(), outputStream);
                        // 关闭流
                        outputStream.close();
                        Log.e("FTPUtils", file.getName()+"下载成功");
                    }

                }

            }


            Log.e("FTPUtils", "全部下载成功");
            ftpClient.logout();
            ftpClient.disconnect();
            return true;


            // 退出登陆FTP，关闭ftpCLient的连接





        } catch (IOException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }



        return false;





    }
}