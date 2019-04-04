package student_player;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * A generic node class to represent tree data structures.
 *
 * @param <A>
 */
public class Node<A> {
	public static <A, B> Node<B> expand(Function<A, Pair<B, List<A>>> func, A a) {
		Pair<B, List<A>> pair = func.apply(a);
		Supplier<List<Node<B>>> children = () -> pair.right.stream().map((a2) -> Node.expand(func, a2))
				.collect(Collectors.toList());
		return new Node<B>(pair.left, children);
	}
	
	private A value;
	private List<Node<A>> children;
	private Supplier<List<Node<A>>> childrenSupplier;

	public Node(A value, Supplier<List<Node<A>>> childrenSupplier) {
		this.value = value;
		this.childrenSupplier = childrenSupplier;
	}

	public List<Node<A>> getChildren() {
		if (children == null) {
			children = childrenSupplier.get();
		}
		return children;
	}

	public A getValue() {
		return value;
	}

	public <B> Node<B> map(Function<A, B> func) {
		return new Node<B>(func.apply(value),
				() -> getChildren().stream().map(node -> node.map(func)).collect(Collectors.toList()));
	}

	public <B> B reduce(BiFunction<A, List<B>, B> func) {
		return func.apply(value, getChildren().stream().map(node -> node.reduce(func)).collect(Collectors.toList()));
	}

	public String toString() {
		return this.reduce((value, folds) -> {
			String str = "";
			for (String fold : folds) {
				str += (str.length() == 0 ? "" : ", ") + fold;
			}
			str = str.length() != 0 ? String.format(", children: {%s}", str) : str;
			return String.format("Node(%s%s)", value.toString(), str);
		});
	}

	public Node<A> truncateAtDepth(int n) {
		Supplier<List<Node<A>>> supplier = () -> n <= 1 ? Arrays.asList()
				: getChildren().stream().map(child -> child.truncateAtDepth(n - 1)).collect(Collectors.toList());
		return new Node<>(value, supplier);
	}
}

class Pair<A, B> {
	public A left;
	public B right;

	Pair(A left, B right) {
		this.left = left;
		this.right = right;
	}
}
