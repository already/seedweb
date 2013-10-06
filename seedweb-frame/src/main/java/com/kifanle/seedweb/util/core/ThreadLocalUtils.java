package com.kifanle.seedweb.util.core;

import java.util.HashMap;
import java.util.Map;

/**
 * ThreadLocal工具
 * User: zhouqin
 * Date: 13-8-2
 * Time: 下午4:06
 */
public class ThreadLocalUtils<K,V> {
    @SuppressWarnings("rawtypes")
	private static final ThreadLocal<Map> session = new ThreadLocal<Map>(){
        protected synchronized Map initialValue() {
            return new HashMap();
        }
    };

    private ThreadLocalUtils(){

    }

    @SuppressWarnings("rawtypes")
	public static ThreadLocalUtils getInstance(){
        return new ThreadLocalUtils();
    }

    @SuppressWarnings("unchecked")
	public void save(K k,V v){
        session.get().put(k,v);
    }

    @SuppressWarnings("unchecked")
	public V get(K k){
        return (V)session.get().get(k);
    }

    @SuppressWarnings("unchecked")
	public V remove(K k){
        return (V)session.get().remove(k);
    }

}
