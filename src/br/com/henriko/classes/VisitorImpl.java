package br.com.henriko.classes;

import br.com.henriko.classes.RadixTreeNode;

/**
 * Implementação do {@link visitor}.
 * Sinceramente, ainda não entendi direito o que tá acontecendo aqui, mas o Jão ajudou pra caralho
 * ao implementar 90% disso, vlw bro sz
 * 
 * @param <T,R>, onde T é o tipo, e R é o resultado
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