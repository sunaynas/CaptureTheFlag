package Assets;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

//util class that can resize and flip images
public class ImageUtil {
	public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight)
			throws IOException {
		Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH); // xImage.SCALE_DEFAULT);
		BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
		outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
		return outputImage;
	}

	public static BufferedImage flipImage(Object original, boolean flipX, boolean flipY) {
		BufferedImage originalImage = (BufferedImage) original;
		if (!flipX && !flipY)
			return originalImage;
		int x = 0;
		int y = 0;
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = flippedImage.createGraphics();
		if (flipX) {
			x = width;// w ww .j a v a2 s. c o m
			width = -width;
		}
		if (flipY) {
			y = height;
			height = -height;
		}
		g.drawImage(originalImage, x, y, width, height, null);
		g.dispose();
		return flippedImage;
	}
}
