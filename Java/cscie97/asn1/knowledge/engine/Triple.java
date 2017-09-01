package cscie97.asn1.knowledge.engine;

public class Triple {

    private final Node sub;
    private final Predicate predicate;
    private final Node obj;
    private final long createDate;

    public Triple(Node sub, Predicate predicate, Node obj) {
        this.sub = sub;
        this.predicate = predicate;
        this.obj = obj;
        this.createDate = System.currentTimeMillis() / 1000L;
    }

    public String getIdentifier(){
        String subId = this.sub.getIdentifier();
        String predicateId = this.predicate.getIdentifier();
        String objId = this.obj.getIdentifier();
        return subId + " " + predicateId + " " + objId;
    }

    public long getCreateDate(){
        return createDate;
    }

}
