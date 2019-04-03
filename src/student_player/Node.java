package student_player;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Node<A> {
	private A value;
    private List<Node<A>> children;
    private Supplier<List<Node<A>>> childrenSupplier;
    
    public Node(A value, Supplier<List<Node<A>>> childrenSupplier) {
    	this.value = value;
    	this.childrenSupplier = childrenSupplier;
    }
    
    public A getValue() {
    	return value;
    }
    
    public List<Node<A>> getChildren() {
    	if (children == null) {
    		children = childrenSupplier.get();
    	}
    	return children;
    }
    
    public <B> Node<B> map(Function<A, B> func) {
    	return new Node<B>(func.apply(value), () -> getChildren()
    			.stream()
    			.map(node -> node.map(func))
    			.collect(Collectors.toList()));
    }

    public <B> B reduce(BiFunction<A, List<B>, B> func) {
    	return func.apply(value, getChildren().stream().map(node -> node.reduce(func)).collect(Collectors.toList()));
    }
}
