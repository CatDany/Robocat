package catdany.telegramapi.robocat.features;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.UUID;

import javax.imageio.ImageIO;

import catdany.telegramapi.robocat.Bot;
import catdany.telegramapi.robocat.logging.Log;
import catdany.telegramapi.robocat.telegram.Photo;

public class ImageEditor {
	
	public static final int MAX_FILE_SIZE = 1024*1024;
	
	public static boolean isFileTooBig(Photo photo) {
		return photo.getFileSize() > MAX_FILE_SIZE;
	}
	
	public static Photo getBestPhoto(Photo[] photo) {
		Photo best = null;
		for (Photo i : photo) {
			if (!isFileTooBig(i) && (best == null || i.getFileSize() > best.getFileSize())) {
				best = i;
			}
		}
		return best;
	}
	
	public static WeakReference<BufferedImage> loadImage(Photo photo, Bot bot) {
		File file = bot.downloadFile(photo.getFileId());
		if (file != null) {
			try {
				BufferedImage img = ImageIO.read(file);
				file.delete();
				return new WeakReference<BufferedImage>(img);
			} catch (IOException t) {
				Log.e("Unable to read image from file: " + file.getName(), t);
				return null;
			}
		} else {
			return null;
		}
	}
	
	public static File saveSilently(BufferedImage img) {
		File tmp = new File("tmp_" + UUID.randomUUID() + ".jpg");
		try {
			ImageIO.write(img, "JPEG", tmp);
			return tmp;
		} catch (IOException t) {
			Log.e("Unable to save image to file: " + tmp.getName(), t);
			return null;
		}
	}
	
	/**
	 * Flips an image.
	 * @param img
	 * @return Modified image. The same instance that was provided as an argument.
	 */
	public static BufferedImage flipRight(BufferedImage img) {
		for (int i = 0; i < img.getWidth()/2; i++) {
			int[] column = img.getRGB(i, 0, 1, img.getHeight(), null, 0, 1);
			img.setRGB(img.getWidth()-i-1, 0, 1, img.getHeight(), column, 0, 1);
		}
		return img;
	}
	
	/**
	 * Flips an image.
	 * @param img
	 * @return Modified image. The same instance that was provided as an argument.
	 */
	public static BufferedImage flipLeft(BufferedImage img) {
		for (int i = 0; i < img.getWidth()/2; i++) {
			int[] column = img.getRGB(img.getWidth()-i-1, 0, 1, img.getHeight(), null, 0, 1);
			img.setRGB(i, 0, 1, img.getHeight(), column, 0, 1);
		}
		return img;
	}
}
