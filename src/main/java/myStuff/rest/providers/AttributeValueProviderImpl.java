package myStuff.rest.providers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AttributeValueProviderImpl implements AttributeValueProvider {
	
	Map<String, String> values = new ConcurrentHashMap<>();

	@Override
	public String getValue(String key) {
		if ( values.containsKey(key) ) {
			return values.get(key);
		}
		return "";
	}

	@Override
	public void setValue(String key, String value) {
		values.put(key, value);
	}

}
