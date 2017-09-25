package com.chinasoft.wt.vo;

/**
 * Created by VerRan.Liu on 2017/9/22.
 */
public class SummaryVO {
    String expectWTL ;
    String actWTL ;
    String alvTWL="0分钟" ;
    String shouldApendTWL ="0分钟" ;

    public String getShouldApendTWL() {
        return shouldApendTWL;
    }

    public void setShouldApendTWL(String shouldApendTWL) {
        this.shouldApendTWL = shouldApendTWL;
    }

    public String getExpectWTL() {
        return expectWTL;
    }

    public void setExpectWTL(String expectWTL) {
        this.expectWTL = expectWTL;
    }

    public String getActWTL() {
        return actWTL;
    }

    public void setActWTL(String actWTL) {
        this.actWTL = actWTL;
    }

    public String getAlvTWL() {
        return alvTWL;
    }

    public void setAlvTWL(String alvTWL) {
        this.alvTWL = alvTWL;
    }
}
