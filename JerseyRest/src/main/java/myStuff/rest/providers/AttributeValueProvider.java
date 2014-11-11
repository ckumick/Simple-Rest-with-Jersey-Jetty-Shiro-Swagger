package myStuff.rest.providers;

public interface AttributeValueProvider {

	String getValue(String key);
	
	void setValue(String key, String value);

	void addListenner(AttributeValueListener listener);
	
	void removeListener(AttributeValueListener listener);
}
