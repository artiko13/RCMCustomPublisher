package pl.akozioro.rcm.model;

import java.util.List;
import java.util.Objects;

public class GridRowModel {

    private String number; //N1
    private String count; //G1
    private String lastLap; //H1
    private String bestLap; //K1
    private List<String> times; //HA1

    public GridRowModel() {

    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        GridRowModel gridRowModel = new GridRowModel();

        public Builder() {

        }

        public GridRowModel build() {
            return gridRowModel;
        }

        public Builder number(String number) {
            gridRowModel.number = number;
            return this;
        }

        public Builder count(String count) {
            gridRowModel.count = count;
            return this;
        }

        public Builder lastLap(String lastLap) {
            gridRowModel.lastLap = lastLap;
            return this;
        }

        public Builder bestLap(String bestLap) {
            gridRowModel.bestLap = bestLap;
            return this;
        }

        public Builder times(List<String> times) {
            gridRowModel.times = times;
            return this;
        }
    }

    public String getLastLap() {
        return lastLap;
    }

    public String getBestLap() {
        return bestLap;
    }

    public String getCount() {
        return count;
    }

    public String getNumber() {
        return number;
    }

    public List<String> getTimes() {
        return times;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GridRowModel that = (GridRowModel) o;
        return Objects.equals(number, that.number) && Objects.equals(count, that.count) && Objects.equals(lastLap, that.lastLap)
                && Objects.equals(bestLap, that.bestLap) && Objects.equals(times, that.times);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, count, lastLap, bestLap, times);
    }
}
