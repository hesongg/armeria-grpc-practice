package com.study.webflux.util;

import com.google.protobuf.Timestamp;

import java.time.Instant;

public class ProtoTimeStampMapper {
    public static Timestamp fromInstant(Instant instant) {
        return Timestamp.newBuilder()
                        .setSeconds(instant.getEpochSecond())
                        .setNanos(instant.getNano())
                        .build();
    }
}
