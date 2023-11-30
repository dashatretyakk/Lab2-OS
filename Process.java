// Клас, що представляє окремий процес в системі планування.
public class Process {

  // Поля класу, які містять інформацію про властивості конкретного процесу.
  public int cputime;
  public int ioblocking;
  public int cpudone;
  public int ionext;
  public int numblocked;

  // Конструктор класу для ініціалізації полів процесу.
  public Process(int cputime, int ioblocking, int cpudone, int ionext, int numblocked) {
    // Присвоєння значень переданих аргументів полям класу.
    this.cputime = cputime;
    this.ioblocking = ioblocking;
    this.cpudone = cpudone;
    this.ionext = ionext;
    this.numblocked = numblocked;
  }
}