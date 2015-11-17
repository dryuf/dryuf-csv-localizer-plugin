package net.dryuf.maven.plugin.csvlocalizer;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import net.dryuf.maven.plugin.csvlocalizer.writer.LocalizationWriter;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


/**
 * CSV to localization messages generator.
 */
@Mojo(name = "update-localization", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class GeneratorMojo extends AbstractMojo
{
	@SuppressWarnings("unchecked")
	@Override
	public void			execute() throws MojoExecutionException, MojoFailureException
	{
		Generator generator = new Generator();
		Configuration configuration = new Configuration();
		configuration.setLanguages(languages);
		configuration.setCharsetEncoding(charsetEncoding);
		configuration.setOutputDirectory(outputDirectory);
		configuration.setGenerateMainMessages(generateMainMessages);
		configuration.setGenerateClassMessages(generateClassMessages);
		generator.setConfiguration(configuration);
		generator.setSources(sources);
		if (writers != null) {
			List<LocalizationWriter> localizationWriters = new LinkedList<LocalizationWriter>();
			for (String writerName: writers) {
				if (writerName.indexOf('.') < 0) {
					writerName = LocalizationWriter.class.getPackage().getName()+"."+writerName;
				}
				try {
					localizationWriters.add(((Class<? extends LocalizationWriter>)Class.forName(writerName)).newInstance());
				}
				catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
				catch (InstantiationException e) {
					throw new RuntimeException(e);
				}
				catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
			generator.setWriters(localizationWriters);
		}
		try {
			generator.execute();
		}
		catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	/**
	 * List of languages to generate.
	 */
	@Parameter(required = true)
	protected String[]		languages;

	/**
	 * List of source CSV files to process.
	 */
	@Parameter(required = true)
	protected String[]		sources;

	/**
	 * Character set used to both reading source files and writing the generated files.
	 */
	@Parameter(required = true, defaultValue = "${project.build.sourceEncoding}")
	protected String		charsetEncoding = "UTF-8";

	/**
	 * Output directory for localization files, default is {@code generated-resources/localize/}.
	 */
	@Parameter(required = true, defaultValue = "${project.build.directory}/generated-resources/localize/")
	protected File			outputDirectory;

	/**
	 * List of output writers, by default only PropertiesLocalizationWriter is used.
	 */
	@Parameter(required = false)
	protected String[]		writers;

	/**
	 * Tells whether to generate localization files containing all messages, e.g. {@code _messages_lang.properties}.
	 */
	@Parameter(required = false)
	protected boolean		generateMainMessages = true;

	/**
	 * Tells whether to generate localization files containing messages only for relevant class.
	 */
	@Parameter(required = false)
	protected boolean		generateClassMessages = true;
}
