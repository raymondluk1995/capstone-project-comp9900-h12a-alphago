package alphago.propertysale.service.impl;

import alphago.propertysale.entity.History;
import alphago.propertysale.entity.RecVO;
import alphago.propertysale.entity.returnVO.AuctionVO;
import alphago.propertysale.entity.returnVO.RunningAuctionAddress;
import alphago.propertysale.mapper.HistoryMapper;
import alphago.propertysale.service.HistoryService;
import alphago.propertysale.utils.FileUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    private static final double AREA = 25;

    @Autowired
    HistoryMapper historyMapper;

    public static List<RecVO> recommendations(RunningAuctionAddress query, List<RunningAuctionAddress> list){
        PriorityQueue<RunningAuctionAddress> heap =
                new PriorityQueue<>((a,b)-> Double.compare(b.getScore(), a.getScore()));

        for(RunningAuctionAddress comp : list){
            if(comp.getAid().equals(query.getAid())) {continue;}
            comp.setScore(score(query, comp));
            System.out.print(comp+"----> ");
            if(heap.size() < 4){
                heap.add(comp);
            }else{
                if(comp.getScore() < heap.peek().getScore()){
                    heap.poll();
                    heap.add(comp);
                }
            }
        }
        LinkedList<RecVO> ret = new LinkedList<>();
        while (!heap.isEmpty()){
            RunningAuctionAddress tmp = heap.poll();
            RecVO vo = new RecVO();
            BeanUtils.copyProperties(tmp, vo);
            vo.setBathroomNum(tmp.getBathroomNum().intValue());
            vo.setBedroomNum(tmp.getBedroomNum().intValue());
            vo.setGarageNum(tmp.getGarageNum().intValue());
            vo.setPhoto(FileUtil.getImages(tmp.getPid()).get(0));
            ret.addFirst(vo);
        }
        return ret;
    }

    private static double score(RunningAuctionAddress query, RunningAuctionAddress comp){
        double ret = Math.pow(query.getBathroomNum() - comp.getBathroomNum(),2)
                + Math.pow(query.getBedroomNum() - comp.getBedroomNum(), 2)
                + Math.pow(query.getGarageNum() - comp.getGarageNum(), 2)
                + Math.pow((query.getLat() - comp.getLat()) / 10 , 2)
                + Math.pow((query.getLng() - comp.getLng()) / 10 , 2);

        return ret;
    }
}
