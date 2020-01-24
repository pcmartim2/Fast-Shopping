package pt.iade.fastShopping.models;

public class Loja {

	private int idLoja;
	private byte[] imagemLoja;
	private String nomeLoja;
	private String coordenadas;
	private String proprietario;
	private String estilo;

	public Loja(int idLoja, byte[] imagemLoja, String nomeLoja, String coordenadas, String proprietario, String estilo) {

		this.idLoja = idLoja;
		this.imagemLoja = imagemLoja;
		this.nomeLoja = nomeLoja;
		this.coordenadas = coordenadas;
		this.proprietario = proprietario;
		this.estilo = estilo;

	}

	public int getIdLoja() {
		return idLoja;
	}
	
	public byte[] getImagemLoja() {
		return imagemLoja;
	}

	public String getCoordenadas() {
		return coordenadas;
	}

	public String getProprietario() {
		return proprietario;
	}

	public String getEstilo() {
		return estilo;
	}

	public String getNomeLoja() {
		return nomeLoja;
	}
	
}
