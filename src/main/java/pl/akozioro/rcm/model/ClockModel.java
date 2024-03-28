package pl.akozioro.rcm.model;

import java.util.Objects;

public class ClockModel {
    private int remaining;
    private int total;
    private int current;

    public ClockModel(int remaining, int total, int current) {
        this.remaining = remaining;
        this.total = total;
        this.current = current;
    }

    public int getRemaining() {
        return remaining;
    }

    public int getTotal() {
        return total;
    }

    public int getCurrent() {
        return current;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClockModel that = (ClockModel) o;
        return remaining == that.remaining && total == that.total && current == that.current;
    }

    @Override
    public int hashCode() {
        return Objects.hash(remaining, total, current);
    }
}
