# Vonage Server SDK for Java

[![Maven Release](https://maven-badges.herokuapp.com/maven-central/com.vonage/client/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.vonage/client)
[![Build Status](https://github.com/Vonage/vonage-java-sdk/actions/workflows/build.yml/badge.svg)](https://github.com/Vonage/vonage-java-sdk/actions/workflows/build.yml?query=workflow%3A"Build+Java+CI")
[![codecov](https://codecov.io/gh/vonage/vonage-java-sdk/branch/main/graph/badge.svg)](https://codecov.io/gh/vonage/vonage-java-sdk)
[![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-v2.0%20adopted-ff69b4.svg)](CODE_OF_CONDUCT.md)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)][license]

<img src="https://developer.nexmo.com/assets/images/Vonage_Nexmo.svg" height="48px" alt="Nexmo is now known as Vonage" />

You can use this Java Server SDK to add [Vonage APIs](https://developer.vonage.com/api) to your application. To use this, you'll
need a Vonage account. You'll need to have [created a Vonage account][signup].

* [Supported APIs](#supported-apis)
* [Installation](#installation)
* [Usage](#usage)
* [Configuration](#configuration)
* [FAQ](#frequently-asked-questions)
* [Contribute!](#contribute)

## Supported APIs

- [Account](https://developer.vonage.com/en/account/overview)
- [Application](https://developer.vonage.com/en/application/overview)
- [Meetings](https://developer.vonage.com/en/meetings/overview)
- [Messages](https://developer.vonage.com/en/messages/overview)
- [Number Insight](https://developer.vonage.com/en/number-insight/overview)
- [Number Management](https://developer.vonage.com/en/numbers/overview)
- [Proactive Connect](https://developer.vonage.com/en/proactive-connect/overview)
- [Redact](https://developer.vonage.com/en/redact/overview)
- [SMS](https://developer.vonage.com/en/sms/overview)
- [Subaccounts](https://developer.vonage.com/en/subaccounts/overview)
- [Verify](https://developer.vonage.com/en/verify/overview)
- [Voice](https://developer.vonage.com/en/voice/overview)


## Installation

Releases are published to [Maven Central](https://central.sonatype.com/artifact/com.vonage/client/7.6.0/snippets).
Instructions for your build system can be found in the snippets section.
They're also available from [here](https://mvnrepository.com/artifact/com.vonage/client/7.6.0).
Release notes can be found in the [changelog][changelog].

### Build It Yourself

Alternatively you can clone the repo and build the JAR file yourself:

```bash
git clone git@github.com:vonage/vonage-java-sdk.git
gradle build
```

### Download everything in a ZIP file

**Note**: We *strongly recommend* that you use a tool that supports dependency management, such as [Gradle], [Maven],
or [Ivy].

We provide a [ZIP file for each release](https://github.com/Vonage/vonage-java-sdk/releases/),
containing the Java Server SDK JAR, along with all the dependencies. Download the file, unzip it, and add the JAR files
to your project's classpath.

[Gradle]: https://gradle.org/
[Maven]: https://maven.apache.org/
[Ivy]: http://ant.apache.org/ivy/


## Usage

* For help understanding our APIs, check out our awesome [developer portal](https://developer.vonage.com/).
* Check the [Javadoc](https://www.javadoc.io/doc/com.vonage/client/latest/index.html) for full reference documentation.
* There are also **many useful code samples** in our [Vonage/vonage-java-code-snippets](https://github.com/Vonage/vonage-java-code-snippets) repository.

## Configuration

### Customize the Base URI
By default, the client will use https://api.nexmo.com, https://rest.nexmo.com, https://sns.nexmo.com and https://api-eu.vonage.com as base URIs for the various endpoints. To customize these you can instantiate `VonageClient` with an `HttpConfig` object.

`HttpConfig.Builder` has been created to assist in building this object. Usage is as follows:

```java
HttpConfig httpConfig = HttpConfig.builder()
		.apiBaseUri("https://api.example.com")
		.restBaseUri("https://rest.example.com")
		.snsBaseUri("https://sns.example.com")
		.apiEuBaseUri("https://api-eu.example.com")
		.build();

VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .httpConfig(httpConfig)
        .build();
```

If you do not specify a property, it will take on whatever the default value is. You can also set all three with a single method:

```java
HttpConfig httpConfig = HttpConfig.builder().baseUri("http://example.com").build();

VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .httpConfig(httpConfig)
        .build();
```

To keep the default values, you can use `HttpConfig.defaultConfig()`:

```java
HttpConfig httpConfig = HttpConfig.defaultConfig();

VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .httpConfig(httpConfig)
        .build();
```

You can also instantiate without the parameter:

```java
VonageClient client = VonageClient.builder()
        .apiKey(API_KEY)
        .apiSecret(API_SECRET)
        .build();
```

### Custom HTTP Configuration

If you need to configure the Apache HttpClient used for making requests, you can
call `VonageClient.Builder.httpClient()` to supply your custom configured object. This
can be useful, for example, if you must use an HTTP proxy to make requests or to configure SSL Certificates.

## Frequently Asked Questions

**Q:** What is your policy on thread safety?

**A:** The current architecture of the SDK means that only one thread should use the client at a time.
If you would like to use the SDK in a multithreaded environment, create a separate instance of
`VonageClient` for each thread.

**Q:** Does this SDK support asynchronous request / response processing?

**A:** Currently no, but it is on the roadmap.

**Q:** Does this SDK support the Video API?

**A:**
The Vonage Video API (formerly OpenTok) is currently in beta. You can try it out by using a beta version.
Usage instructions can be found on the [8.x-beta branch](https://github.com/Vonage/vonage-java-sdk/tree/8.x-beta#video-api).
See the [Releases page](https://github.com/Vonage/vonage-java-sdk/releases) for more information.
In the meantime, you can continue using the [OpenTok Java SDK](https://github.com/opentok/Opentok-Java-SDK).

**Q:** What is the license for this SDK?

**A:** This library is released under the [Apache 2.0 License][license].

## Contribute!

_We :heart: contributions to this library!_

It is a good idea to [talk to us](https://developer.vonage.com/community/slack)
first if you plan to add any new functionality.
Otherwise, [bug reports](https://github.com/Vonage/vonage-java-sdk/issues),
[bug fixes](https://github.com/Vonage/vonage-java-sdk/pulls) and feedback on the
library are always appreciated.

[create_account]: https://developer.vonage.com/account/overview
[signup]: https://dashboard.nexmo.com/sign-up?utm_source=DEV_REL&utm_medium=github&utm_campaign=[LANGUAGE]-client-library
[doc_sms]: https://developer.vonage.com/messaging/sms/overview
[license]: LICENSE.txt
[changelog]: CHANGELOG.md
