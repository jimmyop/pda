package com.mcms.commonlib.request.sign;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 键排序MAP
 * @author 丁伟
 *
 */
public class KeySortedMap extends TreeMap<String, String> {
	
    private static final long serialVersionUID = -2197512413390368847L;

	public KeySortedMap(Map<String,String> map)
	{
		super(new SortComparator());
		this.putAll(map);
	}	
	
	private static class SortComparator implements Comparator<String> 
	{
		public int compare(String key1, String key2) {
			return key1.compareTo(key2);
		}
		
	}
	
}
