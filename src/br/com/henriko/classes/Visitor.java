package br.com.henriko.classes;

import br.com.henriko.classes.RadixTreeNode;

/**
 * Usado por {@link RadixTree} para fazer algo no n� achado, no caso, remover (valeu J�o <3)
 * 
 * @param <T,R>, onde T � o tipo e R � o resultado
 */
public interface Visitor<T, R> {
	/**
	 * Acha o n� que tem a key igual a passada por par�metro
	 * 
	 * @param key    A chave do seu cora��o
	 * @param parent O pai do n�, aquele lindo HUEAHUEAHU
	 * @param node   O n� que t� sendo visitado pra um ch� da tarde
	 */
	public void visit(String key, RadixTreeNode<T> parent, RadixTreeNode<T> node);

	/**
	 * Visitor guarda o valor achado
	 * 
	 * @return O resultado, queria o que? um presente?
	 * (mas se bem que o resultado � um presente do J�o que me ajudou nessa)
	 */
	public R getResult();
}
