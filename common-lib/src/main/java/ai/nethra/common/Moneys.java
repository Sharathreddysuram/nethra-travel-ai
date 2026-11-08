package ai.nethra.common;
import java.math.BigDecimal; import java.math.RoundingMode;
public final class Moneys {
  private Moneys() {}
  public static BigDecimal inr(long value) { return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP); }
  public static BigDecimal round(BigDecimal value) { return value.setScale(2, RoundingMode.HALF_UP); }
  public static BigDecimal rupees(BigDecimal value) { return value.setScale(0, RoundingMode.HALF_UP); }
  public static BigDecimal percent(BigDecimal base, double pct) { return round(base.multiply(BigDecimal.valueOf(pct)).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)); }
  public static String format(BigDecimal value) { return "₹" + rupees(value).toPlainString(); }
}
