package com.kifanle.seedweb.cache;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * simple memory cache,以String 为key，byte[] 为value。适用于一次写入多次读取.
 * @author zhouqin
 *
 */
public class ByteCache {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ByteCache.class);
	
	private static Map<String,Block> _name_map = new HashMap<String,Block>();
	
	private static final AtomicLong size = new AtomicLong(0L);
	
	private ByteCache(){}
	
    static class Nested{
        private static ByteCache instance = new ByteCache();
    }
	
    public static ByteCache getInstance(){
    	return Nested.instance;
    }
    
	public synchronized byte[] put(String key,InputStream input) {
		Block block = _name_map.get(key);
		if(null!=block){
			//log.info("[path is cached.][path:{}]",new Object[]{path});
			return block.getCache();
		}

		byte[] bytes = null;
		
		try {
			bytes = IOUtils.toByteArray(input);
		} catch (IOException e) {
			return null;
		}

		if ( ArrayUtils.isNotEmpty(bytes)) {
			block = new Block().setKey(key)
					.setCache(bytes);
			_name_map.put(key, block);
			size.addAndGet(bytes.length);
			//log.info("[path is not cached.][path:{}]", new Object[] { path });
			return block.getCache();
		} else {
			return null;
		}
	}
	
	public byte[] get(String path){
		Block block = _name_map.get(path);
		return null!=block?block.getCache():null;
	}
	
	public long getSize(){
		return size.longValue();
	}
	
	static class Block {
		private String key;
		private byte[] cache;
		
		public String getKey() {
			return key;
		}
		public Block setKey(String key) {
			this.key = key;
			return this;
		}
		public byte[] getCache() {
			return cache;
		}
		public Block setCache(byte[] cache) {
			this.cache = cache;
			return this;
		}
	}
}
