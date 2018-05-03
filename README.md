# FltToJson
web工程后台调用自定义toolbox

工程开发环境：
myeclipse 2014，jdk 1.8 32位，arcgis desktop 10.5，arcobject sdk for java， tomcat 8.5 32位

运行环境配置：
工程运行前，需要在myeclipse中配置工程的 ao 开发环境，并在添加到 myeclipse 中 tomcat 的 paths （myeclipse菜单->windows->preferences->myeclipse->servers->tomcat->paths）中添加如下2个配置：
C:/Program Files (x86)/ArcGIS/Desktop10.5/java/lib;
C:/Program Files (x86)/ArcGIS/Desktop10.5/bin;
此配置通过myeclipse启动tomcat时，有效

如果将工程打包，放到tomcat中运行，需要在 tomcat bin目录新建 setenv.bat文件，并在文件中添加:
set JAVA_OPTS="-Djava.library.path=C:/Program Files (x86)/ArcGIS/Desktop10.5/java/lib;C:/Program Files (x86)/ArcGIS/Desktop10.5/bin;"

该配置在独立运行tomcat时，有效


