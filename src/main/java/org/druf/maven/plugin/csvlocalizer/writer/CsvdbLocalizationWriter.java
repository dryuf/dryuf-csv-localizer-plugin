package org.druf.maven.plugin.csvlocalizer.writer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.druf.maven.plugin.csvlocalizer.Configuration;
import org.druf.maven.plugin.csvlocalizer.FileUtil;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Properties;


/**
 * {@link LocalizationWriter} which writes into .properties files.
 */
public class CsvdbLocalizationWriter implements LocalizationWriter
{
	public void			writeMessages(Configuration configuration, String language, NavigableMap<String, NavigableMap<String, String>> messages) throws IOException
	{
		StringBuilder properties = new StringBuilder();
		CSVPrinter printer = printerFormat.print(properties);
		for (Map.Entry<String, NavigableMap<String, String>> clazzMessagesEntry: messages.entrySet()) {
			String clazz = clazzMessagesEntry.getKey();
			StringBuilder clazzProperties = new StringBuilder();
			CSVPrinter clazzPrinter = printerFormat.print(clazzProperties);
			for (Map.Entry<String, String> messageEntry: clazzMessagesEntry.getValue().entrySet()) {
				printer.printRecord(clazz+"^"+messageEntry.getKey(), messageEntry.getValue());
				clazzPrinter.printRecord(clazz+"^"+messageEntry.getKey(), messageEntry.getValue());
			}
			clazzPrinter.close();
			if (configuration.getGenerateClassMessages()) {
				new File(new File(configuration.getOutputDirectory(), language), "_class").mkdirs();
				FileUtil.updateFile(new File(new File(new File(configuration.getOutputDirectory(), language), "_class"), clazz+".localize.csvdb"), serializeProperties(clazzProperties));
			}
		}
		printer.close();
		if (configuration.getGenerateMainMessages()) {
			byte[] content = serializeProperties(properties);
			new File(configuration.getOutputDirectory(), language).mkdirs();
			File outputFile = new File(new File(configuration.getOutputDirectory(), language), "_messages.localize.csvdb");
			FileUtil.updateFile(outputFile, content);
		}
	}

	private byte[]			serializeProperties(StringBuilder properties) throws IOException
	{
		CharArrayWriter cmpOutput = new CharArrayWriter();
		return properties.toString().getBytes("UTF-8");
	}

	private static CSVFormat	printerFormat = CSVFormat.DEFAULT;
}
