package br.com.henriko.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T> é o tipo
 * @param key é a chave, string do texto que vai ser decomposta ao decorrer do tempo
 * @param real é pra definir se ele é uma chave (falso) ou se é um valor de verdade
 * @param value é o valor em si, por exemplo a chave AA e o valor é A, logo, vai ser AAA
 * @param linhas é a ordem em que as linhas aparecem
 * 
 */
class RadixTreeNode<T> {
	private String key;
	private List<RadixTreeNode<T>> filho;
	private boolean real;
	private T value;
	private List<Integer> linhas;

	/**
	 * Inicializa com os valores padrões pra não ficar checando por nulo toda hora
	 * 
	 * @param key    = String de texto
	 * @param filho  = Nó filho
	 * @param real   = Nó vazio ? sim:não
	 * @param linhas = linhas em que a palavra aparece, caso o nó seja real
	 */

	public RadixTreeNode() {
		key = "";
		filho = new ArrayList<RadixTreeNode<T>>();
		real = false;
		linhas = new ArrayList<Integer>();
	}

	public T getValue() {
		return value;
	}

	public void setValue(T data) {
		this.value = data;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String value) {
		this.key = value;
	}

	public boolean isReal() {
		return real;
	}

	public void setReal(boolean datanode) {
		this.real = datanode;
	}

	public List<RadixTreeNode<T>> getFilho() {
		return filho;
	}

	public void setFilho(List<RadixTreeNode<T>> filho) {
		this.filho = filho;
	}

	/**
	 * @param key chave, deer, oq vc queria que fosse?
	 * @return valor de caracteres iguais partindo do começo
	 */
	public int getCharsIguais(String key) {
		int charsIguais = 0;
		while (charsIguais < key.length() && charsIguais < this.getKey().length()) {
			if (key.charAt(charsIguais) != this.getKey().charAt(charsIguais)) {
				break;
			}
			charsIguais++;
		}
		return charsIguais;
	}

	public List<Integer> getLinhas() {
		return linhas;
	}

	public void setLinhas(List<Integer> linha) {
		this.linhas = linha;
	}

	public void addLinha(int linha) {
		this.linhas.add(linha);
	}
}
