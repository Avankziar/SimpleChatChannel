package main.java.me.avankziar.scc.general.objects;

import java.util.Iterator;
import java.util.LinkedList;

import org.jetbrains.annotations.NotNull;

public class BoundedList<T> implements Iterable<T> 
{
	  private final LinkedList<T> list;
	  private final int capacity;
	  
	  public BoundedList(int capacity) 
	  {
	    this.list = new LinkedList<T>();
	    this.capacity = capacity;
	  }
	  
	  public void add(T element) 
	  {
	    if (this.list.size() == this.capacity)
	      this.list.removeFirst(); 
	    this.list.addLast(element);
	  }
	  
	  public T get(int index) 
	  {
	    return this.list.get(index);
	  }
	  
	  public int size() 
	  {
	    return this.list.size();
	  }
	  
	  @NotNull
	  public Iterator<T> iterator() 
	  {
	    return this.list.iterator();
	  }
}