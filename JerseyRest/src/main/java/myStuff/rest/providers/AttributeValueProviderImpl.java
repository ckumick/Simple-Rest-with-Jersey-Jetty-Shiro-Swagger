package myStuff.rest.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AttributeValueProviderImpl implements AttributeValueProvider {
	
	Map<String, String> values = new ConcurrentHashMap<>();
	private List<AttributeValueListener> listeners = new ArrayList<>();

	@Override
	public String getValue(String key) {
		if ( values.containsKey(key) ) {
			return values.get(key);
		}
		return "";
	}

	@Override
	public synchronized void setValue(String key, String value) {
		values.put(key, value);
		for(AttributeValueListener listener: listeners) {
			listener.update(key, value);
		}
	}

	@Override
	public synchronized void addListenner(AttributeValueListener listener) {
		listeners.add(listener);
	}

	@Override
	public synchronized void removeListener(AttributeValueListener listener) {
		listeners.remove(listener);
	}

}
