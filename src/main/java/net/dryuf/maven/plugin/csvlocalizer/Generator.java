package net.dryuf.maven.plugin.csvlocalizer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import net.dryuf.maven.plugin.csvlocalizer.writer.LocalizationWriter;
import net.dryuf.maven.plugin.csvlocalizer.writer.PropertiesLocalizationWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;


/**
 * Main generator class
 */
public class Generator
{
	public void			execute() throws IOException
	{
		if (localizationWriters == null)
			localizationWriters = Collections.singleton((LocalizationWriter)new PropertiesLocalizationWriter());
		parseSources();
		configuration.getOutputDirectory().mkdirs();
		writeMessages();
		translations = null;
	}

	public void			setConfiguration(Configuration configuration)
	{
		this.configuration = configuration;
	}

	public void			setSources(String[] sources)
	{
		this.sources = sources;
	}

	public Generator		setWriters(Iterable<LocalizationWriter> localizationWriters)
	{
		this.localizationWriters = localizationWriters;
		return this;
	}

	private void			parseSources() throws IOException
	{
		translations = new LinkedHashMap<String, NavigableMap<String, NavigableMap<String, String>>>();
		for (String source: sources) {
			parseSource(source);
		}
	}

	private void			parseSource(String source) throws IOException
	{
		CSVParser csvParser = sourceFormat.parse(openInputFile(source));
		try {
			Set<String> languages = new LinkedHashSet<String>();
			for (Map.Entry<String, Integer> headerEntry : csvParser.getHeaderMap().entrySet()) {
				String language = headerEntry.getKey();
				if (!language.isEmpty() && headerEntry.getValue() >= 3) {
					if (!language.matches("^\\w+$"))
						throw new IllegalArgumentException(source+":1: Invalid language, must contain only word characters: "+language);
					languages.add(language);
					if (!translations.containsKey(language))
						translations.put(language, new TreeMap<String, NavigableMap<String, String>>());
				}
			}
			for (Iterator<CSVRecord> recordIterator = csvParser.iterator(); recordIterator.hasNext(); ) {
				CSVRecord record = recordIterator.next();
				String comment = record.get(0);
				String clazz = record.get(1);
				String message = record.get(2);
				if (comment.startsWith("#") || clazz.isEmpty() || message.isEmpty())
					continue;
				if (!clazz.matches("^(\\w+\\.)*\\w+$"))
					throw new IllegalArgumentException(source+":1: Invalid class name, must be in package.ClassName: "+clazz);
				for (String language : languages) {
					NavigableMap<String, String> messageMap = translations.get(language).get(clazz);
					if (messageMap == null)
						translations.get(language).put(clazz, messageMap = new TreeMap<String, String>());
					messageMap.put(message, record.get(language));
				}
			}
		}
		finally {
			csvParser.close();
		}
	}

	private Reader			openInputFile(String filename) throws IOException
	{
		if (filename.startsWith("classpath:")) {
			String name = "/"+filename.substring(10);
			InputStream stream = getClass().getResourceAsStream(name);
			if (stream == null)
				throw new FileNotFoundException(name);
			return new InputStreamReader(stream);
		}
		else {
			return new InputStreamReader(new FileInputStream(new File(filename)), configuration.getCharsetEncoding());
		}
	}

	private void			writeMessages() throws IOException
	{
		for (LocalizationWriter localizationWriter: localizationWriters) {
			for (Map.Entry<String, NavigableMap<String, NavigableMap<String, String>>> languageEntry : translations.entrySet()) {
				localizationWriter.writeMessages(configuration, languageEntry.getKey(), languageEntry.getValue());
			}
		}
	}

	protected Configuration		configuration;

	protected String[]		sources;

	protected Iterable<LocalizationWriter>	localizationWriters;

	private LinkedHashMap<String, NavigableMap<String, NavigableMap<String, String>>> translations;

	private static CSVFormat	sourceFormat = CSVFormat.DEFAULT.withHeader().withAllowMissingColumnNames();
}
