package br.net.rwd.enumeracao;

public enum EnumEstado {
	AC("Acre"),
	AL("Alagoas"),
	AP("Amapá"),
	AM("Amazonas"),
	BA("Bahia"),
	CE("Ceará"),
	DF("Distrito Federal"),
	GO("Goiás"),
	ES("Espírito Santo"),
	MA("Maranhão"),
	MT("Mato Grosso"),
	MS("Mato Grosso do Sul"),
	MG("Minas Gerais"),
	PA("Pará"),
	PB("Paraiba"),
	PR("Paraná"),
	PE("Pernambuco"),
	PI("Piauí"),
	RJ("Rio de Janeiro"),
	RN("Rio Grande do Norte"),
	RS("Rio Grande do Sul"),
	RO("Rondônia"),
	RR("Roraima"),
	SP("São Paulo"),
	SC("Santa Catarina"),
	SE("Sergipe"),
	TO("Tocantins");
	
	private String estado;

	EnumEstado(String estado) {
		this.estado = estado;
	}

	public String getEnumEstados() {
		return estado;
	}

	@Override
	public String toString() {
		return estado;
	}
}
