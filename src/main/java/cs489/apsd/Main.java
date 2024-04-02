package cs489.apsd;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Employee> employees = loadData();


  printEmployeesJSON(employees);


    printMonthlyUpcomingEnrolleesJSON(employees);
    }

    private static List<Employee> loadData() {


        List<Employee> employees = new ArrayList<>();


        Employee employee1 = new Employee(1, "Daniel", "Agar", LocalDate.of(2018, 1, 17), 105945.50);
        Employee employee2 = new Employee(2, "Benard", "Shaw", LocalDate.of(2018, 1, 15), 197750.00);
        Employee employee3 = new Employee(3, "Carly", "Agar", LocalDate.of(2014, 5, 16), 842000.75);
        Employee employee4 = new Employee(4, "Wesley", "Schneider", LocalDate.of(2018, 11, 2), 74500.00);

        employee1.setPensionPlan(new PensionPlan("EX1089", LocalDate.of(2023, 1, 17), 100.00));

        employees.add(employee1);
        employees.add(employee2);
        employees.add(employee3);
        employees.add(employee4);


        return employees;
    }

    private static void printEmployeesJSON(List<Employee> employees) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .setPrettyPrinting()
                .create();
        employees.sort(Comparator.comparing(Employee::getLastName).thenComparing(Employee::getYearlySalary).reversed());
        String json = gson.toJson(employees);
        System.out.println("List of all employees in JSON format:");
        System.out.println(json);
    }



    private static void printMonthlyUpcomingEnrolleesJSON(List<Employee> employees) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .setPrettyPrinting()
                .create();

        LocalDate nextMonthFirstDay = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        LocalDate nextMonthLastDay = LocalDate.now().plusMonths(1).withDayOfMonth(nextMonthFirstDay.lengthOfMonth());

        List<Employee> upcomingEnrollees = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getEmploymentDate().isBefore(nextMonthFirstDay.minusYears(5)) ||
                    employee.getEmploymentDate().isAfter(nextMonthLastDay.minusYears(5).minusDays(1))) {
                if (employee.getPensionPlan() == null) {
                    upcomingEnrollees.add(employee);
                }
            }
        }
        upcomingEnrollees.sort(Comparator.comparing(Employee::getEmploymentDate));

        String json = gson.toJson(upcomingEnrollees);
        System.out.println("\nMonthly Upcoming Enrollees report in JSON format:");
        System.out.println(json);
    }



    private static class LocalDateTypeAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        @Override
        public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return LocalDate.parse(json.getAsString());
        }
    }
}


