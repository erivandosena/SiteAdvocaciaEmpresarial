package br.net.rwd.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CriaArquivo {
	
	//para salvar no disco
	public static void criaArq(byte[] bytesArq, String nomeArq, int tamanhoArq) {
		FileOutputStream fos;
		try {
			switch (tamanhoArq) {
			case 1: 
				fos = new FileOutputStream(Constantes.CAMINHO_IMG+"/"+nomeArq);
				break;

			default:
				fos = new FileOutputStream(Constantes.CAMINHO_IMG_PADRAO);
				break;
			}
			//fos = new FileOutputStream(caminhoArq);
			fos.write(bytesArq);
			fos.close();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(CriaArquivo.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(CriaArquivo.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
