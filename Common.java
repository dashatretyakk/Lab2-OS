public class Common {

  // Метод для перетворення рядка в ціле число. Обробляє виняткові ситуації.
  static public int s2i(String s) {
    int i = 0;

    try {
      i = Integer.parseInt(s.trim());
    } catch (NumberFormatException nfe) {
      System.out.println("NumberFormatException: " + nfe.getMessage());
    }
    return i;
  }

  // Метод для генерації випадкового числа від 0 до 1 за допомогою комплексної формули.
  static public double R1() {
    // Покращена генерація випадкових значень (System.currentTimeMillis() вже недостатньо швидка)
    java.util.Random generator = new java.util.Random(/*System.currentTimeMillis()*/System.nanoTime());
    double U = generator.nextDouble();
    while (U < 0 || U >= 1) {
      // Генерація випадкового U в межах (0, 1).
      U = generator.nextDouble();
    }
    double V = generator.nextDouble();
    while (V < 0 || V >= 1) {
      // Генерація випадкового V в межах (0, 1).
      V = generator.nextDouble();
    }

    double X = Math.sqrt((8 / Math.E)) * (V - 0.5) / U;


    if (!(R2(X, U))) {
      return -1;
    }
    if (!(R3(X, U))) {
      return -1;
    }
    if (!(R4(X, U))) {
      return -1;
    }
    return X;
  }

  // Перевірка умови R2 для випадкового числа.
  static public boolean R2(double X, double U) {
    return (X * X) <= (5 - 4 * Math.exp(.25) * U);
  }

  // Перевірка умови R3 для випадкового числа.
  static public boolean R3(double X, double U) {
    return (X * X) < (4 * Math.exp(-1.35) / U + 1.4);
  }

  // Перевірка умови R4 для випадкового числа.
  static public boolean R4(double X, double U) {
    return (X * X) < (-4 * Math.log(U));
  }
}