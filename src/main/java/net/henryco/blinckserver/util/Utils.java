package net.henryco.blinckserver.util;

import org.springframework.web.multipart.MultipartFile;

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


}
