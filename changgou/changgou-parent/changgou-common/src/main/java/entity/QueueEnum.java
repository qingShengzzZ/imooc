package entity;

public enum QueueEnum {
    /**
     * 消息通知队列
     */
    QUEUE_ORDER("exchange.order", "queue.order", "queue.order"),
    /**
     * 消息通知ttl队列
     */
    QUEUE_SECKILLORDER("exchange.seckillorder", "queue.seckillorder", "queue.seckillorder");

    /**
     * 交换名称
     */
    private String exchange;
    /**
     * 队列名称
     */
    private String name;
    /**
     * 路由键
     */
    private String routeKey;

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }

    public String getExchange() {
        return exchange;
    }

    public String getName() {
        return name;
    }

    public String getRouteKey() {
        return routeKey;
    }
}
