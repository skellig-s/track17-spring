package track.container.beans;

import java.util.Objects;

/**
 * Коробка передач, поле - количество скоростей
 */
public class Gear {
    private Car car;

    public Gear() {
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "Gear{" +
                "Car=" + car +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Gear gear = (Gear) obj;
        return car == gear.car;
    }

    @Override
    public int hashCode() {
        return Objects.hash(car);
    }
}
