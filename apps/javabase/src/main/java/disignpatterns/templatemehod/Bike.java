package disignpatterns.templatemehod;

/**
 * Created by MIHE on 3/19/2019.
 */
public class Bike extends AbstractCar {
  @Override
  protected void start() {
    System.out.println("Start bike!");
  }

  @Override
  protected void shift() {
    System.out.println("Shift bike!");
  }

  @Override
  protected void stop() {
    System.out.println("Stop bike!");
  }
}
