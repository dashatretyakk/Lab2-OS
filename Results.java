// Клас, який представляє результати виконання алгоритму планування.
public class Results {

  // Поля класу, що містять інформацію про тип планування, назву планування та час виконання.
  public String schedulingType;
  public String schedulingName;
  public int compuTime;

  // Конструктор класу для ініціалізації полів результатів.
  public Results(String schedulingType, String schedulingName, int compuTime) {
    // Присвоєння значень переданих аргументів полям класу.
    this.schedulingType = schedulingType;
    this.schedulingName = schedulingName;
    this.compuTime = compuTime;
  } 	
}