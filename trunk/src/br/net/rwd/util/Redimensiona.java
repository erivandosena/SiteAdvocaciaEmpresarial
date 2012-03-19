package br.net.rwd.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class Redimensiona {

	// Método para redimensionar imagens, criar thubmnails (miniaturas)
	public static byte[] redimensionaImg(byte[] bytesArq,int alturaFixa) {
		
		BufferedImage miniatura = null;
		byte[] bytesOut = null;
		String extensao = "png";
		
		try {
			BufferedImage bi = ImageIO.read(new ByteArrayInputStream(bytesArq));
			
			int larguraImg   = bi.getWidth();
			int alturaImg   = bi.getHeight();
			int novaLargura = ( larguraImg * alturaFixa ) / alturaImg;
			
			miniatura = new BufferedImage(((int) novaLargura) < 1 ? 1 : (int) novaLargura, ((int) alturaFixa) < 1 ? 1 : (int) alturaFixa, bi.getType() == BufferedImage.TYPE_CUSTOM ? BufferedImage.TYPE_INT_ARGB : bi.getType());
			
			Graphics2D g2d = miniatura.createGraphics();
			//g2d.setComposite(AlphaComposite.Src);
			
			RenderingHints rh = g2d.getRenderingHints (); 
			
			rh.put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			rh.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		//	rh.put(RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_ENABLE);
		//	rh.put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//	rh.put(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			rh.put(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			rh.put(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		//	rh.put(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);
			
			g2d.setRenderingHints(rh); 
			g2d.drawImage(bi, 0, 0, novaLargura, alturaFixa, null);
			g2d.dispose();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			/*
			//Extensao do arquivo
			String extensaoArq = mimeArq.substring(mimeArq.lastIndexOf('/') + 1);
			if (("jpeg".equals(extensaoArq)) || ("png".equals(extensaoArq)))
				extensao = extensaoArq;
			else if ("gif".equals(extensaoArq))
				extensao = "jpg";
			else
				extensao = "png";
			*/	
			
			ImageIO.write(miniatura, extensao, baos);
			bytesOut = baos.toByteArray();
			//InputStream is = new ByteArrayInputStream(bytesOut);

		} catch (IOException e) {
			// e.printStackTrace();
			Logger.getLogger(Redimensiona.class.getName()).log(Level.SEVERE,null, e);
		}
		return bytesOut;
	}
		
}
