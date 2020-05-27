package top.p3wj.bean;

/**
 * @author Aaron
 * @description
 * @date 2020/5/12 8:05 PM
 */
public class Color {
    private Car car;

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "Color{" +
                "car=" + car +
                '}';
    }
}
