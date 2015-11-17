package net.dryuf.maven.plugin.csvlocalizer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import net.dryuf.maven.plugin.csvlocalizer.writer.LocalizationWriter;
import net.dryuf.maven.plugin.csvlocalizer.writer.PropertiesLocalizationWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
public class Configuration
{
	public void			setLanguages(String[] languages)
	{
		this.languages = languages;
	}

	public String[]			getLanguages()
	{
		return languages;
	}

	public void			setCharsetEncoding(String charsetEncoding)
	{
		this.charsetEncoding = charsetEncoding;
	}

	public String			getCharsetEncoding()
	{
		return charsetEncoding;
	}

	public void			setOutputDirectory(File outputDirectory)
	{
		this.outputDirectory = outputDirectory;
	}

	public File			getOutputDirectory()
	{
		return outputDirectory;
	}

	public boolean			getGenerateMainMessages()
	{
		return generateMainMessages;
	}

	public void			setGenerateMainMessages(boolean generateMainMessages)
	{
		this.generateMainMessages = generateMainMessages;
	}

	public boolean			getGenerateClassMessages()
	{
		return generateClassMessages;
	}

	public void			setGenerateClassMessages(boolean generateClassMessages)
	{
		this.generateClassMessages = generateClassMessages;
	}

	protected String[]		languages;

	protected String		charsetEncoding;

	protected File			outputDirectory;

	protected boolean		generateMainMessages;

	protected boolean		generateClassMessages;
}
