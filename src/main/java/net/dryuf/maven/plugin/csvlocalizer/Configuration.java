package net.dryuf.maven.plugin.csvlocalizer;

import java.io.File;


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
