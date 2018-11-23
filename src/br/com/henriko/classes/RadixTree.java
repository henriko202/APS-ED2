package br.com.henriko.classes;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;

import br.com.henriko.classes.RadixTreeNode;

/**
 *
 * @param <T> é o Tipo declarado, diferente do C não se usa #define, mas sim o
 *        operador diamante, TÁ VENDO C? É ASSIM QUE SE FAZ, E NÃO COM AS
 *        GAMBIARRAS QUE VOCÊ USA, SEU CRETINO!
 */
public class RadixTree<T> {

	protected RadixTreeNode<T> root;

	protected long size;

	/**
	 * Cria a árvore com valores padrões
	 */
	public RadixTree() {
		root = new RadixTreeNode<T>();
		root.setKey("");
		size = 0;
	}

	/**
	 * Hmmm, não sei o que esse método faz, to com uma dúvida.... JÁ SEI, ACHO QUE
	 * ELE MEDE A ÁRVORE!! nao nao... ele deleta, foi mal...<br>
	 * Agradeço novamente ao Jão que me ajudou nisso, não ia saber utilizar todo o
	 * potencial do {@link Visitor}
	 * 
	 * @param key chave para ser deletada
	 * @return
	 */
	public boolean delete(String key) {
		Visitor<T, Boolean> visitor = new VisitorImpl<T, Boolean>(Boolean.FALSE) {
			public void visit(String key, RadixTreeNode<T> pai, RadixTreeNode<T> node) {

				if (node.isReal()) {

//					 se não tem filhos, deleta o nó
					if (node.getFilho().size() == 0) {
						Iterator<RadixTreeNode<T>> it = pai.getFilho().iterator();
						while (it.hasNext()) {
							if (it.next().getKey().equals(node.getKey())) {
								it.remove();
								break;
							}
						}

//						Se o pai não é real e tem só um filho, merge eles
						if (pai.getFilho().size() == 1 && pai.isReal() == false) {
							mergeNodes(pai, pai.getFilho().get(0));
						}
					} else if (node.getFilho().size() == 1) {

//						precisa dar merge no unico filho do nó com eles mesmo
						mergeNodes(node, node.getFilho().get(0));
					} else {
//						só marca coimo não real
						node.setReal(false);
					}
				}
			}

			/**
			 * Dá um merge do filho no pai, só se for o unico filho e o pai não for real
			 * Como o merge só é usado na deleção, foi declarado aqui
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
	 * Método usado para inserir, que vai chamar outro método, linkando com a raiz
	 * 
	 * @param key   chave, é utilizado o mesmo valor do value, pois na inserção ele
	 *              é quebrado
	 * @param value valor
	 * @param line  linha em que ele aparece
	 */
	public void insert(String key, T value, int line) {
		insert(key, root, value, line);
		size++;
	}

	/**
	 * Recursão pra inserir
	 * 
	 * @param key   A chave a ser inserida
	 * @param node  O nó atual
	 * @param value O valor do nó (string)
	 */
	private void insert(String key, RadixTreeNode<T> node, T value, int line) {

//		charsIguais conta a quantidade de chars iguais do nó em comparação com a chave (duh)
//		para poder criar um nó novo, repartir, etc etc etc
		int charsIguais = node.getCharsIguais(key);

//		lógica: ou está na raíz, ou a gente precisa ir mais fundo
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

//			nó vai ser filho do nó anterior, simples
			if (flag == false) {
				RadixTreeNode<T> n = new RadixTreeNode<T>();
				n.setKey(newText);
				n.setReal(true);
				n.setValue(value);
				n.addLinha(line);

				node.getFilho().add(n);
			}
		}

//		Palavras iguais, só fazer o nó ser verdadeiro, gg izi meu fión (que linguajar escroto...)
		else if (charsIguais == key.length() && charsIguais == node.getKey().length()) {
			if (node.isReal() == true) {
				node.addLinha(line);
				return;
			}

			node.setReal(true);
			node.setValue(value);
			node.addLinha(line);
		}

//		Esse nó vai ser prefixo da palavra, splita isso plox
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

//		só adicionar como filho do nó atual
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
	 * Recursivamente acha o nó que tem a chave igual o prefixo
	 * 
	 * @param prefix  Prefixo, uai
	 * @param visitor Usado para recursão
	 * @param node    No usado para procurar mais a fundo
	 */
	private <R> void visit(String prefix, Visitor<T, R> visitor, RadixTreeNode<T> pai, RadixTreeNode<T> node) {

		int charsIguais = node.getCharsIguais(prefix);

//		se forem iguais, é factível chegamos no lugar, trivial isso
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
	 * Imprime na tela e coloca no arquivo out.txt (excluído logo em seguida para
	 * poder ordenar) todos os valores da árvore e as linhas que aparecem
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
	 * Escreve no arquivo out.txt (pois é o Formatter f) os valores e as linhas que
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
	 * valor real, ou seja, não chave
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