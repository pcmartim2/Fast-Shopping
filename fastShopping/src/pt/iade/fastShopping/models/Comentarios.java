package pt.iade.fastShopping.models;

public class Comentarios {

	private String nomeUtilizador;
	private String comentario;
	
	public Comentarios(String nomeUtilizador, String comentario) {
		super();
		this.nomeUtilizador = nomeUtilizador;
		this.comentario = comentario;
	}

	public String getNomeUtilizador() {
		return nomeUtilizador;
	}

	public String getComentario() {
		return comentario;
	}
	
	
	
}
