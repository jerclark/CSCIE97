package cscie97.asn1.knowledge.engine;

import java.util.Date;

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
