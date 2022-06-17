package com.ysx.utils.datetime;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Set;

/**
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022/6/15 23:18
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description 获取全球时区信息
 */
public class ZoneIdUtils {
    public static void main(String[] args) {
        Set<String> zoneIds = ZoneId.getAvailableZoneIds();
        System.out.println(zoneIds.size());
        zoneIds.stream().sorted().forEach( zoneIdString -> {
            ZoneId zoneId = ZoneId.of(zoneIdString);
            ZoneOffset zoneOffset = ZonedDateTime.now(zoneId).getOffset();
            System.out.println(zoneIdString + " " + zoneOffset.toString());
        });
    }
}
