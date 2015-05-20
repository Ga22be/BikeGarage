package bikeGarageDatabase;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<E> implements Serializable {
	private Node<E> root;
	private Node<E> last;
	private int size;
	
	public LinkedList() {
		root = null;
		last = null;
		size = 0;
	}

	public int size() {
		return size;
	}
	
	public boolean add(E x) {
		if(root == null) {
			root = new Node<E>(x);
			last = root;
			size++;
			return true;
		}
		else {
			if(contains(x)) {
				return false;
			}
			else {
				Node<E> temp = new Node<E>(x);
				last.next = temp;
				last = temp;
				size++;
				return true;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public E remove(E x) {
		E ret = null;
		if(x.equals(root.element)) {
			if (root.element.equals(last.element)) {
				last = null;
			}
			ret = root.element;
			root = root.next;
			size = size - 1;
			return ret;
		}
		Node<E> temp = root;
		while(temp != null) {
			if(x.equals(temp.next.element)) {
				if (temp.next.element.equals(last.element)) {
					last = temp;
				}
				ret = (E) temp.next.element;
				temp.next = temp.next.next;
				size = size - 1;
				return ret;
			}
			temp = temp.next;
		}
		return null;
	}
	
	public boolean contains(E x) {
		Node<E> temp = root;
		while(temp != null) {
			if(temp.element.equals(x)) {
				return true;
			}
			temp = temp.next;
		}
		return false;
	}
	
	public Iterator<E> iterator() {
		return new ListIterator();
	}
	
	private class ListIterator implements Iterator<E> {
		private Node<E> pos;
		
		private ListIterator() {
			pos  = root;
		}
		
		@Override
		public boolean hasNext() {
			if (pos != null) {
				return true;
			}
			return false;
		}
		
		@Override
		public E next() {
			Node<E> temp = pos;
			if (pos != null) {
				pos = pos.next;
				return temp.element;
			}
			throw new NoSuchElementException();
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private static class Node<E> implements Serializable {
		private Node next;
		private E element;
		
		private Node(E element) {
			this.element = element;
			next = null;
		}
	}
}
