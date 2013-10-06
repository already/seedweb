package com.kifanle.seedweb.util.core;

import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.kifanle.seedweb.util.ext.IPMatchHelper;

/**
 * 路径|文件后缀解析工具类 <br>
 * 用来解析 http path ，支持:<br>
 * plain   格式为“/path1/path2”<br>  
 * restful 格式为“/path1/{id}/path2/{info}”<br>
 * @author zhouqin
 *
 */
public class PathUtils {

	public final static String REGX_UNIT = "((/\\{\\w*\\})|(/\\w+))";
	
	public final static String REGX_PATH = REGX_UNIT +"+"+"/{0,1}";
	
	public static boolean isAcceptPath(String path){
		return StringUtils.isNotBlank(path)&&path.matches(REGX_PATH);
	}
	
	public static LinkedHashSet<String> getRestParamsMeta(String path){
		LinkedHashSet<String> mm = new LinkedHashSet<String>();
		Pattern  p = Pattern.compile("\\{\\w*\\}");
		Matcher m = p.matcher(path);
		while(m.find()){
			String group = m.group();
			mm.add(group.substring(1, group.length()-1));
		}
		return mm;
	}
	
	public static LinkedHashSet<String> getRestParamsValue(String regx,String path){
		LinkedHashSet<String> mm = new LinkedHashSet<String>();
		Pattern  p = Pattern.compile(regx);
		Matcher m = p.matcher(path);
		while(m.find()){
			for(int i =1;i<=m.groupCount();i++){
				mm.add(m.group(i));
			}
			break;
		}
		return mm;
	}
	
	/**
	 * 将转换/{xx}/xx 转换成 /_/xx
	 * @param path
	 * @return
	 */
	public static String convert(String path){
		return path.replaceAll("\\{\\w+\\}", "_");
	}
	
	public static String path2Regx(String path){
		return path.replaceAll("\\{\\w+\\}", "(\\\\w+)");
	}
	
	public static boolean isIndex(String path){
		return path.matches("/\\s*|\\s");
	}
	
	/**
	 * 获取扩展名
	 * @param filename
	 * @return
	 */
	public static String getExt(String filename){
		if(filename==null||filename.isEmpty()) return "";
		if(filename.lastIndexOf(".")>-1){
			return filename.substring(filename.lastIndexOf(".")+1);
		}else{
			return "";
		}
	}
	
	public static String getResourcesPath(HttpServletRequest request){
		return getPath(request).replaceFirst("/", "");
	}
	
	 /**
     * 获取用户访问URL中的根域名
     * 例如: www.dlog.cn -> dlog.cn
     * @param host 域名
     * @return 根域名
     */
    public static String getDomainName(String host){
        if(IPMatchHelper.isIP(host))
            return null;
        String[] names = StringUtils.split(host, '.');
        int len = names.length;
        if(len==1) return null;
        if(len==3){
            return StringUtils.join(names,".",len-2,len); 
        }
        if(len>3){
            String dp = names[len-2];
            if(dp.equalsIgnoreCase("com")||dp.equalsIgnoreCase("gov")||dp.equalsIgnoreCase("net")||dp.equalsIgnoreCase("edu")||dp.equalsIgnoreCase("org"))
                return StringUtils.join(names,".",len-3,len);
            else
                return StringUtils.join(names,".",len-2,len);
        }
        return host;
    }
    
	 /**
     * Retrieves the path for the specified request regardless of
     * whether this is a direct request or an include by the
     * RequestDispatcher.
     */
    public static String getPath(HttpServletRequest request){
        // If we get here from RequestDispatcher.include(), getServletPath()
        // will return the original (wrong) URI requested.  The following special
        // attribute holds the correct path.  See section 8.3 of the Servlet
        // 2.3 specification.
        String path = (String)request.getAttribute("javax.servlet.include.servlet_path");
        // also take into account the PathInfo stated on SRV.4.4 Request Path Elements
        String info = (String)request.getAttribute("javax.servlet.include.path_info");
        if (path == null)
        {
            path = request.getServletPath();
            info = request.getPathInfo();
        }
        if (info != null)
        {
            path += info;
        }
        return path;
    }
}
