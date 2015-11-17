package net.dryuf.maven.plugin.csvlocalizer.writer;

import net.dryuf.maven.plugin.csvlocalizer.Configuration;
import net.dryuf.maven.plugin.csvlocalizer.FileUtil;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Properties;


/**
 * {@link LocalizationWriter} which writes into .properties files.
 */
public class PropertiesLocalizationWriter implements LocalizationWriter
{
	public void			writeMessages(Configuration configuration, String language, NavigableMap<String, NavigableMap<String, String>> messages) throws IOException
	{
		Properties properties = new Properties();
		for (Map.Entry<String, NavigableMap<String, String>> clazzMessagesEntry: messages.entrySet()) {
			String clazz = clazzMessagesEntry.getKey();
			Properties clazzProperties = new Properties();
			for (Map.Entry<String, String> messageEntry: clazzMessagesEntry.getValue().entrySet()) {
				properties.setProperty(clazz+"^"+messageEntry.getKey(), messageEntry.getValue());
				clazzProperties.setProperty(clazz+"^"+messageEntry.getKey(), messageEntry.getValue());
			}
			if (configuration.getGenerateClassMessages()) {
				new File(new File(configuration.getOutputDirectory(), language), "_class").mkdirs();
				FileUtil.updateFile(new File(new File(new File(configuration.getOutputDirectory(), language), "_class"), clazz+".localize.properties"), serializeProperties(configuration, clazzProperties));
			}
		}
		if (configuration.getGenerateMainMessages()) {
			byte[] content = serializeProperties(configuration, properties);
			File outputFile = new File(configuration.getOutputDirectory().toString(), "_messages_"+language+".properties");
			FileUtil.updateFile(outputFile, content);
		}
	}

	private byte[]			serializeProperties(Configuration configuration, Properties properties) throws IOException
	{
		CharArrayWriter cmpOutput = new CharArrayWriter();
		properties.store(cmpOutput, null);
		String cmpOutputStr = cmpOutput.toString().replaceAll("^#.*?\n", "");
		return cmpOutputStr.getBytes(configuration.getCharsetEncoding());
	}
}
