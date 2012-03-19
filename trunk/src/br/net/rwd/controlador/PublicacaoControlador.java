package br.net.rwd.controlador;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.net.rwd.dao.DaoGenerico;
import br.net.rwd.entidades.Publicacao;
import br.net.rwd.enumeracao.EnumStatus;
import br.net.rwd.util.Constantes;
import br.net.rwd.util.CriaArquivo;
import br.net.rwd.util.FacesUtil;
import br.net.rwd.util.FormataBytes;
import br.net.rwd.util.Imagem;
import br.net.rwd.util.Redimensiona;

@Controller("publicacaoControlador")
@Scope("session")
public class PublicacaoControlador {

	private Publicacao publicacao;
	private Publicacao publicacaoEdicao;
	private String nomeImgSelecionada;
	private StreamedContent imagem;

	byte[] bytesImg = null;
	String mimeArq = null;
	String nomeImg = null;
	String nomeImgAtual = null;
	File fImg = null;
	File fImgThumb = null;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	@Resource
	private DaoGenerico<Publicacao, Integer> publicacaoDao;

	@SuppressWarnings("unchecked")
	private DataModel model;

	public PublicacaoControlador() {
		this.publicacao = new Publicacao();
		exibeSemImg();
	}

	public Publicacao getPublicacao() {
		return publicacao;
	}

	public void setPublicacao(Publicacao publicacao) {
		this.publicacao = publicacao;
	}

	public Publicacao getPublicacaoEdicao() {
		return publicacaoEdicao;
	}

	public void setPublicacaoEdicao(Publicacao publicacaoEdicao) {
		this.publicacaoEdicao = publicacaoEdicao;
	}

	public String getNomeImgSelecionada() {
		return nomeImgSelecionada;
	}

	public void setNomeImgSelecionada(String nomeImgSelecionada) {
		this.nomeImgSelecionada = nomeImgSelecionada;
	}

	public StreamedContent getImagem() {
		return imagem;
	}

	public void setImagem(StreamedContent imagem) {
		this.imagem = imagem;
	}

	public DaoGenerico<Publicacao, Integer> getPublicacaoDao() {
		return publicacaoDao;
	}

	public void setPublicacaoDao(DaoGenerico<Publicacao, Integer> publicacaoDao) {
		this.publicacaoDao = publicacaoDao;
	}

	public String novaPub() {
		this.publicacao = new Publicacao();
		exibeSemImg();
		return "formPublicacao";
	}

	@SuppressWarnings("unchecked")
	public DataModel getTodos() {
		model = new ListDataModel(publicacaoDao.todos());
		return model;
	}

	public Publicacao getPublicacaoParaEditarExcluir() {
		Publicacao publicacao = (Publicacao) model.getRowData();
		return publicacao;
	}

	public String editar() {
		setPublicacaoEdicao(getPublicacaoParaEditarExcluir());
		if (publicacao.getPub_img_mime() != null) {
			imagem = Imagem.exibeImagem(publicacao.getPub_img_nome(), 1, publicacao.getPub_img_mime());
			setNomeImgSelecionada(null);
		} else {
			exibeSemImg();
		}
		return "formPublicacao";
	}

	public String salvar() {
		if (publicacao.getPub_cod() == null) {
			publicacao.setPub_visualizacao(0);
			if (publicacao.getPub_status().equals("A")) {
			try {
				publicacao.setPub_data(sdf.parse(sdf.format(new Date())));
			} catch (ParseException e) {
				Logger.getLogger(PublicacaoControlador.class.getName()).log(Level.SEVERE, null, e);
			}
			}
			nomeImgAtual = null;
			publicacaoDao.salvar(publicacao);
			salvaEmDisco();
			FacesUtil.mensInfo("Cadastrado com sucesso");
		} else {
			try {
				if (publicacao.getPub_data() == null
						&& publicacao.getPub_status().equals("A")) {
					publicacao.setPub_data(sdf.parse(sdf.format(new Date())));
				}
				if (publicacao.getPub_data() != null
						&& publicacao.getPub_data().before(
								sdf.parse(sdf.format(new Date())))) {
					publicacao.setPub_dataalteracao(sdf.parse(sdf
							.format(new Date())));
				}
			} catch (ParseException e) {
				Logger.getLogger(PublicacaoControlador.class.getName()).log(Level.SEVERE, null, e);
			}
			
			if (publicacao.getPub_img_nome() == null) {
				if (fImg != null)
					excluiArquivo();	
			} else {
				fImg = new File(Constantes.CAMINHO_IMG + "/" + nomeImgAtual);
				fImgThumb = new File(Constantes.CAMINHO_IMG_THUMB + "/" + nomeImgAtual);
				excluiArquivo();
				salvaEmDisco();
			}
			
			nomeImgAtual = null;
			publicacaoDao.atualizar(publicacao);
			FacesUtil.mensInfo("Atualizado com sucesso");
		}
		return "listarPublicacoes";
	}

	public String excluir() {
		try {
			if (publicacao.getPub_img_mime() != null)
				nomeImgAtual = publicacao.getPub_img_nome();
			removeImagem();
			excluiArquivo();
		} finally {
			publicacaoDao.excluir(publicacao);
		}
		FacesUtil.mensInfo("Excluído com sucesso");
		return "listarPublicacoes";
	}

	public String cancela() {
		bytesImg = null;
		mimeArq = null;
		nomeImg = null;
		nomeImgAtual = null;
		fImg = null;
		fImgThumb = null;

		return "listarPublicacoes";
	}

	void salvaEmDisco() {
		if (nomeImg != null) 
			CriaArquivo.criaArq(bytesImg, nomeImg, 1);
	}

	void exibeSemImg() {
		this.imagem = Imagem.exibeImagem(null, -1, null);
		setNomeImgSelecionada(null);
	}

	void excluiArquivo() { 
		if (fImg.exists())
			fImg.delete();
		if (fImgThumb.exists())
			fImgThumb.delete();
	}

	public void removeImagem() {
		nomeImgAtual = publicacao.getPub_img_nome();
		publicacao.setPub_img_nome(null);
		publicacao.setPub_img_descricao(null);
		publicacao.setPub_img_mime(null);
		fImg = new File(Constantes.CAMINHO_IMG + "/" + nomeImgAtual);
		fImgThumb = new File(Constantes.CAMINHO_IMG_THUMB + "/" + nomeImgAtual);
		exibeSemImg();
	}

	public void handleFileUpload(FileUploadEvent event) {
		try {
			
			if (publicacao.getPub_img_mime() != null) { 
				nomeImgAtual = publicacao.getPub_img_nome();
			}
			
			nomeImg = event.getFile().getFileName();
			
			mimeArq = new MimetypesFileTypeMap().getContentType(new File(nomeImg));
			String extensaoImg = mimeArq.substring(mimeArq.lastIndexOf('/') + 1);
			if (!"png".equals(extensaoImg)) 
				mimeArq = "image/png";

			bytesImg = Redimensiona.redimensionaImg(event.getFile().getContents(),300);
			
			imagem = new DefaultStreamedContent(new ByteArrayInputStream(bytesImg));
			setNomeImgSelecionada("<strong>Imagem visualizada:</strong> "
					+ nomeImg
					+ " "
					+ FormataBytes.formatInMegaBytes(event.getFile().getSize()));

			nomeImg = UUID.randomUUID().toString() + ".png";
			publicacao.setPub_img_mime(mimeArq);
			publicacao.setPub_img_nome(nomeImg);

		} catch (Exception ex) { 
			Logger.getLogger(PublicacaoControlador.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public Map<EnumStatus, String> getStatus() {
		Map<EnumStatus, String> mapParam = new HashMap<EnumStatus, String>();
		for (EnumStatus type : EnumStatus.values()) {
			mapParam.put(type, type.name());
		}
		return mapParam;
	}

}
