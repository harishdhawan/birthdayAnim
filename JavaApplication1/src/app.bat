set packagename=javaapplication1


javac %packagename%\*.java
"C:\Program Files\Java\jdk1.8.0_05\bin\jar.exe" cvf main.jar %packagename%\*.class %packagename%\*.png %packagename%\*.jpg %packagename%\*.gif %packagename%\*.mp3
copy main.jar %packagename%\main.jar
"C:\Program Files\Java\jdk1.8.0_05\bin\jarsigner.exe" -keystore myCert -keypass myKeyPass -storepass myStorePass main.jar myAlias

call play.bat

"C:\Program Files\Java\jdk1.8.0_05\bin\appletviewer.exe" %packagename%\main.html

