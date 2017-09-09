package cscie97.asn1.knowledge.engine;

import java.util.Date;


/**
 * Domain object used to represent predicates in a Triple
 *
 * @see Triple
 *
 * @author Jeremy Clark
 *
 */
public class Predicate {

    private final String identifier;
    private final long createDate;

    public Predicate(String identifier) {
        this.identifier = identifier;
        this.createDate = System.currentTimeMillis() / 1000L;
    }

    public String getIdentifier() {
        return identifier;
    }

    public long getCreateDate() {
        return createDate;
    }


}
