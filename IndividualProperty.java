import java.util.*;
import java.util.function.Consumer;

public class IndividualProperty {

    String name;
    int family;
    int owner;
    int price;
    int landRent;
    int monopoly;
    int oneHouse;
    int twoHouse;
    int threeHouse;
    int fourHouse;
    int hotel;
    int status;
    //{0:market,1:owner,2:monoploy,3:mortgage}
    int mortgage;
    int buildCost;
    int rentPrice;
    int position;
    String colour;

    public IndividualProperty(PropertyBuilder propertyBuilder) {
        this.name= propertyBuilder.name;
        this.family= propertyBuilder.family;
        this.owner= propertyBuilder.owner;
        this.price= propertyBuilder.price;
        this.landRent= propertyBuilder.landRent;
        this.monopoly= propertyBuilder.monopoly;
        this.oneHouse= propertyBuilder.oneHouse;
        this.twoHouse= propertyBuilder.twoHouse;
        this.threeHouse= propertyBuilder.threeHouse;
        this.fourHouse= propertyBuilder.fourHouse;
        this.hotel= propertyBuilder.hotel;
        this.status= propertyBuilder.status;
        //{0:market,1:owner,2:mortgage}
        this.mortgage= propertyBuilder.mortgage;
        this.buildCost= propertyBuilder.buildCost;
        this.rentPrice= propertyBuilder.rentPrice;
        this.position= propertyBuilder.position;
        this.colour= propertyBuilder.colour;
        owner=-1;
    }
    public static class PropertyBuilder {
        String name;
        int family;
        int owner;
        int price;
        int landRent;
        int monopoly;
        int oneHouse;
        int twoHouse;
        int threeHouse;
        int fourHouse;
        int hotel;
        int status;
        //{0:market,1:owner,2:monoploy,3:mortgage}
        int mortgage;
        int buildCost;
        int rentPrice;
        int position;
        String colour;

        public PropertyBuilder with(
                Consumer<PropertyBuilder> builderFunction) {
            builderFunction.accept(this);
            return this;
        }

        public IndividualProperty createBuilding() {
            return new IndividualProperty(this);
        }
    }


    LinkedHashMap info = new LinkedHashMap();
    public LinkedHashMap getInfo(){
        info.put("name",name);
        info.put("family",family);
        info.put("owner",owner);
        info.put("status",status);
        info.put("price",price);
        info.put("rentPrice",rentPrice);
        info.put("colour",colour);
        info.put("mortgage",mortgage);
        info.put("buildCost",buildCost);
        info.put("landRent",landRent);
        info.put("monopoly",monopoly);
        info.put("oneHouse",oneHouse);
        info.put("twoHouse",twoHouse);
        info.put("threeHouse",threeHouse);
        info.put("fourHouse",fourHouse);
        info.put("hotel",hotel);
        info.put("position",position);

        return info;
    }

}
