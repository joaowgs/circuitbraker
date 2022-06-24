package com.circuitbraker.circuitbraker.config.props;

public final class KafkaProperties {
    public static final String ENABLE_AUTO_COMMIT = "enable.auto.commit";
    public static final String AUTO_COMMIT_INTERVAL = "auto.commit.interval.ms";
    public static final String MAX_POLL_RECORDS = "max.poll.records";

    private KafkaProperties() { }
}
