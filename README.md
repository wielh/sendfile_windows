# sendfile_windows
A program that copy files/folder to another PC periodically

Part 1. How to use it?
     (1). Download folder "client"
     (2). install java jdk17 and mariadb 10.6.4
     (3). execute isntall.bat, and follow the step to complete the install process.
     (4). on mariadb, execute MoveFile.sql and Create_MoverFileUser.sql
     (5). You can double-click setting_sendfile.jar to execute it, or execute it in CMD.
          You can see all parameters you need in CMD by --help
          Sendfile.jar will be executed by windows schtasks, so user do not need to execute it.

Part2. soruce code: other folders than "client" contain source code. I recommand open them by eclipse IDE.

 
 
