package myStuff.rest.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonData {

	private final List<String> alist;
	private final String value;
	private final int id;

	@JsonCreator
	public JsonData(@JsonProperty("id") int id,
			@JsonProperty("value") String value,
			@JsonProperty("alist") List<String> alist) {
		this.id = id;
		this.value = value;
		this.alist = alist;
	}

	public List<String> getAlist() {
		return alist;
	}

	public String getValue() {
		return value;
	}

	public int getId() {
		return id;
	}
}
