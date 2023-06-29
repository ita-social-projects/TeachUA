package com.softserve.amqp.message_producer;

import com.softserve.amqp.marker.MQMarker;

public interface RabbitMQMessageProducer<T extends MQMarker> {
    void publish(T payload);
}
