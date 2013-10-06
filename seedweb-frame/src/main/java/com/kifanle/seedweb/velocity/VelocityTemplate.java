package com.kifanle.seedweb.velocity;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.io.VelocityWriter;
import org.apache.velocity.util.SimplePool;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class VelocityTemplate {
	private static final Logger log = LoggerFactory.getLogger(VelocityTemplate.class);
	
	private static SimplePool writerPool = new SimplePool(100);

	public String incoding = "utf-8";
	
	public String outcoding = "utf-8";
	
	public String resourceLoader = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";
	
	public String jarPath = "";

	public String getIncoding() {
		return incoding;
	}
	public void setIncoding(String incoding) {
		this.incoding = incoding;
	}
	public String getOutcoding() {
		return outcoding;
	}
	public void setOutcoding(String outcoding) {
		this.outcoding = outcoding;
	}
	public String getResourceLoader() {
		return resourceLoader;
	}
	public void setResourceLoader(String resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	public String getJarPath() {
		return jarPath;
	}
	public void setJarPath(String jarPath) {
		this.jarPath = jarPath;
	}

	@PostConstruct
	public void init(){
		try {
	        Properties properties=new Properties();
	        properties.setProperty("input.encoding", incoding);
	        properties.setProperty("output.encoding", outcoding);
	        
	        if(StringUtils.isNotBlank(resourceLoader)&&resourceLoader.trim().equals("org.apache.velocity.runtime.resource.loader.JarResourceLoader")){
	        	 properties.setProperty("resource.loader", "jar");
	        	 properties.setProperty("jar.resource.loader.class", "org.apache.velocity.runtime.resource.loader.JarResourceLoader");
	        	 properties.setProperty("jar.resource.loader.path", "jar:file:"+jarPath);
	        }else{
	        	 properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
	        }
	        
			Velocity.init(properties);
		} catch (Exception e) {
			log.error("[velocity init error]", e);
		}
	}
	 /**
     *  merges the template with the context.  Only override this if you really, really
     *  really need to. (And don't call us with questions if it breaks :)
     *
     *  @param template template object returned by the handleRequest() method
     *  @param context  context created by the createContext() method
     *  @param response servlet reponse (use this to get the output stream or Writer
     * @throws ResourceNotFoundException
     * @throws ParseErrorException
     * @throws MethodInvocationException
     * @throws java.io.IOException
     * @throws java.io.UnsupportedEncodingException
     * @throws Exception
     */
    public static void mergeTemplate( Template template, Context context, HttpServletResponse response )
        throws ResourceNotFoundException, ParseErrorException,
               MethodInvocationException, IOException, UnsupportedEncodingException, Exception
    {
        ServletOutputStream output = response.getOutputStream();
        VelocityWriter vw = null;
        String encoding = response.getCharacterEncoding();
        try
        {
            vw = (VelocityWriter) writerPool.get();

            if (vw == null)
            {
                vw = new VelocityWriter(new OutputStreamWriter(output,
                                                               encoding),
                                        4 * 1024, true);
            }
            else
            {
                vw.recycle(new OutputStreamWriter(output, encoding));
            }

            template.merge(context, vw);
        }
        finally
        {
            if (vw != null)
            {
                try
                {
                    /*
                     *  flush and put back into the pool
                     *  don't close to allow us to play
                     *  nicely with others.
                     */
                    vw.flush();
                }
                catch (IOException e)
                {
                    // do nothing
                }

                /*
                 * Clear the VelocityWriter's reference to its
                 * internal OutputStreamWriter to allow the latter
                 * to be GC'd while vw is pooled.
                 */
                vw.recycle(null);
                writerPool.put(vw);
            }
        }
    }
	
	/**
	 * 获取并解析模版
	 * @param templateFile
	 * @return template
	 * @throws Exception
	 */
	public static Template getTemplate(String templateFile) {
		Template template = null;
		try {
			template = Velocity.getTemplate("/templates"+templateFile);
		} catch (ResourceNotFoundException rnfe) {
			log.error("buildTemplate error : cannot find template " + templateFile);
		} catch (ParseErrorException pee) {
			log.error("buildTemplate error in template " + templateFile + ":" + pee);
		} catch (Exception e) {
			log.error("buildTemplate error in template " + templateFile + ":" + e);
		}
		return template;
	}

}
