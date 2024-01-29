## json.org CVE-2022-45688 true & false positive (WTF ??)

The project contains a [fastjson](https://mvnrepository.com/artifact/com.alibaba/fastjson/1.2.80) dependency with [CVE-2022-25845](https://nvd.nist.gov/vuln/detail/CVE-2022-25845).

The vulnerability occurs as markup in JSON is interpreted as Java beans, i.e. classes are instantiated and properties are 
set by executing setter methods. This is done using reflection. If a class is in the classpath where setters 
can trigger behaviour like executing code (in the example, this class is `Trigger`), then this can be exploited. 

The interesting part is the use of reflection here, show in the below stacktrace from running the included test used to demonstrate the
vulnerability. 

```java
setName:11, Trigger
invoke0:-1, NativeMethodAccessorImpl (jdk.internal.reflect)
invoke:62, NativeMethodAccessorImpl (jdk.internal.reflect) [2]
invoke:43, DelegatingMethodAccessorImpl (jdk.internal.reflect)
invoke:566, Method (java.lang.reflect)
setValue:167, FieldDeserializer (com.alibaba.fastjson.parser.deserializer)
deserialze:155, ThrowableDeserializer (com.alibaba.fastjson.parser.deserializer)
parseObject:405, DefaultJSONParser (com.alibaba.fastjson.parser)
parse:1430, DefaultJSONParser (com.alibaba.fastjson.parser)
parse:1390, DefaultJSONParser (com.alibaba.fastjson.parser)
parse:181, JSON (com.alibaba.fastjson)
parse:191, JSON (com.alibaba.fastjson)
parse:147, JSON (com.alibaba.fastjson)
main:18, CheckJSON (scabench)
confirmCVE202225845:39, ConfirmVulnerabilitiesTests (scabench)
```

Standard meta-data based SCA have no problem identifying the vulnerability, this is "business-as-usual". However, callgraph based tools
are likely to miss it as callgraph constructions generally fail to model reflective calls. In this sense, this is 
both a true positive and a false negative, depending on the analyses used. 

Note that there is a proof-of-vulnerability test to demonstrate the vulnerability, this test (and therefore the build with `mvn test`)
fails. See [https://github.com/scabench/jsonorg-tp1](https://github.com/scabench/jsonorg-tp1) for how the test works.

### Running Software Composition Analyses

There are several sh scripts to run different analyses, result resports can be found in `scan-results`.

### Generating the SBOM

The `pom.xml` has a plugin to generate a [SBOM](https://www.cisa.gov/sbom) in [CycloneDX](https://cyclonedx.org/) format.
To do this, run `mvn cyclonedx:makePackageBom`, the SBOM can be found in
`target/` in `json` and `xml` format.

