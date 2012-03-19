package br.net.rwd.enumeracao;

public enum EnumPerfil {
	A("Administrador"), U("Usu�rio");

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
