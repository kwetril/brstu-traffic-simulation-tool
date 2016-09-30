package by.brstu.tst.core.map.elements;

/**
 * Created by kwetril on 8/16/16.
 */
public abstract class BaseRoadElement {
    protected String name;

    public BaseRoadElement(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void accept(BaseRoadElementVisitor visitor) {
        visitor.visit(this);
    }
}
