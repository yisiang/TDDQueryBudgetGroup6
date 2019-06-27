package changheng;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class BudgetQuery {
    public Double query(LocalDate from, LocalDate to) {
        List<Budget> budgets = getAllBudgets();
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Invalid date range");
        }

        return queryTotalAmount(new Period(from, to), budgets);
    }

    protected List<Budget> getAllBudgets() {
        Budget a = new Budget("2019/02", 28000d);
        Budget b = new Budget("2019/03", 31000d);
        return Arrays.asList(a, b);
    }

    private int getOverlappingDays(Period period, Budget budget) {
        LocalDate overlappingStart = period.getStart().isAfter(budget.firstDay()) ? period.getStart() : budget.firstDay();
        LocalDate overlappingEnd = period.getEnd().isBefore(budget.lastDay()) ? period.getEnd() : budget.lastDay();
        if (overlappingEnd.isBefore(overlappingStart)) {
            return 0;
        }
        return new Period(overlappingStart, overlappingEnd).intervalDays();
    }

    private Double queryTotalAmount(Period period, List<Budget> budgets) {
        double result = 0;

        for (Budget budget : budgets) {
            result += budget.dailyAmount() * getOverlappingDays(period, budget);
        }

        return result;
    }
}
