package pt.iade.fastShopping.models;

public class Favoritos {
	
	private int idLoja;
	private String nomeLoja;
	
	public Favoritos(int idLoja, String nomeLoja) {
		super();
		this.idLoja = idLoja;
		this.nomeLoja = nomeLoja;
	}

	public int getIdLoja() {
		return idLoja;
	}

	public String getNomeLoja() {
		return nomeLoja;
	}
	
	

}
