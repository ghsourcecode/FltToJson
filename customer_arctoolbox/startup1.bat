rem 改变当前 cmd 窗口编码为 utf-8
chcp 65001
set AGSDESKTOPJAVA=C:\Program Files (x86)\ArcGIS\Desktop10.5\
rem set AGSDEVKITJAVA=C:\Program Files (x86)\ArcGIS\DeveloperKit10.5\   
set JAVA_HOME=C:\Program Files (x86)\Java\jdk1.8.0_144
set Path=%JAVA_HOME%\bin;%AGSDESKTOPJAVA%bin;%AGSDESKTOPJAVA%java\lib

java -jar C:/Users/elitel0329/Desktop/tool/customertoolbox.jar C:/Users/elitel0329/Desktop/tool/resource/customertoolbox/ZCustomer.tbx E:/临时工作/江西演示/原始数据/2016091508-2016091607逐时数据/rain_2016_09_15__17_00_hourly.flt C:/Users/elitel0329/Desktop/tool/resource/result/rain_2016_09_15__17_00_hourly.json
@pause