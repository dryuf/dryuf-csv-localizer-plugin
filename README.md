# maven-csv-localizer-plugin

Maven plugin converting translations provided in single CSV file into properties files which can be easily used by common Java frameworks.

## Usage

Typical usage is as follows:

`pom.xml` looks like:
```
	<plugin>
		<groupId>net.dryuf.maven.plugin</groupId>
		<artifactId>maven-csv-localizer-plugin</artifactId>
		<version>0.0.1</version>
		<executions>
			<execution>
				<phase>generate-resources</phase>
				<goals>
					<goal>generate-localization</goal>
				</goals>
				<configuration>
					<languages>
						<language>en</language>
						<language>cs</language>
					</languages>
					<sources>
						<source>${project.basedir}/src/main/resources/localize/myproject-translations.csv</source>
					</sources>
					<outputDirectory>${project.build.directory}/generated-resources/localize/</outputDirectory>
					<writers>
						<writer>PropertiesLocalizationWriter</writer>
						<writer>CsvdbLocalizationWriter</writer>
					</writers>
				</configuration>
			</execution>
		</executions>
	</plugin>
```

And provided `src/main/resources/localize/myproject-translations.csv`:
```
,,,en,cs
,net.dryuf.maven.plugin.csvlocalizer.Generator,Hello,Hello,Ahoj
,net.dryuf.maven.plugin.csvlocalizer.Generator,Some # inside,More #,# Another
#commented out,net.dryuf.maven.plugin.csvlocalizer.Generator,Again reasonable,Again reasonable,Again reasonable
```

This will generate `localize/_messages_en.properties` and `localize/_messages_cs.properties` with mapping relevant for respective language.

You can even easily include CSV localizations mappings from dependent jars by using dependency:unpack goal and then refering to unpacked CSV files.


## License

The code is released under version 2.0 of the [Apache License][].

## Stay in Touch

Feel free to contact me at kvr@centrum.cz or http://kvr.znj.cz/software/java/ and http://github.com/kvr000

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
