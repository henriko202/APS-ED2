package br.com.henriko.classes;

import br.com.henriko.classes.RadixTreeNode;

/**
 * Usado por {@link RadixTree} para fazer algo no nó achado, no caso, remover (valeu Jão <3)
 * 
 * @param <T,R>, onde T é o tipo e R é o resultado
 */
public interface Visitor<T, R> {
	/**
	 * Acha o nó que tem a key igual a passada por parâmetro
	 * 
	 * @param key    A chave do seu coração
	 * @param parent O pai do nó, aquele lindo HUEAHUEAHU
	 * @param node   O nó que tá sendo visitado pra um chá da tarde
	 */
	public void visit(String key, RadixTreeNode<T> parent, RadixTreeNode<T> node);

	/**
	 * Visitor guarda o valor achado
	 * 
	 * @return O resultado, queria o que? um presente?
	 * (mas se bem que o resultado é um presente do Jão que me ajudou nessa)
	 */
	public R getResult();
}
