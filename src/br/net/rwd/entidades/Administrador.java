package br.net.rwd.entidades;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "usuarios")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Administrador implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long usu_cod;
	private String usu_email;
	private String usu_senha;
	private String usu_nome;
	private String usu_perfil;

	public Long getUsu_cod() {
		return usu_cod;
	}

	public void setUsu_cod(Long usu_cod) {
		this.usu_cod = usu_cod;
	}

	public String getUsu_email() {
		return usu_email;
	}

	public void setUsu_email(String usu_email) {
		this.usu_email = usu_email;
	}

	public String getUsu_senha() {
		return usu_senha;
	}

	public void setUsu_senha(String usu_senha) {
		this.usu_senha = usu_senha;
	}

	public String getUsu_nome() {
		return usu_nome;
	}

	public void setUsu_nome(String usu_nome) {
		this.usu_nome = usu_nome;
	}

	public String getUsu_perfil() {
		return usu_perfil;
	}

	public void setUsu_perfil(String usu_perfil) {
		this.usu_perfil = usu_perfil;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((usu_cod == null) ? 0 : usu_cod.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Administrador other = (Administrador) obj;
		if (usu_cod == null) {
			if (other.usu_cod != null)
				return false;
		} else if (!usu_cod.equals(other.usu_cod))
			return false;
		return true;
	}
	
}
