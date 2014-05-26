package hr.java.JML.neural;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class NeuralNetworkDataSet implements Set<File> {
	private HashSet<File> dataSet;
	
	public NeuralNetworkDataSet() {	
		dataSet = new HashSet<>();
	}

	@Override
	public boolean add(File e) {
		return dataSet.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends File> c) {
		return dataSet.addAll(c);
	}

	@Override
	public void clear() {
		dataSet.clear();		
	}

	@Override
	public boolean contains(Object o) {
		return dataSet.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return dataSet.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return dataSet.isEmpty();
	}

	@Override
	public Iterator<File> iterator() {
		return dataSet.iterator();
	}

	@Override
	public boolean remove(Object o) {
		return dataSet.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return dataSet.remove(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return dataSet.retainAll(c);
	}

	@Override
	public int size() {
		return dataSet.size();
	}

	@Override
	public Object[] toArray() {
		return dataSet.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return dataSet.toArray(a);
	}

	
}
