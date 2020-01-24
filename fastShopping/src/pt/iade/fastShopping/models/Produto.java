package pt.iade.fastShopping.models;


public class Produto {
	
	private int idProduto;
	private byte[] imagemProduto;
	private String nomeProduto;
	private String categoria;
	private double precoProduto;
	
	public Produto(int idProduto, byte[] imagemProduto, String nomeProduto, String categoria, double precoProduto) {
		this.idProduto = idProduto;
		this.imagemProduto = imagemProduto;
		this.nomeProduto = nomeProduto;
		this.categoria = categoria;
		this.precoProduto = precoProduto;
	}

	public int getIdProduto() {
		return idProduto;
	}

	public byte[] getImagemProduto() {
		return imagemProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public String getCategoria() {
		return categoria;
	}

	public double getPrecoProduto() {
		return precoProduto;
	}	
	
}
