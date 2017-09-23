package net.henryco.blinckserver.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;


/**
 * @author Henry on 17/06/17.
 */
public abstract class Utils {

	public static String arrayToString(String[] array) {
		return Arrays.toString(array);
	}

	public static String[] stringToArray(String string) {
		if (string == null) return null;
		return string.substring(1, string.length() - 1).split(", ");
	}



	/**
	 * @return name of saved file or Null
	 */
	public static String saveMultiPartFileWithOldName(MultipartFile file, String upload_path) {
		try {
			if (file.isEmpty()) return null;
			Files.write(Paths.get(upload_path + file.getOriginalFilename()), file.getBytes());
			return file.getOriginalFilename();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return New name of saved file or Null
	 */
	public static String saveMultiPartFileWithNewName(MultipartFile file, String userName, String upload_path) {
		try {
			if (file.isEmpty()) return null;
			final String fileName = file.getOriginalFilename();
			final int i = fileName.lastIndexOf(".");
			final String userHash = Integer.toString(Math.abs(userName.hashCode()));
			final String time = Long.toString(System.nanoTime());
			final String fileHash = Integer.toString(Math.abs(fileName.hashCode()));
			final String name = userHash + fileHash + time + ( i != -1 ? file.getOriginalFilename().substring(i) : "");

			Files.write(Paths.get(upload_path + name), file.getBytes());
			return name;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String saveImageFile(byte[] file, String userName, String upload_path) {

		try {
			if (file == null || file.length == 0) return null;

			String[] typo = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(file)).split("/");
			String type = typo[typo.length - 1];

			final String userHash = Integer.toString(Math.abs(userName.hashCode()));
			final String time = Long.toString(System.nanoTime());
			final String name = Math.abs(Arrays.hashCode(file)) + userHash + time + "."+type;

			File upFile = new File(upload_path);
			if (!upFile.exists())
				if (!upFile.mkdirs())
					return null;

			Files.write(Paths.get(upload_path + name), file);
			return name;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


}
