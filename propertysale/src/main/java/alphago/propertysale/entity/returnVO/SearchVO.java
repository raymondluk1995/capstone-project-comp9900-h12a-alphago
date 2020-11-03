package alphago.propertysale.entity.returnVO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SearchVO {
    private boolean isRab;// if user login -> rab?
    private List<SearchResVO> resVOList;

}
