package br.net.rwd.enumeracao;

public enum EnumPosicaoMenu {
	T("Topo"), R("Rodapé"), D("Destaque");

	/*
	 * private final String label;

	 * private PosicaoPagina(String label) { this.label = label; }

	 * public String getLabel() { return this.label; }
	 */

	private String posicao;

	EnumPosicaoMenu(String posicao) {
		this.posicao = posicao;
	}

	public String getPosicaoPagina() {
		return posicao;
	}

	@Override
	public String toString() {
		return posicao;
	}

}
