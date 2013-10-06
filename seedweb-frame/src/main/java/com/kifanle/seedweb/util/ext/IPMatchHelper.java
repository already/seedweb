package com.kifanle.seedweb.util.ext;

import org.apache.commons.lang.StringUtils;

/**
 * ip匹配辅助类(ipv4),可验证输入规则中包含ip、ip通配符（134.*.*.*）、ip段(192.0.0.0-192.0.0.50)
 * @author zhouqin
 * Date: 13-8-21
 */
public class IPMatchHelper {
    public final static String REGX_0_255="(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
    //匹配*.*.*.*   (0-255).*.*.*  (0-255).(0-255).*.*  (0-255).(0-255).(0-255).*
    public final static String REGX_IP_WILDCARD = "(((\\*\\.){3}\\*)|("+REGX_0_255+"(\\.\\*){3})|("+REGX_0_255 +"\\."+REGX_0_255+")(\\.\\*){2}"+"|(("+REGX_0_255+"\\.){3}\\*))";
    //匹配 ip
    public final static String REGX_IP = "(("+REGX_0_255+"\\.){3}"+REGX_0_255+")";
    //匹配网段
    public final static String REGX_IP_SEG = "("+REGX_IP + "\\-" + REGX_IP+")";

    public final static String REGX_IP_UNIT = "("+REGX_IP+"|"+REGX_IP_WILDCARD+"|"+REGX_IP_SEG+")";
    /**
     * 输入ip规则，"ip|ipsegs;ip|ipsegs"
     */
    public final static String REGX_INPUT =REGX_IP_UNIT+"|("+REGX_IP_UNIT+"(\\;"+REGX_IP_UNIT+")*)";
    
    /**
     * 是否为ip
     * @param ip
     * @return boolean
     */
    public static boolean isIP(String ip){
        return StringUtils.isNotBlank(ip)&&ip.matches(REGX_IP);
    }
    
    /**
     * ip是否为ip，或 *为间隔的通配符地址
     * @param ip
     * @return boolean
     */
    public static boolean isIpWildCard(String ip){
        return StringUtils.isNotBlank(ip)&&ip.matches(REGX_IP_WILDCARD);
    }

    /**
     * 是否为特定格式如:“10.10.10.1-10.10.10.99”的ip段字符串
     * @param ipSeg
     * @return boolean
     */
    public static boolean isIPSegment(String ipSeg){
        return StringUtils.isNotBlank(ipSeg)&&ipSeg.matches(REGX_IP_SEG);
    }

    public static boolean isCorrectInput(String input){
        return StringUtils.isNotBlank(input)&&input.matches(REGX_INPUT);
    }

    public static void main(String[] args){
        System.out.println(isIP("192.0.0.0"));
        System.out.println(isIPSegment("192.0.0.0-192.0.0.50"));
        System.out.println(ipIsInNet("192.0.0.0-192.0.0.50","192.0.0.56"));
        System.out.println(isCorrectInput("192.0.0.0;192.0.0.0-192.0.0.50;192.168.2.3;192.0.0.0-192.0.0.50;4.3.*.*"));
        System.out.println(isMatchedIp("192.0.0.0;196.0.0.50-196.0.0.0;192.168.2.3;192.0.0.0-192.0.0.50;4.3.*.*", "196.0.0.25"));
        System.out.println(isMatchedIp("192.0.0.0;196.0.0.50-196.0.0.0;192.168.2.3;192.0.0.0-192.0.0.50;*.*.*.*", "251.3.3.6"));
        System.out.println("12.*.*.*".matches(REGX_IP_WILDCARD));
        System.out.println(ipIsInWildCard("125.123.1.*","125.123.1.12"));
    }

    /**
     * ip是否符合过滤串规则
     * @author zhouqin
     * @param filter
     * @param ip
     * @return boolean
     */
    public static boolean isMatchedIp(String filter, String ip){
        if(isCorrectInput(filter)&&isIP(ip)){
            String[] ips = filter.split(";");
            for(String iStr :ips){
                if(isIP(iStr)){
                   if(iStr.equals(ip)) return true;
                }else if(isIpWildCard(iStr)){
                   if(ipIsInWildCardNoCheck(iStr,ip)) return true;
                }else if(isIPSegment(iStr)){
                   if(ipIsInNetNoCheck(iStr,ip)) return true;
                }
            }
        }else {
            return false;
        }
        return false;
    }

    /**
     * 检测参数是否在ip通配符里。
     * @author zhouqin
     * @param ipWildCard
     * @param ip
     * @return boolean
     */
    public static boolean ipIsInWildCard(String ipWildCard, String ip){
        if (!isIpWildCard(ipWildCard) || !isIP(ip)){
            return false;
        }
        String[] s1 = ipWildCard.split("\\.");
        String[] s2 = ip.split("\\.");
        boolean isMatchedSeg = true;
        for(int i=0;i<s1.length&&!s1[i].equals("*");i++){
            if(!s1[i].equals(s2[i])){
                isMatchedSeg = false;
                break;
            }
        }
        return isMatchedSeg;
    }

    /**
     * 检测参数是否在ip通配符里,不检测参数格式，提高效率。
     * @author zhouqin
     * @param ipWildCard
     * @param ip
     * @return boolean
     */
    public static boolean ipIsInWildCardNoCheck(String ipWildCard, String ip){
        String[] s1 = ipWildCard.split("\\.");
        String[] s2 = ip.split("\\.");
        boolean isMatchedSeg = true;
        for(int i=0;i<s1.length&&!s1[i].equals("*");i++){
            if(!s1[i].equals(s2[i])){
                isMatchedSeg = false;
                break;
            }
        }
        return isMatchedSeg;
    }

    /**
     * 判断ip是否在指定网段中
     * @author dh from baidu.已验证
     * @param iparea
     * @param ip
     * @return boolean
     */
    public static boolean ipIsInNet(String iparea, String ip) {
        if (!isIPSegment(iparea) || !isIP(ip))
            return false;
        int idx = iparea.indexOf('-');
        String[] sips = iparea.substring(0, idx).split("\\.");
        String[] sipe = iparea.substring(idx + 1).split("\\.");
        String[] sipt = ip.split("\\.");
        long ips = 0L, ipe = 0L, ipt = 0L;
        for (int i = 0; i < 4; ++i) {
            ips = ips << 8 | Integer.parseInt(sips[i]);
            ipe = ipe << 8 | Integer.parseInt(sipe[i]);
            ipt = ipt << 8 | Integer.parseInt(sipt[i]);
        }
        if (ips > ipe) {
            long t = ips;
            ips = ipe;
            ipe = t;
        }
        return ips <= ipt && ipt <= ipe;
    }

    /**
     * 判断ip是否在指定网段中，不检测参数格式，提高效率。
     * @param iparea
     * @param ip
     * @return boolean
     */
    public static boolean ipIsInNetNoCheck(String iparea, String ip) {
        int idx = iparea.indexOf('-');
        String[] sips = iparea.substring(0, idx).split("\\.");
        String[] sipe = iparea.substring(idx + 1).split("\\.");
        String[] sipt = ip.split("\\.");
        long ips = 0L, ipe = 0L, ipt = 0L;
        for (int i = 0; i < 4; ++i) {
            ips = ips << 8 | Integer.parseInt(sips[i]);
            ipe = ipe << 8 | Integer.parseInt(sipe[i]);
            ipt = ipt << 8 | Integer.parseInt(sipt[i]);
        }
        if (ips > ipe) {
            long t = ips;
            ips = ipe;
            ipe = t;
        }
        return ips <= ipt && ipt <= ipe;
    }
}
