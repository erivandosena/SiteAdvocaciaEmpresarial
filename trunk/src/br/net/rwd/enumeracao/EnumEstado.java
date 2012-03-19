package br.net.rwd.enumeracao;

public enum EnumEstado {
	AC("Acre"),
	AL("Alagoas"),
	AP("Amap�"),
	AM("Amazonas"),
	BA("Bahia"),
	CE("Cear�"),
	DF("Distrito Federal"),
	GO("Goi�s"),
	ES("Esp�rito Santo"),
	MA("Maranh�o"),
	MT("Mato Grosso"),
	MS("Mato Grosso do Sul"),
	MG("Minas Gerais"),
	PA("Par�"),
	PB("Paraiba"),
	PR("Paran�"),
	PE("Pernambuco"),
	PI("Piau�"),
	RJ("Rio de Janeiro"),
	RN("Rio Grande do Norte"),
	RS("Rio Grande do Sul"),
	RO("Rond�nia"),
	RR("Roraima"),
	SP("S�o Paulo"),
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
