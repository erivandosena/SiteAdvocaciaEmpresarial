package br.net.rwd.enumeracao;

public enum EnumPerfil {
	A("Administrador"), U("Usuário");

	private String perfil;
	
	EnumPerfil(String perfil) {
		this.perfil = perfil;
	}
	
	@Override
	public String toString() {
		return perfil;
	}

	public String getPerfil() {
		return perfil;
	}
	
}
