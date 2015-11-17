package net.dryuf.maven.plugin.csvlocalizer.writer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import net.dryuf.maven.plugin.csvlocalizer.Configuration;
import net.dryuf.maven.plugin.csvlocalizer.FileUtil;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.NavigableMap;


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
				FileUtil.updateFile(new File(new File(new File(configuration.getOutputDirectory(), language), "_class"), clazz+".localize.csvdb"), serializeProperties(configuration, clazzProperties));
			}
		}
		printer.close();
		if (configuration.getGenerateMainMessages()) {
			byte[] content = serializeProperties(configuration, properties);
			new File(configuration.getOutputDirectory(), language).mkdirs();
			File outputFile = new File(new File(configuration.getOutputDirectory(), language), "_messages.localize.csvdb");
			FileUtil.updateFile(outputFile, content);
		}
	}

	private byte[]			serializeProperties(Configuration configuration, StringBuilder properties) throws IOException
	{
		CharArrayWriter cmpOutput = new CharArrayWriter();
		return properties.toString().getBytes(configuration.getCharsetEncoding());
	}

	private static CSVFormat	printerFormat = CSVFormat.DEFAULT;
}
