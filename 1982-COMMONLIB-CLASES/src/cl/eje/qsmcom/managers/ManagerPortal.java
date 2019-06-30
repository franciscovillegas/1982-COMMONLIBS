package cl.eje.qsmcom.managers;

public class ManagerPortal
{
  private static ManagerPortal instance;
  
  public static ManagerPortal getInstance()
  {
    if (instance == null) {
      synchronized (ManagerPortal.class)
      {
        if (instance == null) {
          instance = new ManagerPortal();
        }
      }
    }
    return instance;
  }
}
