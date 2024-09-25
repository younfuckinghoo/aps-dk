package com.aps.yinghai.dto.igtos;

import com.aps.yinghai.iGTOS.BizDaynightClassPlan;
import com.aps.yinghai.iGTOS.BizDaynightPlanTeam;
import com.aps.yinghai.iGTOS.BizShipWorkPlan;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class BizDayNightClassDTO {

public static final String DAY_NAME = "白班";
public static final String DAY_PERIOD = "06:00-18:00";
public static final String NIGHT_NAME = "夜班";
public static final String NIGHT_PERIOD = "18:00-06:00";

    private BizDaynightClassPlan classPlan;


    private List<BizDaynightPlanTeam> planTeamList;


    public static BizDayNightClassDTO buildDayClass(String workPlanId){
        BizDayNightClassDTO bizDayNightClassDTO = new BizDayNightClassDTO();
        BizDaynightClassPlan bizDaynightClassPlan = new BizDaynightClassPlan();
        bizDayNightClassDTO.classPlan = bizDaynightClassPlan;
        bizDaynightClassPlan.setClassPeriod(DAY_PERIOD);
        bizDaynightClassPlan.setClassnoName(DAY_NAME);
        bizDaynightClassPlan.setCreatorId("shuiguiyuan");
        bizDaynightClassPlan.setCreator("shuiguiyuan");
        bizDaynightClassPlan.setCreateDate(LocalDateTime.now());
//        bizDaynightClassPlan.setId(UUID.randomUUID().toString());
        bizDaynightClassPlan.setDaynightPlanId(workPlanId);
        bizDaynightClassPlan.setWorkTime(BigDecimal.ZERO);
        bizDaynightClassPlan.setPlanWeight(BigDecimal.ZERO);
        return bizDayNightClassDTO;
    }

    public static BizDayNightClassDTO buildNightClass(String workPlanId){
        BizDayNightClassDTO bizDayNightClassDTO = new BizDayNightClassDTO();
        BizDaynightClassPlan bizDaynightClassPlan = new BizDaynightClassPlan();
        bizDayNightClassDTO.classPlan = bizDaynightClassPlan;
        bizDaynightClassPlan.setClassPeriod(NIGHT_PERIOD);
        bizDaynightClassPlan.setClassnoName(NIGHT_NAME);
        bizDaynightClassPlan.setCreatorId("shuiguiyuan");
        bizDaynightClassPlan.setCreator("shuiguiyuan");
        bizDaynightClassPlan.setCreateDate(LocalDateTime.now());
//        bizDaynightClassPlan.setId(UUID.randomUUID().toString());
        bizDaynightClassPlan.setDaynightPlanId(workPlanId);
        bizDaynightClassPlan.setWorkTime(BigDecimal.ZERO);
        bizDaynightClassPlan.setPlanWeight(BigDecimal.ZERO);

        return bizDayNightClassDTO;
    }

}
