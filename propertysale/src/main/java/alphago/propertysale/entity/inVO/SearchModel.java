package alphago.propertysale.entity.inVO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
* @Description: Value Object to accept search condition from front end
*/
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
    private LocalDateTime startDate;
    private LocalDateTime endDate;

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

    /**
    * @Description: Check if the search filter is empty
    */
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
