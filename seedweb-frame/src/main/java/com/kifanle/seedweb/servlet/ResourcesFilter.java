package com.kifanle.seedweb.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kifanle.seedweb.cache.ByteCache;
import com.kifanle.seedweb.util.core.PathUtils;

/**
 * 处理css、js、pic资源的请求过滤处理,使用前请评估静态资源大小，<br>
 * 确保内存空间大于所有静态资源大小。请查看{@link com.kifanle.seedweb.cache.ByteCache}
 * @author zhouqin
 *
 */
public class ResourcesFilter implements Filter {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ResourcesFilter.class);
	private static final ByteCache cache = ByteCache.getInstance();
	
	private static final String DEFAULT_RESOURCES_PREFIX  = "webRes";
	private static final String RESOURCE_CHARSET = "UTF-8";
	private static final Map<String, String> MIME_MAP;
	
	static {
		MIME_MAP = new HashMap<String, String>();
		MIME_MAP.put("js", "application/javascript;charset="
				+ RESOURCE_CHARSET);
		MIME_MAP.put("css", "text/css;charset=" + RESOURCE_CHARSET);
		MIME_MAP.put("gif", "image/gif");
		MIME_MAP.put("png", "image/png");
		MIME_MAP.put("jpg", "image/jpeg");
		MIME_MAP.put("jpeg", "image/jpeg");
	}
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse rep = (HttpServletResponse) res;
		String resourcesPath = DEFAULT_RESOURCES_PREFIX+PathUtils.getPath((HttpServletRequest)req);
		String ext = PathUtils.getExt(resourcesPath);
		String mime = MIME_MAP.get(ext);
		if(StringUtils.isNotBlank(mime)){
				ServletOutputStream outputStream = null;
				InputStream in = null;
				try {
					outputStream = res.getOutputStream();
					byte[] buf = cache.get(resourcesPath);
					if(null==buf){
						URL url = Thread.currentThread().getContextClassLoader()
								.getResource(resourcesPath);
						if (null == url) {
							chain.doFilter(req, res);
							return;
						}
						in = url.openStream();
						buf = cache.put(resourcesPath, in);
					}
					
					if (ArrayUtils.isNotEmpty(buf)) {
						rep.setContentType(mime);
						IOUtils.write(buf, outputStream);
						rep.setContentLength(buf.length);
					} else {
						rep.sendError(HttpServletResponse.SC_NOT_FOUND);
					}

				} finally {
					IOUtils.closeQuietly(in);
					IOUtils.closeQuietly(outputStream);
				}
			} else {
				chain.doFilter(req, res);
			}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}
}
