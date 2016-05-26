package is.arionbanki.arionfintechdemo.models;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Created by aegirt on 23.05.2016.
 */
public class Account {
    private String customName;
    private String accountId;
    private String accountOwnerID;
    private double balance;

    public Account setCustomName(String name) {
        this.customName = name;
        return this;
    }

    public Account setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public Account setAccountOwnerID(String accountOwnerID) {
        this.accountOwnerID = accountOwnerID;
        return this;
    }

    public String getCustomName() {
        return customName;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountOwnerID() {
        return accountOwnerID;
    }

    public Account setBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public static final class ViewModel {
        private static final DecimalFormat DECIMAL_FORMAT;
        static {
            DECIMAL_FORMAT = (DecimalFormat) DecimalFormat.getInstance(Locale.GERMAN);
            DECIMAL_FORMAT.setMaximumFractionDigits(0);
            DECIMAL_FORMAT.setGroupingUsed(true);
            DECIMAL_FORMAT.setGroupingSize(3);
        }
        public final Account model;

        public ViewModel(Account item) {
            this.model = item;
        }

        public String getBalanceText() {
            if(Double.isNaN(model.balance)) return "";

            return String.format("%s kr.", DECIMAL_FORMAT.format(model.balance));
        }

        public String getCustomName() {
            return model.getCustomName();
        }

        public String getAccountId() {
            return model.getAccountId();
        }
    }
}
