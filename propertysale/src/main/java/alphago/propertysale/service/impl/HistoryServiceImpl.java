package alphago.propertysale.service.impl;

import alphago.propertysale.entity.History;
import alphago.propertysale.entity.RecVO;
import alphago.propertysale.entity.returnVO.AuctionVO;
import alphago.propertysale.entity.returnVO.RunningAuctionAddress;
import alphago.propertysale.mapper.HistoryMapper;
import alphago.propertysale.service.HistoryService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Xiaohan Zhu
 * @since 2020-11-07
 */
@Service
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, History> implements HistoryService {

    @Autowired
    HistoryMapper historyMapper;

    @Override
    public void updateHistory(long uid, AuctionVO auctionVO) {
        historyMapper.update(null, new UpdateWrapper<History>().eq("uid", uid)
                .setSql("bedroom_num = bedroom_num + " + auctionVO.getBedroomNum())
                .setSql("bathroom_num = bathroom_num + " + auctionVO.getBathroomNum())
                .setSql("garage_num = garage_num + " + auctionVO.getGarageNum())
                .setSql("lat = lat + " + auctionVO.getLat())
                .setSql("lng = lng + " + auctionVO.getLng())
                .setSql("cnt = cnt + 1")
        );
    }

    public static List<RecVO> recommendations(RunningAuctionAddress query, List<RunningAuctionAddress> list){
        PriorityQueue<RunningAuctionAddress> heap =
                new PriorityQueue<>((a,b)-> Double.compare(b.getScore(), a.getScore()));

        for(RunningAuctionAddress comp : list){
            if(comp.getAid().equals(query.getAid())) {continue;}
            comp.setScore(score(query, comp));
            if(heap.size() < 3){
                heap.add(comp);
            }else{
                if(comp.getScore() < heap.peek().getScore()){
                    heap.poll();
                    heap.add(comp);
                }
            }
        }
        List<RecVO> ret = new ArrayList<>();
        while (!heap.isEmpty()){
            RunningAuctionAddress tmp = heap.poll();
            RecVO vo = new RecVO();
            BeanUtils.copyProperties(tmp, vo);
            ret.add(vo);
        }
        return ret;
    }

    private static double score(RunningAuctionAddress query, RunningAuctionAddress comp){
        double ret = query.getBathroomNum() * comp.getBathroomNum()
                + query.getBedroomNum() * comp.getBedroomNum()
                + query.getGarageNum() * comp.getGarageNum()
                + query.getLat() * comp.getLat()
                + query.getLng() * comp.getLng();

        double tmp = Math.sqrt(
                comp.getBathroomNum() * comp.getBathroomNum()
                + comp.getBedroomNum() * comp.getBedroomNum()
                + comp.getGarageNum() * comp.getGarageNum()
                + comp.getLat() * comp.getLat()
                + comp.getLng() * comp.getLng()
        );
        return ret / tmp;
    }
}
