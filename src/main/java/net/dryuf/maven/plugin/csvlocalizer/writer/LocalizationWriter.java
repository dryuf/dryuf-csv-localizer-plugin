package net.dryuf.maven.plugin.csvlocalizer.writer;

import net.dryuf.maven.plugin.csvlocalizer.Configuration;

import java.io.IOException;
import java.util.NavigableMap;

/**
 * Component which actually writes the localization files.
 */
public interface LocalizationWriter
{
	/**
	 * Write the messages into outputDirectory DB.
	 *
	 * @param configuration
	 * 	common configuration
	 * @param language
	 * 	language identifier
	 * @param messages
	 * 	list of message mapping
	 */
	void				writeMessages(Configuration configuration, String language, NavigableMap<String, NavigableMap<String, String>> messages) throws IOException;
}
