package com.amt.wechat.common.constants;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-24 18:08
 * @version 1.0
 */
public enum DurationUnit {

    DAY,WEEK,MONTH,YEAR;


    private static DurationUnit[][] _pools ={
            /* day:*/ {DurationUnit.WEEK,DurationUnit.MONTH,DurationUnit.YEAR},
            /* week*/ {DurationUnit.MONTH,DurationUnit.YEAR},
            /* month*/{DurationUnit.YEAR},
            /* year*/ {}
    };

    public static DurationUnit value(String unitName){
        for(DurationUnit e:DurationUnit.values()){
            if(e.name().equalsIgnoreCase(unitName)){
                return e;
            }
        }
        return null;
    }

    /**
     * 是否可升级
     * @param unitName
     * @return
     */
    public boolean allowUpgrade(String unitName){
        DurationUnit[] pools = _pools[this.ordinal()];
        if(pools.length <= 0){
            return false;
        }
        for(DurationUnit e:pools){
            if(e.name().equalsIgnoreCase(unitName)){
                return true;
            }
        }
        return false;
    }
}
