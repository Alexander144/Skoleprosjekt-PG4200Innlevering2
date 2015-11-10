
import edu.princeton.cs.algs4.Queue;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class LinkManager {


	private Queue<String> queue;
	private Stack<String> stack;
	private HashSet<String> links;
	private boolean breadthFirst;
	
	public LinkManager() {
		this(false);
	}
	
	public LinkManager(boolean breadthFirst) {
		
		this.breadthFirst = breadthFirst;
		links = new HashSet<String>();
		if (this.breadthFirst) {
			queue = new Queue<String>();
		} else {
			stack = new Stack<String>();
		}
	}
	
	public void addLink(String link) {
		links.add(link);
	}
	
	public void addLinks(Set<String> links) {

		
		for (String link : links) {
			if ((this.links.add(link))) {
				if (this.breadthFirst) {
					queue.enqueue(link);
				} else {
					stack.push(link);
				}
			}
		}
	}
	
	public String nextLink() {
		if (this.breadthFirst) {
			return queue.dequeue();
		} else {
			return stack.pop();
		}
	}
}
