package com.aps.yinghai.dto;

import com.aps.yinghai.entity.BollardInfo;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class PlanningBollardDTO implements Cloneable{

    private BollardInfo bollardInfo;

    public static PlanningBollardDTO packageBollardDTO(BollardInfo bollardInfo){
        PlanningBollardDTO planningBollardDTO = new PlanningBollardDTO();
        planningBollardDTO. bollardInfo = bollardInfo;
        return planningBollardDTO;
    }

    /**
     * 在某个时间点是否可用
     * 这个时间只跟靠泊、离泊时间相关
     * @param readyTime
     * @return
     */
    public boolean isFree(LocalDateTime readyTime){
        boolean flag = bollardInfo.getOccupyUntil()==null || readyTime.isAfter(bollardInfo.getOccupyUntil());
//        if (flag) bollardInfo.setOccupyUntil(null);
        return flag;
    }

    /**
     * 更新被占用时间
     * @param leaveTime
     */
    public void updateOccupiedTime(LocalDateTime leaveTime) {
        if (this.bollardInfo.getOccupyUntil()==null || this.bollardInfo.getOccupyUntil().isBefore(leaveTime)) {
            this.bollardInfo.setOccupyUntil(leaveTime);
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        PlanningBollardDTO planningBollardDTO= (PlanningBollardDTO) super.clone();
        planningBollardDTO.setBollardInfo((BollardInfo)this.bollardInfo.clone());
        return planningBollardDTO;
    }


}
