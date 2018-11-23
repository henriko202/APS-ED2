package br.com.henriko.classes;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;

import br.com.henriko.classes.RadixTreeNode;

/**
 *
 * @param <T> � o Tipo declarado, diferente do C n�o se usa #define, mas sim o
 *        operador diamante, T� VENDO C? � ASSIM QUE SE FAZ, E N�O COM AS
 *        GAMBIARRAS QUE VOC� USA, SEU CRETINO!
 */
public class RadixTree<T> {

	protected RadixTreeNode<T> root;

	protected long size;

	/**
	 * Cria a �rvore com valores padr�es
	 */
	public RadixTree() {
		root = new RadixTreeNode<T>();
		root.setKey("");
		size = 0;
	}

	/**
	 * Hmmm, n�o sei o que esse m�todo faz, to com uma d�vida.... J� SEI, ACHO QUE
	 * ELE MEDE A �RVORE!! nao nao... ele deleta, foi mal...<br>
	 * Agrade�o novamente ao J�o que me ajudou nisso, n�o ia saber utilizar todo o
	 * potencial do {@link Visitor}
	 * 
	 * @param key chave para ser deletada
	 * @return
	 */
	public boolean delete(String key) {
		Visitor<T, Boolean> visitor = new VisitorImpl<T, Boolean>(Boolean.FALSE) {
			public void visit(String key, RadixTreeNode<T> pai, RadixTreeNode<T> node) {

				if (node.isReal()) {

//					 se n�o tem filhos, deleta o n�
					if (node.getFilho().size() == 0) {
						Iterator<RadixTreeNode<T>> it = pai.getFilho().iterator();
						while (it.hasNext()) {
							if (it.next().getKey().equals(node.getKey())) {
								it.remove();
								break;
							}
						}

//						Se o pai n�o � real e tem s� um filho, merge eles
						if (pai.getFilho().size() == 1 && pai.isReal() == false) {
							mergeNodes(pai, pai.getFilho().get(0));
						}
					} else if (node.getFilho().size() == 1) {

//						precisa dar merge no unico filho do n� com eles mesmo
						mergeNodes(node, node.getFilho().get(0));
					} else {
//						s� marca coimo n�o real
						node.setReal(false);
					}
				}
			}

			/**
			 * D� um merge do filho no pai, s� se for o unico filho e o pai n�o for real
			 * Como o merge s� � usado na dele��o, foi declarado aqui
			 * 
			 * @param pai
			 * @param filho
			 */
			private void mergeNodes(RadixTreeNode<T> pai, RadixTreeNode<T> filho) {
				pai.setKey(pai.getKey() + filho.getKey());
				pai.setReal(filho.isReal());
				pai.setValue(filho.getValue());
				pai.setFilho(filho.getFilho());
			}

		};

		visit(key, visitor);

		if (visitor.getResult()) {
			size--;
		}
		return visitor.getResult().booleanValue();
	}

	/**
	 * M�todo usado para inserir, que vai chamar outro m�todo, linkando com a raiz
	 * 
	 * @param key   chave, � utilizado o mesmo valor do value, pois na inser��o ele
	 *              � quebrado
	 * @param value valor
	 * @param line  linha em que ele aparece
	 */
	public void insert(String key, T value, int line) {
		insert(key, root, value, line);
		size++;
	}

	/**
	 * Recurs�o pra inserir
	 * 
	 * @param key   A chave a ser inserida
	 * @param node  O n� atual
	 * @param value O valor do n� (string)
	 */
	private void insert(String key, RadixTreeNode<T> node, T value, int line) {

//		charsIguais conta a quantidade de chars iguais do n� em compara��o com a chave (duh)
//		para poder criar um n� novo, repartir, etc etc etc
		int charsIguais = node.getCharsIguais(key);

//		l�gica: ou est� na ra�z, ou a gente precisa ir mais fundo
		if (node.getKey().equals("") == true || charsIguais == 0
				|| (charsIguais < key.length() && charsIguais >= node.getKey().length())) {
			boolean flag = false;
			String newText = key.substring(charsIguais, key.length());
			for (RadixTreeNode<T> filho : node.getFilho()) {
				if (filho.getKey().startsWith(newText.charAt(0) + "")) {
					flag = true;
					insert(newText, filho, value, line);
					break;
				}
			}

//			n� vai ser filho do n� anterior, simples
			if (flag == false) {
				RadixTreeNode<T> n = new RadixTreeNode<T>();
				n.setKey(newText);
				n.setReal(true);
				n.setValue(value);
				n.addLinha(line);

				node.getFilho().add(n);
			}
		}

//		Palavras iguais, s� fazer o n� ser verdadeiro, gg izi meu fi�n (que linguajar escroto...)
		else if (charsIguais == key.length() && charsIguais == node.getKey().length()) {
			if (node.isReal() == true) {
				node.addLinha(line);
				return;
			}

			node.setReal(true);
			node.setValue(value);
			node.addLinha(line);
		}

//		Esse n� vai ser prefixo da palavra, splita isso plox
		else if (charsIguais > 0 && charsIguais < node.getKey().length()) {
			RadixTreeNode<T> n1 = new RadixTreeNode<T>();
			n1.setKey(node.getKey().substring(charsIguais, node.getKey().length()));
			n1.setReal(node.isReal());
			n1.setValue(node.getValue());
			n1.setFilho(node.getFilho());

			node.setKey(key.substring(0, charsIguais));
			node.setReal(false);
			node.setFilho(new ArrayList<RadixTreeNode<T>>());
			node.getFilho().add(n1);

			if (charsIguais < key.length()) {
				RadixTreeNode<T> n2 = new RadixTreeNode<T>();
				n2.setKey(key.substring(charsIguais, key.length()));
				n2.setReal(true);
				n2.setValue(value);
				n2.addLinha(line);
				for (int i = 0; i < node.getLinhas().size(); i++) {
					n1.addLinha(node.getLinhas().get(i));
				}

				node.getFilho().add(n2);
			} else {
				node.setValue(value);
				node.setReal(true);
				node.addLinha(line);
			}
		}

//		s� adicionar como filho do n� atual
		else {
			RadixTreeNode<T> n = new RadixTreeNode<T>();
			n.setKey(node.getKey().substring(charsIguais, node.getKey().length()));
			n.setFilho(node.getFilho());
			n.setReal(node.isReal());
			n.setValue(node.getValue());
			n.setLinhas(null);

			node.addLinha(line);
			node.setKey(key);
			node.setReal(true);
			node.setValue(value);

			node.getFilho().add(n);
		}
	}

	/**
	 * Visita o node que tem a key igual
	 * 
	 * @param key
	 * @param visitor
	 */
	public <R> void visit(String key, Visitor<T, R> visitor) {
		if (root != null) {
			visit(key, visitor, null, root);
		}
	}

	/**
	 * Recursivamente acha o n� que tem a chave igual o prefixo
	 * 
	 * @param prefix  Prefixo, uai
	 * @param visitor Usado para recurs�o
	 * @param node    No usado para procurar mais a fundo
	 */
	private <R> void visit(String prefix, Visitor<T, R> visitor, RadixTreeNode<T> pai, RadixTreeNode<T> node) {

		int charsIguais = node.getCharsIguais(prefix);

//		se forem iguais, � fact�vel chegamos no lugar, trivial isso
		if (charsIguais == prefix.length() && charsIguais == node.getKey().length()) {
			visitor.visit(prefix, pai, node);
		} else if (node.getKey().equals("") == true
				|| (charsIguais < prefix.length() && charsIguais >= node.getKey().length())) {
			String newText = prefix.substring(charsIguais, prefix.length());
			for (RadixTreeNode<T> filho : node.getFilho()) {
				if (filho.getKey().startsWith(newText.charAt(0) + "")) {
					visit(newText, visitor, node, filho);
					break;
				}
			}
		}
	}

	/**
	 * Imprime na tela e coloca no arquivo out.txt (exclu�do logo em seguida para
	 * poder ordenar) todos os valores da �rvore e as linhas que aparecem
	 */
	public void display() throws IOException {
		Formatter f = new Formatter(Paths.get(".").toAbsolutePath().normalize().toString()
				+ System.getProperty("file.separator") + "out.txt");
		formatNodeTo(f, root);
//		imprimir na ordem que foi lido
		formatNodeTo(new Formatter(System.out), 0, root);
		f.close();
	}

	/**
	 * Escreve no arquivo out.txt (pois � o Formatter f) os valores e as linhas que
	 * ele aparece
	 */
	private void formatNodeTo(Formatter f, RadixTreeNode<T> node) {
		if (node.isReal() == true)
			f.format("%s %s%n", node.getValue(), node.getLinhas());
		for (RadixTreeNode<T> filho : node.getFilho()) {
			formatNodeTo(f, filho);
		}
	}

	/**
	 * escreve na linha de forma organizada as chaves e os valores caso seja um
	 * valor real, ou seja, n�o chave
	 */
	private void formatNodeTo(Formatter f, int profundidade, RadixTreeNode<T> node) {
		for (int i = 0; i < profundidade; i++) {
			f.format(" ");
		}
		f.format("|");
		for (int i = 0; i < profundidade; i++) {
			f.format("-");
		}

		if (node.isReal() == true)
			f.format("%s %s %s*%n", node.getKey(), node.getValue(), node.getLinhas());
		else
			f.format("%s%n", node.getKey());

		for (RadixTreeNode<T> child : node.getFilho()) {
			formatNodeTo(f, profundidade + 1, child);
		}
	}

}