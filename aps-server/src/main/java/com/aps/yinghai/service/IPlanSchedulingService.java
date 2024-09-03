package com.aps.yinghai.service;

import java.util.List;

/**
 * 船舶计划排产
 */
public interface IPlanSchedulingService {

    /**
     * 长期计划排产
     * @return
     */
    List longTermScheduling();

    /**
     * 昼夜计划排产
     * @return
     */
    List dayNightScheduling();

}
