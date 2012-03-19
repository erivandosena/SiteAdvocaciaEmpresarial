package br.net.rwd.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public class Imagem {
	
	// Retorna o conteúdo do arquivo em StreamedContent
	public static StreamedContent exibeImagem(String nomeArq,int localArq, String mimeArq) {
		InputStream is = null;
		byte[] bytes = null;
		File file = null;
		
		try {
			try {
				switch (localArq) {
				case 1:
					file = new File(Constantes.CAMINHO_IMG+"/"+nomeArq);
					break;

				default:
					file = new File(Constantes.CAMINHO_IMG_PADRAO);
					mimeArq = "image/gif";
					break;
				}
				
				if(!file.exists()) {
					file = new File(Constantes.CAMINHO_IMG_PADRAO);
					mimeArq = "image/gif";
				}
				
				is = new FileInputStream(file);
				long length = file.length();
				if (length > Integer.MAX_VALUE) {
				}
				bytes = new byte[(int) length];
				int offset = 0;
				int numRead = 0;
				while (offset < bytes.length
						&& (numRead = is.read(bytes, offset, bytes.length
								- offset)) >= 0) {
					offset += numRead;
				}
				if (offset < bytes.length) {
					Logger.getLogger(Imagem.class.getName()).log(
							Level.INFO,"Não foi possível ler completamente o arquivo "
									+ file.getName());
				}
			} catch (Exception e) {
				Logger.getLogger(Imagem.class.getName()).log(Level.SEVERE,
						null, e);
			}
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Logger.getLogger(Imagem.class.getName()).log(Level.SEVERE,
						null, e);
			}
		}
		  return new DefaultStreamedContent(new ByteArrayInputStream(bytes), mimeArq);
	}

}
