package org.druf.maven.plugin.csvlocalizer;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.druf.maven.plugin.csvlocalizer.writer.LocalizationWriter;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


/**
 * CSV to localization messages generator.
 */
@Mojo(name = "generate-localization", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class GeneratorMojo extends AbstractMojo
{
	@SuppressWarnings("unchecked")
	@Override
	public void			execute() throws MojoExecutionException, MojoFailureException
	{
		Generator generator = new Generator();
		Configuration configuration = new Configuration();
		configuration.setLanguages(languages);
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


	@Parameter(required = true)
	protected String[]		languages;

	@Parameter(required = true)
	protected String[]		sources;

	@Parameter(required = true, defaultValue = "${project.build.directory}/generated-resources/localize/")
	protected File			outputDirectory;

	@Parameter(required = false)
	protected String[]		writers;

	@Parameter(required = false)
	protected boolean		generateClassMessages = true;

	@Parameter(required = false)
	protected boolean		generateMainMessages = true;
}
