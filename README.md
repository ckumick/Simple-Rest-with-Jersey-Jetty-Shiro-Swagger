# Jersey Rest with Shiro Security, Jackson json, swagger api docs, and hk2 dependency injection

A simple implemention of a Restful web server to test about the basic usage. It has the following features:

- Uses Jersey for the JAX-RS implmentation.
- Uses Embedded Jetty as the web server. No web.xml file.
- Uses Shiro with Basic Authentication. No ini file.
- Uses Jackson for json parsing. json data class is immutable.
- Uses embedded swagger to publish api, and embeds the ui into the jetty web server at /api-doc-ui.
- Uses hk2 for dependency injection.

## Acknowledgements

Thank You to the following articles and forums for solutions:
- https://groups.google.com/forum/#!topic/swagger-swaggersocket/KHYESESD6c4 Embedding Swagger without a web.xml
- http://azurvii.blogspot.ca/2014/04/configuring-apache-shiro-shiro-web.html Embedding Shiro without a web.xml or ini file.

## License

Copyright 2014 Craig Kumick

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at [apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

