package br.com.henriko.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T> � o tipo
 * @param key � a chave, string do texto que vai ser decomposta ao decorrer do tempo
 * @param real � pra definir se ele � uma chave (falso) ou se � um valor de verdade
 * @param value � o valor em si, por exemplo a chave AA e o valor � A, logo, vai ser AAA
 * @param linhas � a ordem em que as linhas aparecem
 * 
 */
class RadixTreeNode<T> {
	private String key;
	private List<RadixTreeNode<T>> filho;
	private boolean real;
	private T value;
	private List<Integer> linhas;

	/**
	 * Inicializa com os valores padr�es pra n�o ficar checando por nulo toda hora
	 * 
	 * @param key    = String de texto
	 * @param filho  = N� filho
	 * @param real   = N� vazio ? sim:n�o
	 * @param linhas = linhas em que a palavra aparece, caso o n� seja real
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
	 * @return valor de caracteres iguais partindo do come�o
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
