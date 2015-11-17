package net.dryuf.maven.plugin.csvlocalizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileUtil
{
	public static void		updateFile(File file, byte[] content) throws IOException
	{
		if (file.exists() && Arrays.equals(Files.readAllBytes(file.toPath()), content))
			return;
		Files.write(file.toPath(), content);
	}
}
