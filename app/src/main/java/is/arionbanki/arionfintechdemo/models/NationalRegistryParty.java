package is.arionbanki.arionfintechdemo.models;

import android.os.Message;
import android.text.TextUtils;

import java.text.MessageFormat;

/**
 * Created by aegirt on 25.05.2016.
 */
public class NationalRegistryParty {
    private String city;
    private int fate;
    private String fullName;
    private String home;
    private String kennitala;
    private String postalCode;

    public String getCity() {
        return city;
    }

    public NationalRegistryParty setCity(String city) {
        this.city = city;
        return this;
    }

    public int getFate() {
        return fate;
    }

    public NationalRegistryParty setFate(int fate) {
        this.fate = fate;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public NationalRegistryParty setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getHome() {
        return home;
    }

    public NationalRegistryParty setHome(String home) {
        this.home = home;
        return this;
    }

    public String getKennitala() {
        return kennitala;
    }

    public NationalRegistryParty setKennitala(String kennitala) {
        this.kennitala = kennitala;
        return this;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public NationalRegistryParty setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public static class ViewModel {

        public final NationalRegistryParty model;

        public ViewModel(NationalRegistryParty model) {
            this.model = model;
        }

        public String getFullName() {
            return model.fullName;
        }

        public String getAddressLine() {
            if(TextUtils.isEmpty(model.postalCode)) {
                return model.home;
            }
            return MessageFormat.format("{0}, {1} {2}", model.home, model.postalCode, model.city);
        }
    }
}
