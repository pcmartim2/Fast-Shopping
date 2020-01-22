package pt.iade.fastShopping.models;

public class Loja {

	private int idLoja;
	private byte[] imagemLoja;
	private String nomeLoja;
	private Coordenadas coordenadas;
	private String proprietario;
	private String estilo;

	public Loja(int idLoja, byte[] imagemLoja, String nomeLoja, Coordenadas coordenadas, String proprietario, String estilo) {

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

	public Coordenadas getCoordenadas() {
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
