import java.util.*;
import java.util.function.Consumer;

public class OtherPosition {

    private int position;
    private int money=0;
    private int type;
    private String name;

    public OtherPosition(PropertyBuilder propertyBuilder) {
        this.position = propertyBuilder.position;
        this.type = propertyBuilder.type;
        this.name = propertyBuilder.name;
        this.money = propertyBuilder.money;
    }
    public static class PropertyBuilder {
        int position;
        int money=0;
        int type;
        String name;

        public PropertyBuilder with(
                Consumer<PropertyBuilder> builderFunction) {
            builderFunction.accept(this);
            return this;
        }

        public OtherPosition createBuilding() {
            return new OtherPosition(this);
        }
    }
    LinkedHashMap info = new LinkedHashMap();
    public LinkedHashMap getInfo(){
        info.put("position",position);
        info.put("money",money);
        info.put("type",type);
        info.put("name",name);
        return info;
    }


}
