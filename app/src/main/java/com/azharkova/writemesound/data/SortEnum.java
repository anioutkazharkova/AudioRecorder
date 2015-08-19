package com.azharkova.writemesound.data;

/**
 * Created by aniou_000 on 09.08.2015.
 */
public enum SortEnum {
    NAME,DATE,COLLECTION;

    public  SortEnum toSortEnum(int code)
    {
        switch(code)
        {
            case 0:
                return SortEnum.NAME;

            case 1:
                return SortEnum.DATE;

            case 2:
                return SortEnum.COLLECTION;

        }

        return SortEnum.NAME;
    }

    public int toInt()
    {
        if (this==SortEnum.NAME)
            return 0;
        if (this==SortEnum.DATE)
            return 1;
        if (this==SortEnum.COLLECTION)
            return 2;
        return 0;
    }

}
