cd %~dp0\target
java -Xms256M -Xmx256M -Xmn128M -Xss1M -XX:PermSize=64M -XX:MaxPermSize=64M -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=7 -XX:GCTimeRatio=19 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSPermGenSweepingEnabled -XX:+CMSClassUnloadingEnabled -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=2 -XX:-CMSParallelRemarkEnabled -XX:+DisableExplicitGC -XX:CMSInitiatingOccupancyFraction=70 -XX:SoftRefLRUPolicyMSPerMB=0 -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8006  -jar seed-web-1.0.0.jar
@pause
