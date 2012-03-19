package br.net.rwd.enumeracao;

public enum EnumStatus {
	A("Ativa"), I("Inativa");

	private String status;

	EnumStatus(String status) {this.status = status;}

	public String getStatus() {return status;}

	public String toString() {return status;}
}
