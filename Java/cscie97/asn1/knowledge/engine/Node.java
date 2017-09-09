package cscie97.asn1.knowledge.engine;


/**
 * Domain object used to represent subjects and objects in a Triple
 *
 * @see Triple
 *
 * @author Jeremy Clark
 *
 */
public class Node {

    private final String identifier;
    private final long createDate;

    public Node(String identifier) {
        this.identifier = identifier;
        this.createDate = System.currentTimeMillis() / 1000L;;
    }

    public String getIdentifier() {
        return identifier;
    }

    public long getCreateDate() {
        return createDate;
    }


}
