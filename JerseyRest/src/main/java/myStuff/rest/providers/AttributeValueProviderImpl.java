/*******************************************************************************
 * Copyright (C) 2014 by Craig Kumick
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *******************************************************************************/
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
