package br.net.rwd.controlador;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Controller;

import br.net.rwd.dao.DaoGenerico;
import br.net.rwd.entidades.Website;
import br.net.rwd.util.Constantes;
import br.net.rwd.util.FacesUtil;

@Controller("contatoControlador")
@Scope("request")
public class ContatoControlador {
	private static String nome;
	private String email;
	private String assunto;
	private static String mensagem;
	
	private static String imagem;

	@Resource
	private JavaMailSender enviarEmail;
	
	@Resource
	private DaoGenerico<Website, Integer> websiteDao;
	
	public ContatoControlador() {

	}

	public DaoGenerico<Website, Integer> getWebsiteDao() {
		return websiteDao;
	}

	public void setWebsiteDao(DaoGenerico<Website, Integer> websiteDao) {
		this.websiteDao = websiteDao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		ContatoControlador.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		ContatoControlador.mensagem = mensagem;
	}
	
	public String enviar() {
		
		final String EMAIL_SITE = ((Website)getDados().getRowData()).getWeb_email();

		File img = new File(Constantes.CAMINHO_IMG+"/imagem.png");
		if (img.exists())
			imagem  = "/ckfinder/userfiles/images/imagem.png";
		else
			imagem  = "/resources/img/sem_imagem.gif";
		
		if (assunto.equals("")) assunto = "Contato sem assunto especificado";

		if (nome != null && !nome.equals("") && email != null
				&& !email.equals("") && mensagem != null
				&& !mensagem.equals("")) {

			// envia e-mail de contato
			MimeMessagePreparator preparator = new MimeMessagePreparator() {

				public void prepare(MimeMessage mimeMessage) throws Exception {

					MimeMessageHelper MMhelper = new MimeMessageHelper(mimeMessage, true, "ISO-8859-1");
					MMhelper.setFrom(new InternetAddress(email,nome));
					MMhelper.setTo(EMAIL_SITE);
					MMhelper.setBcc(email);
					MMhelper.setPriority(Thread.MAX_PRIORITY);
					MMhelper.setSentDate(new Date());
					MMhelper.setSubject(assunto);
					setHTMLContent(MMhelper);
					//MMhelper.setText("", true);
				}
			};
			try {
				this.enviarEmail.send(preparator);
				nome = "";
				email = "";
				assunto = "";
				mensagem = "";
				FacesUtil.mensInfo("Enviado com sucesso!");
				return "contato";
			} catch (MailException ex) {
				FacesUtil.mensInfo("Erro ao enviar.");
			}

		} else {

			FacesUtil.mensInfo("Campos com * são obrigatórios.");
		}

		return null;

	}
	
	public static void setHTMLContent(MimeMessageHelper msg) throws MessagingException {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		String html = "<html><head><title>" + msg.getMimeMessage().getSubject() +"</title></head><body lang=PT-BR link=blue vlink=purple style='tab-interval: 35.4pt'>" +
                        "<div align=center>" +
                        "<table border=0 cellspacing=0 cellpadding=0 width=550 style='width: 412.5pt; mso-cellspacing: 0cm; mso-yfti-tbllook: 1184; mso-padding-alt: 0cm 0cm 0cm 0cm'>" +
                        "<tr style='mso-yfti-irow: 0; mso-yfti-firstrow: yes'>" +
                        "<td width=175 style='width: 131.25pt; padding: 0cm 0cm 0cm 0cm'>" +
                        "<p><span style='font-family: Arial'><a href='"+request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath())+"'><span style='text-decoration: none; text-underline: none'>" +
                        "<img border=0 height=80 src='"+request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath())+imagem+"' alt='' /></span></a>" +
                        "</span></p></td><td width=372 style='width: 279.0pt; padding: 0cm 0cm 0cm 0cm'></td></tr>" +
                        "<tr style='mso-yfti-irow: 1'><td style='padding: 0cm 0cm 0cm 0cm'></td><td style='padding: 0cm 0cm 0cm 0cm'></td><td style='padding: 0cm 0cm 0cm 0cm'></td>" +
                        "</tr><tr style='mso-yfti-irow: 2'>" +
                        "<td colspan=3 style='text-align:left; border: solid #C1C1C1 1.0pt; mso-border-alt: solid #C1C1C1 .75pt; mso-border-bottom-alt: solid #C1C1C1 .25pt; padding: 15.0pt 15.0pt 7.5pt 15.0pt'>" +
                        "<p style='line-height: 13.5pt'><span style='font-size: 9.0pt; font-family: Arial; color: #333333'>Olá! <br />Meu nome é <strong>"+nome.toUpperCase()+"</strong></span></p>" +
                        "<p style='line-height: 13.5pt'><span style='font-size: 9.0pt; font-family: Arial; color: #333333'>"+mensagem+"</span></p>" +
                        "<p style='line-height: 13.5pt'><span style='font-size: 8.0pt; font-family: Arial; color: #333333'><i>Mensagem originada do site, o usuário "+nome+" foi registrado com o IP Nº: "+request.getRemoteHost()+"</i></span>" +
                        "</p></td></tr><tr bgcolor='#FEF1C7' style='mso-yfti-irow: 3; height: 6.75pt'>" +
                        "<td colspan=3 style='height: 5px; border: solid #C1C1C1 1.0pt; mso-border-alt: solid #C1C1C1 .75pt; mso-border-top-alt: solid #C1C1C1 .25pt; padding: 0cm 0cm 0cm 0cm; height: 6.75pt'>" +
                        "<p><span style='font-size: 1.0pt; font-family: Arial'>" +
                        "</span></p></td></tr><tr style='mso-yfti-irow: 4; mso-yfti-lastrow: yes'>" +
                        "<td colspan=3 style='padding: 11.25pt 0cm 0cm 0cm'><p align=center style='text-align: center'>" +
                        "<span style='font-size: 8.5pt; font-family: Arial; color: #666666'>" +
                        "© Produzido por <a href='http://www.rwd.net.br/'>RWD</a></span></p></td></tr>" +
                        "</table></div></body></html>";
        // HTMLDataSource é uma classe interna
        msg.getMimeMessage().setDataHandler(new DataHandler(new HTMLDataSource(html)));
    }
	
	static class HTMLDataSource implements DataSource {
        private String html;

        public HTMLDataSource(String htmlString) {
            html = htmlString;
        }

        // String html de retorno em um InputStream.
        // Um novo fluxo deve ser devolvido a cada vez.
        public InputStream getInputStream() throws IOException {
            if (html == null) throw new IOException("HTML nulo");
            return new ByteArrayInputStream(html.getBytes());
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Este DataHandler não pode escrever HTML");
        }

        public String getContentType() {
            return "text/html";
        }

        public String getName() {
            return "JAF text/html dataSource apenas para enviar e-mail";
        }
    }
	
	public DataModel<Website> getDados() {
		String consulta = "SELECT w FROM Website w";
		return new ListDataModel<Website>(websiteDao.listPesq(consulta));
	}

}
