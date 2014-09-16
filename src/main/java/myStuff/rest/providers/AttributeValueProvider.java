package myStuff.rest.providers;

public interface AttributeValueProvider {

	String getValue(String key);
	
	void setValue(String key, String value);
}
