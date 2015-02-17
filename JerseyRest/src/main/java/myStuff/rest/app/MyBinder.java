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
package myStuff.rest.app;

import java.io.File;

import myStuff.rest.providers.AttributeValueProvider;
import myStuff.rest.providers.AttributeValueProviderImpl;
import myStuff.rest.providers.XmlFileProvider;
import myStuff.rest.providers.XmlFileProviderImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class MyBinder extends AbstractBinder {
	
	private final File uploadFolder;

	public MyBinder(File uploadFolder) {
		this.uploadFolder = uploadFolder;
	}

	@Override
	protected void configure() {
		bind(new AttributeValueProviderImpl()).to(AttributeValueProvider.class);
		bind(new XmlFileProviderImpl(uploadFolder)).to(XmlFileProvider.class);
	}

}
