package com.ysx.utils.datetime;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.zone.ZoneRules;
import java.util.Set;

/**
 * 获取当前系统的时区列表
 *
 * @author youngbear
 * @email youngbear@aliyun.com
 * @date 2022/6/15 23:18
 * @blog https://blog.csdn.net/next_second
 * @github https://github.com/YoungBear
 * @description 获取全球时区信息
 */
public class ZoneIdListTest {
    public static void main(String[] args) {
        Instant instant = Instant.now();
        Set<String> zoneIds = ZoneId.getAvailableZoneIds();
        System.out.println(zoneIds.size());
        zoneIds.stream().sorted().forEach(zoneIdString -> {
            ZoneId zoneId = ZoneId.of(zoneIdString);
            ZoneRules rules = zoneId.getRules();
            // 当前offset
            ZoneOffset offset = rules.getOffset(instant);
            // 标准offset
            ZoneOffset standardOffset = rules.getStandardOffset(instant);
            // 是否为夏令时
            boolean daylightSavings = rules.isDaylightSavings(instant);

            System.out.println(zoneIdString + ", offset: "
                    + offset + ", standardOffset: " + standardOffset
                    + ", daylightSavings: " + daylightSavings);
        });
    }
}
