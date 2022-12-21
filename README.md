# easy-resources-core 

![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/jaygazula27/easy-resources-core/maven-build.yml)
![Maven Central](https://img.shields.io/maven-central/v/com.jgazula/easy-resources-core)
![GitHub](https://img.shields.io/github/license/jaygazula27/easy-resources-core)


## Overview

This repository contains the core parsing and class generation logic of 
the [easy-resources-maven-plugin](https://github.com/jaygazula27/easy-resources-maven-plugin) and 
the [easy-resources-gradle-plugin](https://github.com/jaygazula27/easy-resources-gradle-plugin). 
For information on how to use these plugins, please view the corresponding repositories.

The goal of this project is to enable easy and typesafe access to resources such as properties files, i18n bundles, etc.


## Development

* Requires Java 11 and Maven 3.
* To build the project (and run unit tests): `mvn -U clean install`
* Use the [Publish to Maven Central](https://github.com/jaygazula27/easy-resources-core/actions/workflows/maven-publish.yml) workflow to deploy to maven central.
  * Needs the following secrets: `OSSRH_USERNAME`, `OSSRH_PASSWORD`, `MAVEN_GPG_PASSPHRASE`, and `MAVEN_GPG_PRIVATE_KEY`


## License

MIT License. Please see the [LICENSE](LICENSE) file for more information.