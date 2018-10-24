package com.envisioniot.enos.iot_mqtt_sdk.util;

import java.io.Serializable;

public class Pair<F, S> implements Serializable {
    private static final long serialVersionUID = -3820567846153604437L;
    public F first;
    public S second;

    public Pair() {
    }

    public Pair(F f, S s) {
        this.first = f;
        this.second = s;
    }

    public static <FT, ST> Pair<FT, ST> makePair(FT f, ST s) {
        return new Pair<FT, ST>(f, s);
    }

    private static <T> boolean eq(T o1, T o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    @Override
    public boolean equals(Object o) {
        Pair<F, S> pr = (Pair) o;
        return pr == null ? false : eq(this.first, pr.first) && eq(this.second, pr.second);
    }

    private static int h(Object o) {
        return o == null ? 0 : o.hashCode();
    }

    @Override
    public int hashCode() {
        int seed = h(this.first);
        seed ^= h(this.second) + -1640531527 + (seed << 6) + (seed >> 2);
        return seed;
    }

    public F getFirst() {
        return this.first;
    }

    public S getSecond() {
        return this.second;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(this.first).append(", ").append(this.second).append("}");
        return sb.toString();
    }
}