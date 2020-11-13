package alphago.propertysale.entity.inVO;

import lombok.Data;
import lombok.experimental.Accessors;


// localhost:8080/search
// localhost:8080/search?startDate=xxxx&aaa=xxx...
@Data
@Accessors(chain = true)
public class SearchModel {
    // address table
    private String suburb;

    // tao add state.
    private String state;

    private Long lat;
    private Long lng;

    private String postcode;
    // auction
    private Long startDate;
    private Long endDate;

    private Long minPrice;
    private Long maxPrice;

    // property features
    private Integer bedrooms;
    private Integer bathrooms;
    private Integer garages;

    private String propertyType;
    private Integer minArea;
    private Integer maxArea;
    private Integer currPage;


    private String order; // desc or asc


    public boolean isAllEmpty() {
        return this.suburb == null &&
                this.state == null &&
                this.postcode == null &&
                this.startDate == null &&
                this.endDate == null &&
                this.minPrice == null &&
                this.maxPrice == null &&
                this.bedrooms == null &&
                this.bathrooms == null &&
                this.garages == null &&
                this.propertyType == null &&
                this.minArea == null &&
                this.maxArea == null;
    }


}
