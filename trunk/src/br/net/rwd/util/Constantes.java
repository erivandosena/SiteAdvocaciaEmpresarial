package br.net.rwd.util;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public class Constantes {
	
	static FacesContext facesContext = FacesContext.getCurrentInstance();
	static ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();

	public static final String URL_CONSUL_PROCES_NOM = "http://www4.tjce.jus.br/sproc2/paginas/resconprocpartenova.asp?optNomeParte=2&TXT_NOMEPARTE=";
	public static final String URL_CONSUL_PROCES_NUM = "http://www4.tjce.jus.br/sproc2/paginas/ResConProc02.asp?TXT_PARAM1=";
	public static final String URL_CONSUL_PROCES_REC = "&TXT_PARAM2=";
	
	public static final String CAMINHO_IMG = scontext.getRealPath("/ckfinder/userfiles/images");
	public static final String CAMINHO_IMG_THUMB = scontext.getRealPath("/ckfinder/userfiles/_thumbs/Images");
	public static final String CAMINHO_IMG_PADRAO = scontext.getRealPath("/resources/img/sem_imagem.gif");
	
}
