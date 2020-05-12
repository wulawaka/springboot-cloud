# 基于SpringBoot2的 网盘功能

> 个人博客：zahemblogs.club(国内访问有点慢)
> 备用博客：wulawaka.github.io

## 1、技术栈

主要采用了：SpringBoot、MyBatis、MySQL、Redis、AliPay

## 2、快速运行

1、克隆本项目到本地撒的

> git clone  git@github.com:wulawaka/springboot-cloud.git

2、根据实际情况，修改application-dev.yml中的内容。

3、运行项目，访问**localhost:8081**

## 3、全部接口说明

> 用户模块

| 接口      | 说明 | 参数                |
| --------- | ---- | ------------------- |
| /register | 注册 | Name,Email,Password |
| /login    | 登陆 | Email,password      |

> 文件模块

| 接口        | 说明             | 参数                                                         |
| ----------- | ---------------- | ------------------------------------------------------------ |
| /all        | 查看所有文件     | token，parentId(不填默认为0）                                |
| /addfile    | 新建文件夹       | token,Name(默认为新建文件夹）,type(默认0)，parentIdd(默认为0) |
| /delete     | 清空垃圾箱       | token，id                                                    |
| /rubbish    | 移到垃圾箱       | token,id                                                     |
| /upload     | 上传             | file,token                                                   |
| /download   | 下载             | token,id                                                     |
| /rubbishall | 查询垃圾箱       | token                                                        |
| /type       | 根据类型查询文件 | token,type                                                   |
|             |                  |                                                              |

## 4、接口详细说明

> 详情跳转Wiki