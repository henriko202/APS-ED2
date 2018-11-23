package br.com.henriko.classes;

import br.com.henriko.classes.RadixTreeNode;

/**
 * Implementa��o do {@link visitor}.
 * Sinceramente, ainda n�o entendi direito o que t� acontecendo aqui, mas o J�o ajudou pra caralho
 * ao implementar 90% disso, vlw bro sz
 * 
 * @param <T,R>, onde T � o tipo, e R � o resultado
 */
public abstract class VisitorImpl<T, R> implements Visitor<T, R> {

	protected R result;

	public VisitorImpl() {
		this.result = null;
	}

	public VisitorImpl(R initialValue) {
		this.result = initialValue;
	}

	public R getResult() {
		return result;
	}

	abstract public void visit(String key, RadixTreeNode<T> parent, RadixTreeNode<T> node);
}