package alphago.propertysale.entity.inVO;

import lombok.Data;
import lombok.experimental.Accessors;


// localhost:8080/search
// localhost:8080/search?startDate=xxxx&aaa=xxx...
@Data
@Accessors(chain = true)
public class SearchModel {
    private String suburb;
    private String postcode;

    private Long startDate;
    private Long endDate;

    private String minPrice;
    private String maxPrice;


    private Integer bedRooms;
    private Integer bathRooms;
    private Integer garages;

    private String order; // desc or asc
    private String propertyType;
    private Integer minArea;
    private Integer maxArea;


    public boolean isAllEmpty() {
        return this.suburb == null &&
                this.postcode == null &&
                this.startDate == null &&
                this.endDate == null &&
                this.minPrice == null &&
                this.maxPrice == null &&
                this.bedRooms == null &&
                this.bathRooms == null &&
                this.garages == null &&
                this.order == null &&
                this.propertyType == null &&
                this.minArea == null &&
                this.maxArea == null;
    }


}
